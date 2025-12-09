package com.trova_app.exception;

import org.springframework.http.HttpStatus;

public class ArtistAlreadyExistsException extends CustomException {
    public ArtistAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}