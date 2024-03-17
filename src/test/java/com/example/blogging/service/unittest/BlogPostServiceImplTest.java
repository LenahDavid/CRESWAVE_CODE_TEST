package com.example.blogging.service.unittest;

import com.example.blogging.dto.BlogPostResponse;
import com.example.blogging.entity.BlogPost;
import com.example.blogging.entity.Role;
import com.example.blogging.entity.User;
import com.example.blogging.repository.BlogPostRepository;
import com.example.blogging.repository.UserRepository;
import com.example.blogging.service.BlogPostServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BlogPostServiceImplTest {

    @Mock
    private BlogPostRepository blogPostRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BlogPostServiceImpl blogPostService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateBlogPost() {
        BlogPost blogPost = new BlogPost();
        blogPost.setId(1L);
        blogPost.setTitle("Test Title");
        blogPost.setContent("Test Content");

        User user = new User();
        user.setUsername("testUser");

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(blogPostRepository.save(blogPost)).thenReturn(blogPost);

        BlogPostResponse response = blogPostService.createBlogPost(blogPost, "testUser");

        assertNotNull(response);
        assertEquals(blogPost.getId(), response.getId());
        assertEquals("Test Title", response.getTitle());
        assertEquals("Test Content", response.getContent());
        assertEquals("testUser", response.getAuthor());
    }

    @Test
    void testGetAllBlogPosts() {
        List<BlogPost> blogPosts = new ArrayList<>();
        blogPosts.add(new BlogPost());
        blogPosts.add(new BlogPost());

        when(blogPostRepository.findAll()).thenReturn(blogPosts);

        List<BlogPost> result = blogPostService.getAllBlogPosts();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testGetBlogPostById() {
        // Create a mock User object
        User user = new User();
        user.setUsername("testUser");

        // Create a BlogPost object with the mock User
        BlogPost blogPost = new BlogPost();
        blogPost.setId(1L);
        blogPost.setTitle("Test Title");
        blogPost.setContent("Test Content");
        blogPost.setUser(user); // Set the user here

        // Mock behavior to return the blog post
        when(blogPostRepository.findById(1L)).thenReturn(Optional.of(blogPost));

        // Call the service method
        Optional<BlogPostResponse> response = blogPostService.getBlogPostById(1L);

        // Assert that the response is present
        assertTrue(response.isPresent());

        // Assert the fields of the BlogPostResponse
        assertEquals(blogPost.getId(), response.get().getId());
        assertEquals(blogPost.getTitle(), response.get().getTitle());
        assertEquals(blogPost.getContent(), response.get().getContent());
    }


    @Test
    void testDeleteBlogPostById() {
        // Create a user with sufficient permissions
        User user = new User();
        user.setUsername("testUser");
        user.setRole(Role.ADMIN);

        // Create a blog post
        BlogPost blogPost = new BlogPost();
        blogPost.setId(1L);
        blogPost.setTitle("Test Title");
        blogPost.setContent("Test Content");
        blogPost.setUser(user); // Set the user

        // Mock the behavior of repositories
        when(blogPostRepository.findById(1L)).thenReturn(Optional.of(blogPost));
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        // Call the service method
        blogPostService.deleteBlogPostById(1L, "testUser");

        // Verify that the deleteById method is called once
        verify(blogPostRepository, times(1)).deleteById(1L);
    }

    @Test
    void testUpdateBlogPost() {
        // Mock a user
        User user = new User();
        user.setUsername("testUser");

        // Create a blog post
        BlogPost blogPost = new BlogPost();
        blogPost.setId(1L);
        blogPost.setTitle("Test Title");
        blogPost.setContent("Test Content");
        blogPost.setUser(user); // Set the user

        // Mock the behavior of repositories
        when(blogPostRepository.findById(1L)).thenReturn(Optional.of(blogPost));
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(blogPostRepository.save(any(BlogPost.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Return the saved blog post

        // Call the service method
        BlogPostResponse response = blogPostService.updateBlogPost(blogPost, "testUser");

        // Assertions
        assertNotNull(response);
        assertEquals(blogPost.getId(), response.getId());
        assertEquals("Test Title", response.getTitle());
        assertEquals("Test Content", response.getContent());
    }


}
