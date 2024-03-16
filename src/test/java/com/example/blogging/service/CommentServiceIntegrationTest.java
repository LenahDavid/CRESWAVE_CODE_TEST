package com.example.blogging.service;

import com.example.blogging.dto.CommentResponse;
import com.example.blogging.entity.Comment;
import com.example.blogging.repository.CommentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentServiceIntegrationTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    public void testCreateComment() {
        // Mocking dependencies
        Comment comment = new Comment();
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentResponse createdCommentResponse = commentService.createComment(comment, 1L, "username");

        // Assertions
        assertTrue(createdCommentResponse != null);
        assertEquals("content", createdCommentResponse.getContent()); // Assuming a content field in CommentResponse
    }

    @Test
    public void testGetCommentById() {
        // Mocking dependencies
        Comment comment = new Comment();
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        // Test method
        Optional<Comment> result = commentService.getCommentById(1L);

        // Assertions
        assertTrue(result.isPresent());
        assertEquals(comment, result.get());
    }

    @Test
    public void testUpdateComment() {
        // Mocking dependencies
        Comment comment = new Comment();
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        // Test method
        Comment updatedCommentResponse = commentService.updateComment(comment, "username");

        // Assertions
        assertTrue(updatedCommentResponse != null); // Add relevant assertions
    }

    @Test
    public void testGetAllComments() {
        // Mocking dependencies
        List<Comment> comments = new ArrayList<>();
        when(commentRepository.findAll(any(PageRequest.class))).thenReturn(new PageImpl<>(comments));

        // Test method
        Page<Comment> result = commentService.getAllComments(PageRequest.of(0, 10));

        // Assertions
        assertEquals(comments.size(), result.getContent().size());
    }
}



