package com.trova_app.exception;

import org.springframework.http.HttpStatus;

public class TooManyLoginAttemptsException extends CustomException {
    public TooManyLoginAttemptsException(String message) {
        super(message, HttpStatus.TOO_MANY_REQUESTS);
    }
}