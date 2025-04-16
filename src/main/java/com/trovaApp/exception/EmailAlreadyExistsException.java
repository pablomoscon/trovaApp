package com.trovaApp.exception;

import org.springframework.http.HttpStatus;

public class EmailAlreadyExistsException extends CustomException {
    public EmailAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}

