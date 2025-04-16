package com.trovaApp.exception;

import org.springframework.http.HttpStatus;

public class ArtistNotFoundException extends CustomException {
    public ArtistNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}