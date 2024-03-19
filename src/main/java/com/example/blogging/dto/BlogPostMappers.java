package com.example.blogging.dto;

import com.example.blogging.entity.BlogPost;

public class BlogPostMappers {

    public static BlogPostResponse mapToResponseDto(BlogPost blogPost) {
        BlogPostResponse response = new BlogPostResponse();
        response.setId(blogPost.getId());
        response.setTitle(blogPost.getTitle());
        response.setContent(blogPost.getContent());
        // Map other fields as needed
        return response;
    }

    public static BlogPost mapToEntity(BlogPostResponse request) {
        BlogPost blogPost = new BlogPost();
        blogPost.setTitle(request.getTitle());
        blogPost.setContent(request.getContent());
        // Map other properties as needed
        return blogPost;
    }
}

