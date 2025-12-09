package com.trova_app.exception;

import org.springframework.http.HttpStatus;

public class InvalidPasswordFormatException extends CustomException {
    public InvalidPasswordFormatException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}