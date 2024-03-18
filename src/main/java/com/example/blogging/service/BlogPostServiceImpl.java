package com.example.blogging.service;

import com.example.blogging.dto.BlogPostResponse;
import com.example.blogging.entity.BlogPost;
import com.example.blogging.entity.Role;
import com.example.blogging.entity.User;
import com.example.blogging.exceptions.BlogPostNotFoundException;
import com.example.blogging.exceptions.UserUnAuthorizedException;
import com.example.blogging.repository.BlogPostRepository;
import com.example.blogging.repository.UserRepository;
import com.example.blogging.util.BlogPostMapper;
import jakarta.persistence.EntityNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BlogPostServiceImpl implements BlogPostService {
    @Autowired
    private BlogPostRepository blogPostRepository;

    @Autowired
    JWTService jwtService;

    @Autowired
    UserRepository userRepository;

    @Override
    public BlogPostResponse createBlogPost(BlogPost blogPost, String username) {

        BlogPostResponse blogPostResponse = new BlogPostResponse();

        User user = userRepository.findByUsername(username).get();

        blogPost.setUser(username);
        blogPost.setComments(new ArrayList<>());



        blogPostResponse.setAuthor(username);
        blogPostResponse.setComments(new ArrayList<>());
        blogPostResponse.setContent(blogPost.getContent());
        blogPostResponse.setTitle(blogPost.getTitle());


        blogPostRepository.save(blogPost);
        blogPostResponse.setId(blogPost.getId());
        return blogPostResponse;
    }

    public List<BlogPost> getAllBlogPosts() {
        return blogPostRepository.findAll();
    }

    @Override
    public Optional<BlogPostResponse> getBlogPostById(Long id) {
        BlogPost blog = blogPostRepository.findById(id).orElse(null);
        if(blog == null){
            throw new BlogPostNotFoundException("BlogPost with id " + id + " not found");
        }
        return Optional.of(BlogPostMapper.mapToResponseDto(blog));
    }

    @Override
    public void deleteBlogPostById(Long id, String username) {
        BlogPost existingBlogPost = blogPostRepository.findById(id).orElse(null);
        User user = userRepository.findByUsername(username).get();
        if (existingBlogPost != null) {
            //check role of the user is USER or ADMIN
            if(user.getRole().equals(Role.USER)) {

                //check if the requesting user is the author of the blog post
                if(existingBlogPost.getUser().equals(username)) {
                    blogPostRepository.deleteById(id);
                }else {
                    throw new UserUnAuthorizedException("You are not authorized to delete this blog post");
                }


            }else if (user.getRole().equals(Role.ADMIN)) {
                blogPostRepository.deleteById(id);

            }

            else {
                throw new UserUnAuthorizedException("You are not authorized to delete this blog post");
            }


        } else {
            throw new BlogPostNotFoundException("Blog post with id " + id + " not found");
        }
    }

    @Override
    public BlogPostResponse updateBlogPost(BlogPost updatedBlogPost, String username) {
        Long id = updatedBlogPost.getId(); // Assuming id is a field in BlogPost

        // Check if the id is null
        if (id == null) {
            throw new BlogPostNotFoundException("Blog post id cannot be null");
        }

        BlogPost existingBlogPost = blogPostRepository.findById(id).orElse(null);

        if (existingBlogPost != null) {
            //check if user is the author of the blog post
            if (!existingBlogPost.getUser().equals(username)) {
                throw new UserUnAuthorizedException("You are not authorized to update this blog post");
            }

            // Update the existing blog post with the data from updatedBlogPost
            existingBlogPost.setTitle(updatedBlogPost.getTitle());
            existingBlogPost.setContent(updatedBlogPost.getContent());
            // Update other fields as needed

            // Save the updated blog post
            existingBlogPost = blogPostRepository.save(existingBlogPost);

            return BlogPostMapper.mapToResponseDto(existingBlogPost);
        } else {
            throw new BlogPostNotFoundException("Blog post with id " + id + " not found");
        }
    }


    @Override
    public Page<BlogPost> getAllBlogPosts(PageRequest pageable) {
        return blogPostRepository.findAll(pageable);
    }

    @Override
    public List<BlogPostResponse> getAllBlogs() {
        List<BlogPost> blogPosts = blogPostRepository.findAll();
        List<BlogPostResponse> blogPostResponses = new ArrayList<>();
        for(BlogPost blogPost : blogPosts) {
            blogPostResponses.add(BlogPostMapper.mapToResponseDto(blogPost));
        }
        return blogPostResponses;


    }

    @Override
    public List<BlogPostResponse> searchBlogPosts(String keyword) {
        // Check if the keyword is empty or null
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Keyword cannot be empty or null");
        }

        // Search for blog posts by keyword in title or content
        List<BlogPost> blogPosts = blogPostRepository.searchByTitleOrContent(keyword);

        // Check if no blog posts are found for the given keyword
        if (blogPosts.isEmpty()) {
            throw new BlogPostNotFoundException("No blog posts found for keyword: " + keyword);
        }

        // Convert found blog posts to response DTOs
        List<BlogPostResponse> blogPostResponses = new ArrayList<>();
        for (BlogPost blogPost : blogPosts) {
            blogPostResponses.add(BlogPostMapper.mapToResponseDto(blogPost));
        }

        return blogPostResponses;
    }


    @Override
    public String extractUsernameFromToken(String authHeader) {
        String userEmail = null;
        final String jwt;

        if (!StringUtils.isEmpty(authHeader) && org.apache.commons.lang3.StringUtils.startsWith(authHeader, "Bearer ")) {

            jwt = authHeader.substring(7);
            userEmail = jwtService.extractUserName(jwt);

        }
        return userEmail;
    }


}