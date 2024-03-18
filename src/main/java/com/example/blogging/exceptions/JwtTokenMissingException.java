package com.example.blogging.exceptions;

public class JwtTokenMissingException extends RuntimeException {

    public JwtTokenMissingException(String message) {
        super(message);
    }
}
