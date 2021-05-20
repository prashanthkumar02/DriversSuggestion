package com.drivers.suggestion.controller.exceptionHandler.exceptions;

import org.springframework.http.HttpStatus;

public class GenericRestExpception extends RuntimeException{
    HttpStatus status;
    public GenericRestExpception(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
