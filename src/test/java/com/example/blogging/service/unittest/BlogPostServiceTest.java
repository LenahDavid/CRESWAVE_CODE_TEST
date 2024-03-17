package com.example.blogging.service.unittest;

import com.example.blogging.dto.BlogPostResponse;
import com.example.blogging.entity.BlogPost;
import com.example.blogging.entity.User;
import com.example.blogging.repository.BlogPostRepository;
import com.example.blogging.repository.UserRepository;
import com.example.blogging.service.BlogPostService;
import com.example.blogging.service.BlogPostServiceImpl;
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

class BlogPostServiceTest {

    @Mock
    private BlogPostRepository blogPostRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BlogPostService blogPostService = new BlogPostServiceImpl(); // Provide a mock implementation

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateBlogPost() {
        // Create a mock User object with a valid username
        User user = new User();
        user.setUsername("testUser");

        BlogPost blogPost = new BlogPost();
        blogPost.setId(1L);
        blogPost.setTitle("Test Blog Post");

        String username = "testUser";

        // Mock behavior
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(blogPostRepository.save(blogPost)).thenReturn(blogPost);

        // Test the method
        BlogPostResponse response = blogPostService.createBlogPost(blogPost, username);

        assertNotNull(response);
        assertEquals(blogPost.getId(), response.getId());
        assertEquals("Test Blog Post", response.getTitle());
        assertEquals(username, response.getAuthor()); // Assert author matches the provided username
    }


    @Test
    void testGetBlogPostById() {
        // Create a mock User object with a valid username
        User user = new User();
        user.setUsername("testUser");

        // Create a BlogPost object with a non-null user field
        BlogPost blogPost = new BlogPost();
        blogPost.setId(1L);
        blogPost.setUser(user); // Set the user

        // Mock behavior of the repository
        when(blogPostRepository.findById(1L)).thenReturn(Optional.of(blogPost));

        // Test the method
        Optional<BlogPostResponse> result = blogPostService.getBlogPostById(1L);

        assertTrue(result.isPresent());
        assertEquals(blogPost.getId(), result.get().getId());
    }


    @Test
    void testDeleteBlogPostById() {
        // Create a mock User object with a valid username
        User user = new User();
        user.setUsername("testUser");

        // **Create and initialize a BlogPost object**
        BlogPost blogPost = new BlogPost();
        blogPost.setId(1L);
        blogPost.setUser(user);

        String username = "testUser";

        // Mock behavior to consistently find the BlogPost by ID
        when(blogPostRepository.existsById(1L)).thenReturn(true);
        when(blogPostRepository.findById(1L)).thenReturn(Optional.of(blogPost)); // Now blogPost is initialized

        // Test the method
        assertDoesNotThrow(() -> blogPostService.deleteBlogPostById(1L, username));

        // Verify deleteById is called with the correct ID
        verify(blogPostRepository).deleteById(1L);
    }





    @Test
    void testUpdateBlogPost() {
        BlogPost blogPost = new BlogPost();
        blogPost.setId(1L);
        blogPost.setTitle("Initial Title");

        User user = new User();
        user.setUsername("testUser"); // Set a valid username
        blogPost.setUser(user); // Associate the user with the blog post

        String username = "testUser";

        // Mock behavior
        when(blogPostRepository.findById(1L)).thenReturn(Optional.of(blogPost));
        when(blogPostRepository.save(blogPost)).thenReturn(blogPost);

        // Test the method
        BlogPostResponse response = blogPostService.updateBlogPost(blogPost, username);

        assertNotNull(response);
        assertEquals("Initial Title", response.getTitle());
    }


    @Test
    void testGetAllBlogPosts() {
        List<BlogPost> blogPosts = new ArrayList<>();
        blogPosts.add(new BlogPost());
        blogPosts.add(new BlogPost());
        Page<BlogPost> blogPostPage = new PageImpl<>(blogPosts);

        // Mock behavior
        when(blogPostRepository.findAll(any(PageRequest.class))).thenReturn(blogPostPage);

        // Test the method
        Page<BlogPost> result = blogPostService.getAllBlogPosts(PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(blogPostPage, result);
    }

}

