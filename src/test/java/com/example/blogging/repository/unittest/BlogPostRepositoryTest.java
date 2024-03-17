package com.example.blogging.repository.unittest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import com.example.blogging.entity.BlogPost;
import com.example.blogging.repository.BlogPostRepository;
import com.example.blogging.service.BlogPostService;
import com.example.blogging.service.BlogPostServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BlogPostRepositoryTest {

    @Mock
    private BlogPostService mockBlogPostService;
    @Mock
    private BlogPostRepository blogPostRepository;
    @InjectMocks
    private BlogPostServiceImpl blogPostServiceImpl;

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
        when(blogPostRepository.searchByTitleOrContent(keyword)).thenReturn(mockResult);

        // Call the service method
        List<BlogPost> result = blogPostServiceImpl.searchBlogPostsByTitleOrContent(keyword);

        // Verify the result
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getTitle()).isEqualTo("Test Blog 1");
        assertThat(result.get(1).getTitle()).isEqualTo("Blog about testing");
    }

}
