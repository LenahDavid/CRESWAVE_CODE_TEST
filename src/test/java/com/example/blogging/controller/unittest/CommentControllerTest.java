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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentControllerTest {

    @Mock
    private CommentService commentService;

    @Mock
    private BlogPostController blogPostController;

    @InjectMocks
    private CommentController commentController;

    @Mock
    private HttpServletRequest request;

    @Test
    public void testSaveComment_Success() {
        // Arrange
        Long blogPostId = 1L;
        Comment comment = new Comment();
        CommentResponse commentResponse = new CommentResponse();
        String expectedUsername = "test_user";

        when(request.getHeader("Authorization")).thenReturn("valid-token");
        when(blogPostController.getUsernameFromHeader(request)).thenReturn(expectedUsername);
        when(commentService.createComment(commentResponse, blogPostId, expectedUsername)).thenReturn(new CommentResponse());

        // Act
        CommentResponse response = commentController.saveComment(commentResponse, blogPostId, request);

        // Assert
        assertNotNull(response);
    }

    @Test
    public void testGetAllComments_Paginated() {
        // Arrange
        int page = 0;
        int size = 20;
        String sortBy = "id";
        String sortOrder = "asc";
        Page<Comment> mockPage = mock(Page.class);
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), sortBy));
        when(commentService.getAllComments(pageRequest)).thenReturn((Page) mockPage);

        // Act
        ResponseEntity<Page<CommentResponse>> response = commentController.getAllComments(page, size, sortBy, sortOrder);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetComment_Success() {
        // Arrange
        Long id = 1L;
        Optional<CommentResponse> expectedComment = Optional.of(new CommentResponse());

        when(commentService.getCommentById(id)).thenReturn(expectedComment);

        // Act
        Optional<CommentResponse> response = commentController.getComment(id).getBody();

        // Assert
        assertTrue(response.isPresent());
        assertEquals(expectedComment.get(), response.get());
    }


    @Test
    public void testGetComment_NotFound() {
        // Arrange
        Long id = 1L;

        when(commentService.getCommentById(id)).thenReturn(Optional.empty());

        // Act
        Optional<CommentResponse> response = commentController.getComment(id).getBody();

        // Assert
        assertFalse(response.isPresent());
    }

    // Similar tests for other controller methods

}
