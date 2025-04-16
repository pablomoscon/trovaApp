package com.trovaApp.exception;

import org.springframework.http.HttpStatus;

public class UsernameAlreadyExistsException extends CustomException {
    public UsernameAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}