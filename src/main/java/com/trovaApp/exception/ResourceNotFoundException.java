package com.trovaApp.exception;

import org.springframework.http.HttpStatus;

// ResourceNotFoundException.java
public class ResourceNotFoundException extends CustomException {
    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}