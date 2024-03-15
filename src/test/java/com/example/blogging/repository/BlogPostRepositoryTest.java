package com.example.blogging.repository;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import com.example.blogging.entity.BlogPost;
import com.example.blogging.service.BlogPostService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BlogPostRepositoryTest {

    @Mock
    private BlogPostRepository blogPostRepository;

    @InjectMocks
    private BlogPostService blogPostService; // Assuming you have a service to use the repository

    @Test
    public void testSearchByTitleOrContent() {
        // Mocking the behavior of the repository method
        String keyword = "test";
        BlogPost blogPost1 = new BlogPost();
        blogPost1.setId(1L);
        blogPost1.setTitle("Test Blog 1");
        blogPost1.setContent("This is a test blog post.");

        BlogPost blogPost2 = new BlogPost();
        blogPost2.setId(2L);
        blogPost2.setTitle("Blog about testing");
        blogPost2.setContent("Unit testing is important.");

        List<BlogPost> mockResult = Arrays.asList(blogPost1, blogPost2);
        when(blogPostRepository.searchByTitleOrContent(anyString())).thenReturn(mockResult);

        // Call the service method that uses the repository method
        List<BlogPost> result = blogPostService.searchBlogPostsByTitleOrContent(keyword);

        // Verify the result
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getTitle()).isEqualTo("Test Blog 1");
        assertThat(result.get(1).getTitle()).isEqualTo("Blog about testing");
    }
}
