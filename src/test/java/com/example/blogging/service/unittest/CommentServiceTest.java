package com.example.blogging.service.unittest;

import com.example.blogging.dto.CommentResponse;
import com.example.blogging.entity.Comment;
import com.example.blogging.entity.User;
import com.example.blogging.repository.CommentRepository;
import com.example.blogging.repository.UserRepository;
import com.example.blogging.service.CommentServiceImpl;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository; // Mock the UserRepository

    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    void testCreateComment() {
        // Create a mock Comment object
        Comment comment = new Comment();

        // Set up other necessary variables
        Long postId = 1L;
        String username = "testUser";

        // **Set necessary fields for Comment object**
        comment.setUser(String.valueOf(new User())); // Assuming Comment has a User field


        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setUser("testUser");
        commentResponse.setId(1L);


        // Mock behavior of commentRepository
        when(commentRepository.save(comment)).thenReturn(comment);

        // Mock behavior of userRepository
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(new User()));

        // Test the method
        CommentResponse response = commentService.createComment(commentResponse, postId, username);

        // Assert the result
        assertEquals(comment.getId(), response.getId()); // Assuming CommentResponse includes id field
        // Add more assertions as needed (e.g., for content, postId, etc.)
        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    void testGetCommentById() {
        Long id = 1L;
        Comment comment = new Comment();
        comment.setId(id);

        when(commentRepository.findById(id)).thenReturn(Optional.of(comment));

        Optional<CommentResponse> result = commentService.getCommentById(id);

        assertEquals(Optional.of(comment), result);
        verify(commentRepository, times(1)).findById(id);
    }

    @Test
    void testDeleteCommentById() {
        Long id = 1L;
        String username = "testUser";

        // **Create a mock Comment object with ID**
        Comment comment = new Comment();
        comment.setId(id);

        // Set up mock repository behavior to find the comment
        when(commentRepository.existsById(id)).thenReturn(true);  // Ensure it exists
        when(commentRepository.findById(id)).thenReturn(Optional.of(comment)); // Assuming comment is used later

        commentService.deleteCommentById(id, username);

        verify(commentRepository, times(1)).deleteById(id);
    }

    @Test
    void testUpdateComment() {
        // Set a valid ID for the Comment
        Comment comment = new Comment();
        comment.setId(1L);

        // **Set a mock User for the comment**
        User mockUser = new User();
        mockUser.setUsername("testUser");
        comment.setUser(String.valueOf(mockUser));  // Associate the comment with the mock User

        // Mock behavior of retrieving the comment from repository
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        String username = "testUser";

        // Test the method
        CommentResponse updatedCommentResponse = commentService.updateComment(1L, comment, "username");

        // Assert
        assertTrue(updatedCommentResponse != null);
        // ... assertions for interactions with repositories
    }


    @Test
    void testGetAllComments() {
        int page = 1;
        int size = 10;
        PageRequest pageable = PageRequest.of(page, size);
        List<Comment> comments = new ArrayList<>(); // Assuming you have some test data
        Page<Comment> expectedPage = new PageImpl<>(comments);

        when(commentRepository.findAll(pageable)).thenReturn(expectedPage);

        Page<CommentResponse> result = commentService.getAllComments(pageable);

        assertEquals(expectedPage, result);
        verify(commentRepository, times(1)).findAll(pageable);
    }
}
