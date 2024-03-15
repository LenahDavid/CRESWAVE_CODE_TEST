package com.example.blogging.service;

import com.example.blogging.entity.BlogPost;
import com.example.blogging.entity.Comment;
import com.example.blogging.repository.BlogPostRepository;
import com.example.blogging.repository.CommentRepository;
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

    @Override
    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Optional<Comment> getCommentById(Long id) {
        return commentRepository.findById(id);
    }

    @Override
    public void deleteCommentById(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new EntityNotFoundException("Comment with id " + id + " not found");
        }
        commentRepository.deleteById(id);
    }

    @Override
    public Comment updateComment(Comment updatedComment) {
        Long id = updatedComment.getId();
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id " + id + " not found"));
        existingComment.setContent(updatedComment.getContent());
        return commentRepository.save(existingComment);
    }
    @Override
    public Page<Comment> getAllComments(PageRequest pageable) {
        return commentRepository.findAll(pageable);
    }
}