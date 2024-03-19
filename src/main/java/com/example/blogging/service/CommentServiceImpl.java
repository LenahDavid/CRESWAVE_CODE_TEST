package com.example.blogging.service;

import com.example.blogging.dto.CommentResponse;
import com.example.blogging.entity.BlogPost;
import com.example.blogging.entity.Comment;
import com.example.blogging.entity.Role;
import com.example.blogging.entity.User;
import com.example.blogging.exceptions.CommentNotFoundException;
import com.example.blogging.exceptions.UserUnAuthorizedException;
import com.example.blogging.repository.BlogPostRepository;
import com.example.blogging.repository.CommentRepository;
import com.example.blogging.repository.UserRepository;
import com.example.blogging.util.BlogPostMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import com.example.blogging.dto.CommentMapper;
@Slf4j
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BlogPostService blogPostService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BlogPostRepository blogPostRepository;

    @Override
    public CommentResponse createComment(CommentResponse comment, Long id, String username) {
        BlogPost post = blogPostRepository.getById(id);

        Comment newComment = new Comment();
        newComment.setUser(username);
        newComment.setContent(comment.getContent());
        newComment.setBlogPost(post);


        commentRepository.save(newComment);

        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .user(username)
                .build();

    }

    @Override
    public Optional<CommentResponse> getCommentById(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new CommentNotFoundException("Comment with id " + id + " not found");
        }
        return Optional.ofNullable(BlogPostMapper.maptoCommentDto(commentRepository.findById(id).get()));
    }

    @Override
    public void deleteCommentById(Long id, String username) {

        if (!commentRepository.existsById(id)) {
            throw new CommentNotFoundException("Comment with id " + id + " not found");
        }

//        Comment existingComment = commentRepository.findById(id).get();
        CommentResponse commentResponse = getCommentById(id).get();

        User user = userRepository.findByUsername(username).get();
        if (username == null) {
            throw new EntityNotFoundException("User not found");
        }
        if(user.getRole().equals(Role.ADMIN)) {
            commentRepository.deleteById(id);
        } else if (user.getRole().equals(Role.USER)) {
            //check if user is the author of the comment
            if (!commentResponse.getUser().equals(username)) {
                throw new UserUnAuthorizedException("You are not authorized to delete this comment");
            }
            commentRepository.deleteById(id);
        }


    }

    @Override
    public CommentResponse updateComment(Long id, Comment updatedComment, String username) {
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment with id " + id + " not found"));

        //check if user is the author of the comment
        if (!existingComment.getUser().equals(username)) {
            throw new UserUnAuthorizedException("You are not authorized to update this comment");
        }

        existingComment.setContent(updatedComment.getContent());
        return BlogPostMapper.maptoCommentDto(commentRepository.save(existingComment));
    }
    @Override
    public Page<CommentResponse> getAllComments(Pageable pageable) {
        Page<Comment> commentPage = commentRepository.findAll(pageable);
        List<CommentResponse> commentResponses = new ArrayList<>();
        for (Comment comment : commentPage.getContent()) {
            // Manually map comment to commentResponse
            CommentResponse commentResponse = new CommentResponse();
            commentResponse.setId(comment.getId());
            commentResponse.setContent(comment.getContent());
            // Map other properties as needed
            commentResponses.add(commentResponse);
        }
        return new PageImpl<>(commentResponses, pageable, commentPage.getTotalElements());
    }







    @Override
    public List<CommentResponse> getComments() {
        List<Comment> comments = commentRepository.findAll();
        List<CommentResponse> commentResponses = new ArrayList<>();

        for(Comment comment: comments){
            commentResponses.add(BlogPostMapper.maptoCommentDto(comment));
        }
        return commentResponses;
    }
}