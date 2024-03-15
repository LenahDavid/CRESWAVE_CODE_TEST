package com.example.blogging.service;

import com.example.blogging.entity.BlogPost;
import com.example.blogging.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

public interface CommentService {
    public Comment createComment(Comment comment);
    public Optional<Comment> getCommentById(Long id);

    public void deleteCommentById(Long id);
    public Comment updateComment(Comment comment);

    Page<Comment> getAllComments(PageRequest pageable);
}
