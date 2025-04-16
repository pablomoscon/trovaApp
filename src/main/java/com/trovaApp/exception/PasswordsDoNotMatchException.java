package com.trovaApp.exception;

import org.springframework.http.HttpStatus;

public class PasswordsDoNotMatchException extends CustomException {
    public PasswordsDoNotMatchException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}