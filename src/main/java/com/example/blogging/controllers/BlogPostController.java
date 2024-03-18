package com.example.blogging.controllers;


import com.example.blogging.dto.BlogPostResponse;
import com.example.blogging.entity.BlogPost;
import com.example.blogging.service.BlogPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
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
@ApiResponses({@ApiResponse(responseCode = "200", description = "Success"),
    @ApiResponse(responseCode = "400", description = "Bad Request"),
    @ApiResponse(responseCode = "403", description = "Unauthorized"),
    @ApiResponse(responseCode = "404", description = "Not Found"),
    @ApiResponse(responseCode = "500", description = "Internal Server Error"),
        @ApiResponse(responseCode = "201", description = "Created")
})
public class BlogPostController {

    @Autowired
    BlogPostService blogPostService;
    @Operation(
            description = "Post all blog posts",
            summary = "Posting of blog posts"

    )
//    Posting a blog
    @PostMapping("/blog")
    public BlogPostResponse saveBlog(@RequestBody BlogPost blogPost, HttpServletRequest request) {
        String username = getUsernameFromHeader(request);
        return blogPostService.createBlogPost(blogPost, username);
    }

    public String getUsernameFromHeader(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String username = blogPostService.extractUsernameFromToken(token);
        return username;
    }
    @Operation(
            description = "Searching all blog posts",
            summary = "Searching of blog posts by title or content"

    )
//    searching of blog by either content or title
    @GetMapping("/search")
    public ResponseEntity<List<BlogPostResponse>> searchBlogPosts(@RequestParam String keyword) {
        List<BlogPostResponse> searchResults = blogPostService.searchBlogPosts(keyword);
        return ResponseEntity.ok(searchResults);
    }
    @Operation(
            description = "Get all blog posts",
            summary = "Getting of all blog posts"

    )
//    Pagination of blogs
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
    @Operation(
            description = "Getting all blog posts",
            summary = "Getting all blog posts"

    )
//    Getting of all blogs
    @GetMapping("/posts")
    public ResponseEntity<List<BlogPostResponse>> findallBlogs() {
        return ResponseEntity.ok(blogPostService.getAllBlogs());
    }
    @Operation(
            description = "Getting single blog posts",
            summary = "Getting single blog post by Id"

    )
//    getting blog by id
    @GetMapping("/blog/{id}")
    public ResponseEntity<Optional<BlogPostResponse>> getBlogPost(@PathVariable Long id) {
        return new ResponseEntity<>(blogPostService.getBlogPostById(id), HttpStatus.OK);
    }
    @Operation(
            description = "Updating of blog posts",
            summary = "Updating of blog posts by id"

    )
//    Editing of blog
    @PutMapping("/blog/{id}")
    public BlogPostResponse updateBlogPost(@PathVariable("id") Long id, @RequestBody BlogPost updatedBlogPost, HttpServletRequest request) {
        String username = getUsernameFromHeader(request);
        return blogPostService.updateBlogPost(updatedBlogPost, username);
    }
    @Operation(
            description = "Deleting of blog posts",
            summary = "Deleting of blog posts by id"

    )
//    Deleting of blog
    @DeleteMapping("/blog/{id}")
    public ResponseEntity<Void> deleteBlogPost(@PathVariable Long id, HttpServletRequest request) {
        String username = getUsernameFromHeader(request);
        blogPostService.deleteBlogPostById(id, username);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}