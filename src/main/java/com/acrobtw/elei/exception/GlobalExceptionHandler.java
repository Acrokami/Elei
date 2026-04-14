package com.acrobtw.elei.exception;

import java.time.LocalDateTime;
import java.util.stream.Collectors;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.acrobtw.elei.dto.ApiErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);



    // 400
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleBadRequest(IllegalArgumentException ex) {
        log.warn("400: Bad Request: {}", ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, "Bad request", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleNotValidArgument(MethodArgumentNotValidException ex) {
        log.warn("Not Valid Argument: {}", ex.getBindingResult().getFieldErrors());
        String error = ex.getBindingResult().getFieldErrors()
        .stream()
        .map(FieldError::getDefaultMessage)
        .collect(Collectors.joining(", "));
        return buildResponse(HttpStatus.BAD_REQUEST, "Validation Error", error);
    }

    // 404
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        log.warn("404: Not Found: {}", ex.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage());
    }

    @ExceptionHandler(ActivityNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleActivityNotFound(ActivityNotFoundException ex) {
        log.warn("404: Not Found: {}", ex.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage());
    }


    // 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleAll(Exception ex) {
        log.warn("Critical Unknown error: {}", ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", ex.getMessage());
    }


    private ResponseEntity<ApiErrorResponse> buildResponse(HttpStatus status, String error, String message) {
        ApiErrorResponse response = new ApiErrorResponse(
            status.value(), error, message, LocalDateTime.now());
            return new ResponseEntity<>(response, status);
    }
}
