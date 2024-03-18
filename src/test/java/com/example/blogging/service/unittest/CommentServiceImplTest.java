package com.example.blogging.service.unittest;

import com.example.blogging.dto.CommentResponse;
import com.example.blogging.entity.BlogPost;
import com.example.blogging.entity.Comment;
import com.example.blogging.entity.Role;
import com.example.blogging.entity.User;
import com.example.blogging.repository.BlogPostRepository;
import com.example.blogging.repository.CommentRepository;
import com.example.blogging.repository.UserRepository;
import com.example.blogging.service.CommentServiceImpl;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private BlogPostRepository blogPostRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateComment() {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setContent("Test Comment");

        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setId(1L);
        commentResponse.setContent("Test Comment");


        User user = new User();
        user.setUsername("testUser");

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(blogPostRepository.getReferenceById(1L)).thenReturn(new BlogPost());
        when(commentRepository.save(comment)).thenReturn(comment);

        CommentResponse response = commentService.createComment(commentResponse, 1L, "testUser");

        assertNotNull(response);
        assertEquals(comment.getId(), response.getId());
        assertEquals("Test Comment", response.getContent());
        assertEquals("testUser", response.getUser());
    }

    @Test
    void testGetCommentById() {
        Comment comment = new Comment();
        comment.setId(1L);

        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        Optional<CommentResponse> result = commentService.getCommentById(1L);

        assertTrue(result.isPresent());
        assertEquals(comment, result.get());
    }

    @Test
    void testDeleteCommentById_AdminRole() {
        User user = new User();
        user.setRole(Role.ADMIN);

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setUser(String.valueOf(user));

        when(commentRepository.existsById(1L)).thenReturn(true);
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> commentService.deleteCommentById(1L, "testUser"));

        verify(commentRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteCommentById_UserRole_Owner() {
        User user = new User();
        user.setRole(Role.USER);
        user.setUsername("testUser");

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setUser(String.valueOf(user));

        when(commentRepository.existsById(1L)).thenReturn(true);
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> commentService.deleteCommentById(1L, "testUser"));

        verify(commentRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteCommentById_UserRole_NotOwner() {
        User user = new User();
        user.setRole(Role.USER);
        user.setUsername("testUser");

        // Create a mock User object with a valid username for the comment
        User commentOwner = new User();
        commentOwner.setUsername("commentOwner");

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setUser(String.valueOf(commentOwner)); // Set the comment's user

        when(commentRepository.existsById(1L)).thenReturn(true);
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () -> commentService.deleteCommentById(1L, "testUser"));

        verify(commentRepository, never()).deleteById(anyLong());
    }

    @Test
    void testUpdateComment() {
        Comment updatedComment = new Comment();
        updatedComment.setId(1L);
        updatedComment.setContent("Updated Comment");

        User user = new User();
        user.setUsername("testUser");

        Comment existingComment = new Comment();
        existingComment.setId(1L);
        existingComment.setContent("Initial Comment");
        existingComment.setUser(String.valueOf(user));

        when(commentRepository.findById(1L)).thenReturn(Optional.of(existingComment));
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(commentRepository.save(existingComment)).thenReturn(updatedComment);

        assertDoesNotThrow(() -> commentService.updateComment(updatedComment.getId(), updatedComment, "testUser"));

        assertEquals(updatedComment.getContent(), existingComment.getContent());
    }

    @Test
    void testGetAllComments() {
        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment());
        comments.add(new Comment());
        Page<Comment> commentPage = new PageImpl<>(comments);

        when(commentRepository.findAll(any(PageRequest.class))).thenReturn(commentPage);

        Page<Comment> result = commentService.getAllComments(PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(commentPage, result);
    }
}
