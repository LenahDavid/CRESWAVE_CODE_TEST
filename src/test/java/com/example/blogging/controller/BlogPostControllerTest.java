package com.example.blogging.controller;

import com.example.blogging.controllers.BlogPostController;
import com.example.blogging.dto.BlogPostResponse;
import com.example.blogging.entity.BlogPost;
import com.example.blogging.service.BlogPostService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class BlogPostControllerTest {

    @Mock
    BlogPostService blogPostService;

    @InjectMocks
    BlogPostController blogPostController;

    @Mock
    HttpServletRequest httpServletRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveBlog() {
        BlogPost blogPost = new BlogPost();
        BlogPostResponse expectedResponse = new BlogPostResponse();

        when(blogPostService.createBlogPost(blogPost, "username")).thenReturn(expectedResponse);

        BlogPostResponse response = blogPostController.saveBlog(blogPost, httpServletRequest);

        assertEquals(expectedResponse, response);
        verify(blogPostService, times(1)).createBlogPost(blogPost, "username");
    }

    @Test
    void testSearchBlogPosts() {
        String keyword = "keyword";
        List<BlogPostResponse> expectedResponse = new ArrayList<>();

        when(blogPostService.searchBlogPosts(keyword)).thenReturn(expectedResponse);

        ResponseEntity<List<BlogPostResponse>> responseEntity = blogPostController.searchBlogPosts(keyword);

        assertEquals(expectedResponse, responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(blogPostService, times(1)).searchBlogPosts(keyword);
    }

    @Test
    public void testGetAllBlogPosts() {
        int page = 0;
        int size = 10;
        String sortBy = "createdAt";
        String sortOrder = "desc";

        List<BlogPost> expectedBlogPosts = new ArrayList<>();
        PageImpl<BlogPost> pageImpl = new PageImpl<>(expectedBlogPosts);

        // Mock to return a non-null PageImpl with empty list (optional)
        when(blogPostService.getAllBlogPosts(PageRequest.of(page, size))).thenReturn(pageImpl);

        // Adjusting the controller method call
        ResponseEntity<List<BlogPost>> responseEntity = blogPostController.getAllBlogPosts(page, size, sortBy, sortOrder);

        // Verify the behavior and assertions
        assertNotNull(responseEntity.getBody(), "Response body should not be null"); // Check for null before assertions
        assertEquals(expectedBlogPosts, responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(blogPostService, times(1)).getAllBlogPosts(PageRequest.of(page, size));
    }

    @Test
    void testGetBlogPost() {
        Long id = 1L;
        Optional<BlogPostResponse> expectedResponse = Optional.of(new BlogPostResponse());

        when(blogPostService.getBlogPostById(id)).thenReturn(expectedResponse);

        Optional<BlogPostResponse> response = blogPostController.getBlogPost(id);

        assertEquals(expectedResponse, response);
        verify(blogPostService, times(1)).getBlogPostById(id);
    }

    @Test
    void testUpdateBlogPost() {
        Long id = 1L;
        BlogPost updatedBlogPost = new BlogPost();
        BlogPostResponse expectedResponse = new BlogPostResponse();

        when(blogPostService.updateBlogPost(updatedBlogPost, "username")).thenReturn(expectedResponse);

        BlogPostResponse response = blogPostController.updateBlogPost(id, updatedBlogPost, httpServletRequest);

        assertEquals(expectedResponse, response);
        verify(blogPostService, times(1)).updateBlogPost(updatedBlogPost, "username");
    }

    @Test
    void testDeleteBlogPost() {
        Long id = 1L;

        ResponseEntity<Void> responseEntity = blogPostController.deleteBlogPost(id, httpServletRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(blogPostService, times(1)).deleteBlogPostById(id, "username");
    }
}
