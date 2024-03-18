package com.example.blogging.exceptions;

public class BlogPostNotFoundException extends RuntimeException {
    public BlogPostNotFoundException(String message) {
        super(message);
    }

    public BlogPostNotFoundException() {
    }
}
