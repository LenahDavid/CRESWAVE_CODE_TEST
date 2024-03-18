package com.example.blogging.service.integrationtest;

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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceImplIntegrationTest {

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
        // Initialize any necessary setup before each test method
    }

    @Test
    void testCreateComment() {
        // Given
        User user = new User();
        user.setUsername("username");
        user.setRole(Role.USER);

        BlogPost post = new BlogPost();
        post.setId(1L);
        post.setTitle("Test Blog Post");

        Comment comment = new Comment();
        comment.setContent("Test comment content");
        comment.setUser(user.getUsername());
        comment.setBlogPost(post);

        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
        when(blogPostRepository.getReferenceById(any())).thenReturn(post);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        // When
        CommentResponse response = commentService.createComment(comment, 1L, "username");

        // Then
        assertNotNull(response);
        assertEquals(comment.getContent(), response.getContent());
        assertEquals(post.getTitle(), response.getBlogTitle());
        assertEquals(user.getUsername(), response.getUser());
    }

    @Test
    void testGetCommentById() {
        // Given
        Comment comment = new Comment();
        comment.setId(1L);

        when(commentRepository.findById(any())).thenReturn(Optional.of(comment));

        // When
        Optional<CommentResponse> result = commentService.getCommentById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(comment, result.get());
    }

    // Add tests for other methods (deleteCommentById, updateComment, getAllComments) similarly
}
