package com.drivers.suggestion.controller.exceptionHandler;


import org.hibernate.id.IdentifierGenerationException;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.validation.ConstraintViolationException;

public class RestExceptionHandlerTest {

    private static final String MESSAGE_BODY = "Testing the exception handlers";
    private final RestExceptionHandler handler = new RestExceptionHandler();

    @Test
    public void testHandleConstraintViolationException() {
        final ResponseEntity<Object> handled = handler.handleConstraintViolationException(new ConstraintViolationException(null));
        Assertions.assertEquals(handled.getStatusCodeValue(), HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void testHandleIdentityGenerationException() {
        final ResponseEntity<Object> handled = handler.handleIdentityGenerationException(new IdentifierGenerationException(MESSAGE_BODY));
        Assertions.assertEquals(handled.getStatusCodeValue(), HttpStatus.BAD_REQUEST.value());
    }
}