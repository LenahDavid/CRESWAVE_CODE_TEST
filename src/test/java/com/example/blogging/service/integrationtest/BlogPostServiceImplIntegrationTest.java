package com.example.blogging.service.integrationtest;

import com.example.blogging.dto.BlogPostResponse;
import com.example.blogging.entity.BlogPost;
import com.example.blogging.entity.Role;
import com.example.blogging.entity.User;
import com.example.blogging.repository.BlogPostRepository;
import com.example.blogging.repository.UserRepository;
import com.example.blogging.service.BlogPostServiceImpl;
import com.example.blogging.service.JWTService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BlogPostServiceImplIntegrationTest {

    @Mock
    private BlogPostRepository blogPostRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JWTService jwtService;

    @InjectMocks
    private BlogPostServiceImpl blogPostService;

    @BeforeEach
    void setUp() {
        // Mocking repository behavior
        BlogPost blogPost = new BlogPost();
        blogPost.setId(1L);
        blogPost.setTitle("Test Title");
        blogPost.setContent("Test Content");

        when(blogPostRepository.save(any(BlogPost.class))).thenReturn(blogPost);

        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        user.setRole(Role.USER);
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
    }

    @Test
    void testCreateBlogPost() {
        // Setup
        BlogPost blogPost = new BlogPost();
        blogPost.setTitle("Test Title");
        blogPost.setContent("Test Content");

        String username = "testUser";

        // Test
        BlogPostResponse response = blogPostService.createBlogPost(blogPost, username);

        // Verify
        assertEquals("testUser", response.getAuthor());
        assertEquals("Test Title", response.getTitle());
        assertEquals("Test Content", response.getContent());
    }

    // Add more integration tests for other methods similarly
}

