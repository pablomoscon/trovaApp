package com.trovaApp.exception;

import org.springframework.http.HttpStatus;

public class InvalidRoleException extends CustomException {
    public InvalidRoleException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}