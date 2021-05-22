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

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
@RestController
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @NotNull
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, @NotNull HttpHeaders headers, @NotNull HttpStatus status, @NotNull WebRequest request) {
        String error = "Bad JSON format, please verify before retry.\nFurther information - ";
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error + ex.getMessage()));
    }

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

    @ExceptionHandler(IdentifierGenerationException.class)
    protected ResponseEntity<Object> handleIdentityGenerationException(
            IdentifierGenerationException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
        return buildResponseEntity(apiError);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

}
