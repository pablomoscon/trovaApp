package com.trovaApp.exception;

import org.springframework.http.HttpStatus;

public class InvalidGenreException extends CustomException {
    public InvalidGenreException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
