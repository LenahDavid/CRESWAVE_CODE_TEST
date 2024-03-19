
package com.example.blogging.service;

import com.example.blogging.dto.CommentResponse;
import com.example.blogging.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    CommentResponse createComment(CommentResponse comment, Long id, String username);
    Optional<CommentResponse> getCommentById(Long id);
    void deleteCommentById(Long id, String username);
    CommentResponse updateComment(Long id, Comment updatedComment, String username);
    Page<CommentResponse> getAllComments(Pageable pageable);
    List<CommentResponse> getComments();
}
