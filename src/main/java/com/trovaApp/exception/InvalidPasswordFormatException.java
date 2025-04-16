package com.trovaApp.exception;

import org.springframework.http.HttpStatus;

public class InvalidPasswordFormatException extends CustomException {
    public InvalidPasswordFormatException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}