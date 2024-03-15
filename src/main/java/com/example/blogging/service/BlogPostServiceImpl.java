package com.example.blogging.service;

import com.example.blogging.entity.BlogPost;
import com.example.blogging.repository.BlogPostRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BlogPostServiceImpl implements BlogPostService {
    @Autowired
    private BlogPostRepository blogPostRepository;

    @Override
    public BlogPost createBlogPost(BlogPost blogPost) {
        return blogPostRepository.save(blogPost);
    }
    public List<BlogPost> getAllBlogPosts() {
        return blogPostRepository.findAll();
    }

    @Override
    public Optional<BlogPost> getBlogPostById(Long id) {
        return blogPostRepository.findById(id);
    }

    @Override
    public void deleteBlogPostById(Long id) {
        BlogPost existingBlogPost = blogPostRepository.findById(id).orElse(null);
        if (existingBlogPost != null) {
            blogPostRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Blog post with id " + id + " not found");
        }
    }

    @Override
    public BlogPost updateBlogPost(BlogPost updatedBlogPost) {
        Long id = updatedBlogPost.getId(); // Assuming id is a field in BlogPost
        BlogPost existingBlogPost = blogPostRepository.findById(id).orElse(null);
        if (existingBlogPost != null) {
            // Update the existing blog post with the data from updatedBlogPost
            existingBlogPost.setTitle(updatedBlogPost.getTitle());
            existingBlogPost.setContent(updatedBlogPost.getContent());
            // Update other fields as needed

            // Save the updated blog post
            return blogPostRepository.save(existingBlogPost);
        } else {
            throw new EntityNotFoundException("Blog post with id " + id + " not found");
        }
    }

    @Override
    public Page<BlogPost> getAllBlogPosts(PageRequest pageable) {
        return blogPostRepository.findAll(pageable);
    }
    @Override
    public List<BlogPost> searchBlogPosts(String keyword) {
        return blogPostRepository.searchByTitleOrContent(keyword);
    }

}


