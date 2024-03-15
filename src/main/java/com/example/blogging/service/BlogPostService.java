package com.example.blogging.service;


import com.example.blogging.entity.BlogPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface BlogPostService {
    public BlogPost createBlogPost(BlogPost blogPost);
    public Optional<BlogPost> getBlogPostById(Long id);

    public void deleteBlogPostById(Long id);
    public BlogPost updateBlogPost(BlogPost blogPost);


    Page<BlogPost> getAllBlogPosts(PageRequest pageable);

    List<BlogPost> searchBlogPosts(String keyword);
}
