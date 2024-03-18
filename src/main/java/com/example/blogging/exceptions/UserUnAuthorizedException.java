package com.example.blogging.exceptions;

public class UserUnAuthorizedException extends RuntimeException {
    public UserUnAuthorizedException(String message) {
        super(message);
    }
}
