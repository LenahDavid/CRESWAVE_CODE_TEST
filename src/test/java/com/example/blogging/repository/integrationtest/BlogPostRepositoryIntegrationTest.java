package com.example.blogging.repository.integrationtest;


import com.example.blogging.entity.BlogPost;
import com.example.blogging.repository.BlogPostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class BlogPostRepositoryIntegrationTest {

    @Autowired
    private BlogPostRepository blogPostRepository;

    @Test
    public void testSearchByTitleOrContent() {
        // Create and save blog posts
        BlogPost blogPost1 = new BlogPost();
        blogPost1.setTitle("Test Title 1");
        blogPost1.setContent("Test Content 1");
        blogPostRepository.save(blogPost1);

        BlogPost blogPost2 = new BlogPost();
        blogPost2.setTitle("Title 2");
        blogPost2.setContent("Content for testing");
        blogPostRepository.save(blogPost2);

        // Search for blog posts with keyword "Test"
        List<BlogPost> foundBlogPosts = blogPostRepository.searchByTitleOrContent("Test");

        // Check if the correct blog posts are found
        assertEquals(1, foundBlogPosts.size());
        assertTrue(foundBlogPosts.contains(blogPost1));
        assertFalse(foundBlogPosts.contains(blogPost2));
    }
}

