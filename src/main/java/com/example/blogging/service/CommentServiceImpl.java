package com.example.blogging.service;

import com.example.blogging.dto.BlogPostResponse;
import com.example.blogging.dto.CommentResponse;
import com.example.blogging.entity.BlogPost;
import com.example.blogging.entity.Comment;
import com.example.blogging.entity.Role;
import com.example.blogging.entity.User;
import com.example.blogging.repository.BlogPostRepository;
import com.example.blogging.repository.CommentRepository;
import com.example.blogging.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;
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
    public CommentResponse createComment(Comment comment, Long id, String username) {



        User user = userRepository.findByUsername(username).get();
        BlogPost post = blogPostRepository.getReferenceById(id);
        comment.setUser(user);
        comment.setBlogPost(post);
        commentRepository.save(comment);

        return CommentResponse.builder()
                .id(comment.getId())
                .blogTitle(post.getTitle())
                .content(comment.getContent())
                .user(comment.getUser().getUsername())
                .build();

    }

    @Override
    public Optional<Comment> getCommentById(Long id) {
        return commentRepository.findById(id);
    }

    @Override
    public void deleteCommentById(Long id, String username) {
        if (!commentRepository.existsById(id)) {
            throw new EntityNotFoundException("Comment with id " + id + " not found");
        }

        Comment existingComment = commentRepository.findById(id).get();

        User user = userRepository.findByUsername(username).get();

        if(user.getRole().equals(Role.ADMIN)) {
            commentRepository.deleteById(id);
        } else if (user.getRole().equals(Role.USER)) {
            //check if user is the author of the comment
            if (!existingComment.getUser().getUsername().equals(username)) {
                throw new IllegalArgumentException("You are not authorized to update this comment");
            }
            commentRepository.deleteById(id);
        }


    }

    @Override
    public Comment updateComment(Comment updatedComment, String username) {
        Long id = updatedComment.getId();
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id " + id + " not found"));

        //check if user is the author of the comment
        if (!existingComment.getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("You are not authorized to update this comment");
        }

        existingComment.setContent(updatedComment.getContent());
        return commentRepository.save(existingComment);
    }
    @Override
    public Page<Comment> getAllComments(PageRequest pageable) {
        return commentRepository.findAll(pageable);
    }
}