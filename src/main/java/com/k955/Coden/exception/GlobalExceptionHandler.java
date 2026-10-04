package com.k955.Coden.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleResourceNotFoundException(ResourceNotFoundException exception) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                HttpStatus.NOT_FOUND, exception.getMessage(), Instant.now());
        log.error(exceptionResponse.message());
        return ResponseEntity.status(exceptionResponse.status()).body(exceptionResponse);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponse> handleBadRequestException(BadRequestException exception) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                HttpStatus.BAD_REQUEST, exception.getMessage(), Instant.now());
        log.error(exceptionResponse.message());
        return ResponseEntity.status(exceptionResponse.status()).body(exceptionResponse);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponse> handleAccessDeniedException(AccessDeniedException exception) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                HttpStatus.FORBIDDEN, exception.getMessage(), Instant.now());
        log.error(exceptionResponse.message());
        return ResponseEntity.status(exceptionResponse.status()).body(exceptionResponse);
    }

}
