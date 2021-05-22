package com.drivers.suggestion.controller.exceptionHandler;

import com.drivers.suggestion.model.requestAndResponse.ApiError;
import com.drivers.suggestion.model.requestAndResponse.Error;
import org.hibernate.id.IdentifierGenerationException;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashSet;
import java.util.Set;

/**
 * Intercepts the exceptions between the rest controller (Inside world) and rest service (Outside world)
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
@RestController
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles all the JSON error's.
     * @param ex - exception
     * @param headers - headers
     * @param status - response status
     * @param request - request
     * @return - a response entity with detailed message
     */
    @NotNull
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, @NotNull HttpHeaders headers, @NotNull HttpStatus status, @NotNull WebRequest request) {
        String error = "Bad JSON format, please verify before retry.\nFurther information - ";
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error + ex.getMessage()));
    }

    /**
     * Intercepts if a data model has validation issues - handles by inbuilt method in response entity exception handler
     * @param ex - exception
     * @param headers - headers
     * @param status - status
     * @param request - request
     * @return - a response entity with detailed message
     */
    @NotNull
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  @NotNull HttpHeaders headers,
                                                                  @NotNull HttpStatus status, @NotNull WebRequest request) {
        Set<Error> errors = new HashSet<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            Error errorMessage = Error.builder()
                    .fieldName(((FieldError) error).getField())
                    .errorMessage(error.getDefaultMessage())
                    .build();
            errors.add(errorMessage);
        });
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, errors);
        return buildResponseEntity(apiError);
    }

    /**
     * Intercepts if a data model has validation issues - manual handling
     * @param ex - exception
     * @return - a response entity with detailed message
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        Set<Error> errorMessage = new HashSet<>();
        if (violations != null && !violations.isEmpty()) {
            violations.iterator().forEachRemaining((next) -> {
                Error error = Error.builder()
                        .fieldName(((PathImpl)next.getPropertyPath()).getLeafNode().getName())
                        .errorMessage(next.getMessage())
                        .build();
                errorMessage.add(error);
            });
        }
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, errorMessage);
        return buildResponseEntity(apiError);
    }

    /**
     * Builds the response entity by using provided apiError object.
     * @param apiError - error object
     * @return - a response entity with detailed message
     */
    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

}
