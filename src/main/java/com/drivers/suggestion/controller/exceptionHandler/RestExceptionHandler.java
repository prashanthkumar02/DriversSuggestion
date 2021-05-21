package com.drivers.suggestion.controller.exceptionHandler;

import com.drivers.suggestion.controller.exceptionHandler.exceptions.GenericKafkaException;
import com.drivers.suggestion.controller.exceptionHandler.exceptions.GenericRestExpception;
import org.hibernate.id.IdentifierGenerationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "Bad JSON format, please verify before retry further information - ";
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error + ex.getMessage()));
    }

    @ExceptionHandler(IdentifierGenerationException.class)
    protected ResponseEntity<Object> handleIdentityGenerationException(
            IdentifierGenerationException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(GenericRestExpception.class)
    protected ResponseEntity<Object> handleGenericRestException(
            GenericRestExpception ex) {
        ApiError apiError = new ApiError(ex.getStatus(), ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(GenericKafkaException.class)
    protected ResponseEntity<Object> handleGenericKafkaException(
            GenericKafkaException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
        return buildResponseEntity(apiError);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

}
