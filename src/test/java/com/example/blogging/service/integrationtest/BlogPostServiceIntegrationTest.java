package com.example.blogging.service.integrationtest;

import com.example.blogging.dto.BlogPostResponse;
import com.example.blogging.entity.BlogPost;
import com.example.blogging.repository.BlogPostRepository;
import com.example.blogging.service.BlogPostServiceImpl;
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
public class BlogPostServiceIntegrationTest {

    @Mock
    private BlogPostRepository blogPostRepository;

    @InjectMocks
    private BlogPostServiceImpl blogPostService;

    @Test
    void testCreateBlogPost() {
        // Setup
        BlogPost blogPost = new BlogPost();
        blogPost.setId(1L);
        blogPost.setTitle("Test Title");
        blogPost.setContent("Test Content");

        String username = "testUser";

        // Mock repository behavior
        when(blogPostRepository.save(blogPost)).thenReturn(blogPost);

        // Test
        BlogPostResponse response = blogPostService.createBlogPost(blogPost, username);

        // Verify
        assertEquals(blogPost.getId(), response.getId());
        assertEquals(blogPost.getTitle(), response.getTitle());
        assertEquals(blogPost.getContent(), response.getContent());
    }

    // Add more tests for other methods similarly
}
