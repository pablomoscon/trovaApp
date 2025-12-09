package com.trova_app.exception;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

public abstract class CustomException extends RuntimeException implements Serializable {
    private static final long SERIAL_VERSION_UID = 1L;
    private final HttpStatus status;

    public CustomException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}