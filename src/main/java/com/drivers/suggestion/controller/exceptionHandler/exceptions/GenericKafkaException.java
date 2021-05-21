package com.drivers.suggestion.controller.exceptionHandler.exceptions;

public class GenericKafkaException extends RuntimeException{
    public GenericKafkaException(String message) {
        super(message);
    }
}
