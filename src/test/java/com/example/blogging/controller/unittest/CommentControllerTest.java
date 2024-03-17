package com.example.blogging.controller.unittest;

import com.example.blogging.controllers.BlogPostController;
import com.example.blogging.controllers.CommentController;
import com.example.blogging.dto.CommentResponse;
import com.example.blogging.entity.Comment;
import com.example.blogging.service.BlogPostService;
import com.example.blogging.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class) // Or @SpringBootTest for integration-like tests
@ExtendWith(MockitoExtension.class)
public class CommentControllerTest {

    @InjectMocks
    private CommentController commentController;

    @Mock
    private CommentService commentService;

    @Mock
    private BlogPostService blogPostService; // Needed for getUsernameFromHeader indirection

    @Mock
    private BlogPostController blogPostController; // Mock for getUsernameFromHeader

    @Mock
    private HttpServletRequest request;

    private String mockUsername() {
        return "test_user";
    }

    @Test
    public void testSaveComment_success() throws Exception {
        Long blogPostId = 1L;
        Comment comment = new Comment();
        // Set comment properties (content, etc.)
        String expectedUsername = mockUsername();

        when(request.getHeader("Authorization")).thenReturn("valid-token");
        when(blogPostController.getUsernameFromHeader(request)).thenReturn(expectedUsername);
        when(commentService.createComment(comment, blogPostId, expectedUsername)).thenReturn(new CommentResponse()); // Mock response

        CommentResponse response = commentController.saveComment(comment, blogPostId, request);

        assertNotNull(response); // Assert comment response is not null
        // Additional assertions on response content (if applicable)
    }

    @Test
    public void testSaveComment_invalidToken() {
        Long blogPostId = 1L;
        Comment comment = new Comment();
        // Set comment properties

        when(request.getHeader("Authorization")).thenReturn("invalid-token");

        assertThrows(Exception.class, () -> commentController.saveComment(comment, blogPostId, request));
    }


    @Test
    public void testGetAllComments_paginated() {
        int page = 1;
        int size = 20;
        String sortBy = "createdAt";
        String sortOrder = "desc";

        PageRequest expectedPageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), sortBy));
        Page<Comment> mockPage = mock(Page.class); // Mock Page object

        when(commentService.getAllComments(expectedPageable)).thenReturn(mockPage);

        ResponseEntity<List<Comment>> response = commentController.getAllComments(page, size, sortBy, sortOrder);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetComment_success() throws Exception {
        Long id = 1L;
        Optional<Comment> expectedComment = Optional.of(new Comment());

        when(commentService.getCommentById(id)).thenReturn(expectedComment);

        Optional<Comment> response = commentController.getComment(id);

        assertTrue(response.isPresent());
        assertEquals(expectedComment.get(), response.get()); // Compare comment objects
    }

    @Test
    public void testGetComment_notFound() throws Exception {
        Long id = 1L;

        when(commentService.getCommentById(id)).thenReturn(Optional.empty());

        Optional<Comment> response = commentController.getComment(id);

        assertFalse(response.isPresent());
    }

    @Test
    public void testUpdateComment_success() throws Exception {
        Long id = 1L;
        Comment updatedComment = new Comment();
        // Set updated comment properties
        String expectedUsername = mockUsername();

        when(request.getHeader("Authorization")).thenReturn("valid-token");
        when(blogPostController.getUsernameFromHeader(request)).thenReturn(expectedUsername);
        when(commentService.updateComment(updatedComment, expectedUsername)).thenReturn(updatedComment); // Mock response

        Comment response = commentController.updateComment(id, updatedComment, request);

        assertEquals(updatedComment, response); // Compare comment objects
    }

    @Test
    public void testUpdateComment_invalidToken() throws Exception {
        Long id = 1L;
        Comment updatedComment = new Comment();
        // Set updated comment properties

        when(request.getHeader("Authorization")).thenReturn("invalid-token");

        // Use assertThrows for JUnit 5
        assertThrows(Exception.class, () -> commentController.updateComment(id, updatedComment, request));
    }

    @Test
    public void testDeleteComment_success() throws Exception {
        Long id = 1L;
        String expectedUsername = mockUsername();

        when(request.getHeader("Authorization")).thenReturn("valid-token");
        when(blogPostController.getUsernameFromHeader(request)).thenReturn(expectedUsername);
        doNothing().when(commentService).deleteCommentById(id, expectedUsername);

        ResponseEntity<Void> response = commentController.deleteComment(id, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteComment_invalidToken() throws Exception {
        Long id = 1L;

        when(request.getHeader("Authorization")).thenReturn("invalid-token");

        // Use assertThrows for JUnit 5
        assertThrows(Exception.class, () -> commentController.deleteComment(id, request));
    }

}


