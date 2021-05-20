package com.drivers.suggestion.controller.exceptionHandler;


import com.drivers.suggestion.controller.exceptionHandler.exceptions.GenericKafkaException;
import com.drivers.suggestion.controller.exceptionHandler.exceptions.GenericRestExpception;
import org.hibernate.id.IdentifierGenerationException;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;

public class RestExceptionHandlerTest {

    private static final String MESSAGE_BODY = "Testing the exception handlers";
    private final RestExceptionHandler handler = new RestExceptionHandler();

    @Test
    public void testHandleHttpMessageNotReadable() {
        final ResponseEntity<Object> handled = handler.handleHttpMessageNotReadable(new HttpMessageNotReadableException(MESSAGE_BODY), null, null, null);
        Assertions.assertEquals(handled.getStatusCodeValue(), HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void testHandleIdentityGenerationException() {
        final ResponseEntity<Object> handled = handler.handleIdentityGenerationException(new IdentifierGenerationException(MESSAGE_BODY));
        Assertions.assertEquals(handled.getStatusCodeValue(), HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void testHandleGenericRestException() {
        final ResponseEntity<Object> handled = handler.handleGenericRestException(new GenericRestExpception(MESSAGE_BODY, HttpStatus.NOT_FOUND));
        Assertions.assertEquals(handled.getStatusCodeValue(), HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void testHandleGenericKafkaException() {
        final ResponseEntity<Object> handled = handler.handleGenericKafkaException(new GenericKafkaException(MESSAGE_BODY));
        Assertions.assertEquals(handled.getStatusCodeValue(), HttpStatus.BAD_REQUEST.value());
    }
}