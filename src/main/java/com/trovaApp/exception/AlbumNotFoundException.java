package com.trovaApp.exception;

import org.springframework.http.HttpStatus;

public class AlbumNotFoundException extends CustomException {
    public AlbumNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
