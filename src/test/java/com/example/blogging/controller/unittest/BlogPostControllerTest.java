
package com.example.blogging.controller.unittest;

import com.example.blogging.controllers.BlogPostController;
import com.example.blogging.dto.BlogPostResponse;
import com.example.blogging.dto.CommentResponse;
import com.example.blogging.entity.BlogPost;
import com.example.blogging.entity.Comment;
import com.example.blogging.service.BlogPostService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
        String sortBy = "id";
        String sortOrder = "asc";
        Page<BlogPost> mockPage = mock(Page.class);
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), sortBy));;

        // Mock to return a non-null PageImpl with empty list (optional)
        when(blogPostService.getAllBlogPosts(PageRequest.of(page, size))).thenReturn((Page)mockPage);;

        // Adjusting the controller method call
        ResponseEntity<Page<BlogPostResponse>> response = blogPostController.getAllBlogPosts(page, size, sortBy, sortOrder);
        // Verify the behavior and assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }


    @Test
    void testGetBlogPost() {
        Long id = 1L;
        Optional<BlogPostResponse> expectedResponse = Optional.of(new BlogPostResponse());

        when(blogPostService.getBlogPostById(id)).thenReturn(expectedResponse);

        Optional<BlogPostResponse> response = blogPostController.getBlogPost(id).getBody();

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
