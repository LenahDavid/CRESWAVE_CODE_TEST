package com.example.blogging.service;

import com.example.blogging.dto.CommentResponse;
import com.example.blogging.entity.Comment;
import com.example.blogging.repository.CommentRepository;
import com.example.blogging.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository; // Assuming CommentRepository is used in CommentService implementation

    @InjectMocks
    private CommentServiceImpl commentService; // Assuming CommentServiceImpl implements CommentService

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateComment() {
        Comment comment = new Comment();
        Long postId = 1L;
        String username = "testUser";

        when(commentRepository.save(comment)).thenReturn(comment);

        CommentResponse response = commentService.createComment(comment, postId, username);

        assertEquals(comment.getId(), response.getId()); // Assuming CommentResponse includes id field
        // Add more assertions as needed
        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    void testGetCommentById() {
        Long id = 1L;
        Comment comment = new Comment();
        comment.setId(id);

        when(commentRepository.findById(id)).thenReturn(Optional.of(comment));

        Optional<Comment> result = commentService.getCommentById(id);

        assertEquals(Optional.of(comment), result);
        verify(commentRepository, times(1)).findById(id);
    }

    @Test
    void testDeleteCommentById() {
        Long id = 1L;
        String username = "testUser";

        // Create a mock Comment object with ID 1
        Comment comment = new Comment();
        comment.setId(id);

        // Set up mock repository behavior to find the comment
        when(commentRepository.existsById(id)).thenReturn(true);  // Ensure it exists
        when(commentRepository.findById(id)).thenReturn(Optional.of(comment));

        commentService.deleteCommentById(id, username);

        verify(commentRepository, times(1)).deleteById(id);
    }


    @Test
    void testUpdateComment() {
        // Set a valid ID for the Comment
        Comment comment = new Comment();
        comment.setId(1L);
        String username = "testUser";

        when(commentRepository.save(comment)).thenReturn(comment);

        Comment updatedComment = commentService.updateComment(comment, username);

        assertEquals(comment, updatedComment);
        verify(commentRepository, times(1)).save(comment);
    }


    @Test
    void testGetAllComments() {
        int page = 0;
        int size = 10;
        PageRequest pageable = PageRequest.of(page, size);
        List<Comment> comments = new ArrayList<>(); // Assuming you have some test data
        Page<Comment> expectedPage = new PageImpl<>(comments);

        when(commentRepository.findAll(pageable)).thenReturn(expectedPage);

        Page<Comment> result = commentService.getAllComments(pageable);

        assertEquals(expectedPage, result);
        verify(commentRepository, times(1)).findAll(pageable);
    }
}
