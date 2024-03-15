package com.example.blogging.controllers;


import com.example.blogging.entity.BlogPost;
import com.example.blogging.service.BlogPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController

public class BlogPostController {
    @Autowired
    BlogPostService blogPostService;

    @PostMapping("/blog")
    public BlogPost saveBlog(@RequestBody BlogPost blogPost) {
        return blogPostService.createBlogPost(blogPost);
    }

    @GetMapping("/search")
    public ResponseEntity<List<BlogPost>> searchBlogPosts(@RequestParam String keyword) {
        List<BlogPost> searchResults = blogPostService.searchBlogPosts(keyword);
        return ResponseEntity.ok(searchResults);
    }
    @GetMapping("/blog")
    public ResponseEntity<List<BlogPost>> getAllBlogPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder
    ) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), sortBy));
        Page<BlogPost> blogPostPage = blogPostService.getAllBlogPosts(pageable);
        return ResponseEntity.ok(blogPostPage.getContent());
    }
    @GetMapping("/blog/{id}")
    public Optional<BlogPost> getBlogPost(@PathVariable Long id) {
      return blogPostService.getBlogPostById(id);
    }

    @PutMapping("/blog/{id}")
    public BlogPost updateBlogPost(@PathVariable Long id, @RequestBody BlogPost updatedBlogPost) {
      return  blogPostService.updateBlogPost(updatedBlogPost);
    }

    @DeleteMapping("/blog/{id}")
    public ResponseEntity<Void> deleteBlogPost(@PathVariable Long id) {
        blogPostService.deleteBlogPostById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
//    @GetMapping("/blog")
//    public ResponseEntity<List<BlogPost>> getAllBlogPosts() {
//        return blogPostService.findAllB();
//    }
}
