package com.drivers.suggestion.controller.exceptionHandler;


import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.validation.ConstraintViolationException;

public class RestExceptionHandlerTest {

    private final RestExceptionHandler handler = new RestExceptionHandler();

    @Test
    public void testHandleConstraintViolationException() {
        final ResponseEntity<Object> handled = handler.handleConstraintViolationException(new ConstraintViolationException(null));
        Assertions.assertEquals(handled.getStatusCodeValue(), HttpStatus.BAD_REQUEST.value());
    }

}