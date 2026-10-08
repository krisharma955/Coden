package com.k955.Coden.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.Instant;
import java.util.stream.Collectors;

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

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<ExceptionResponse> handleStorageException(StorageException exception) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), Instant.now());
        log.error(exceptionResponse.message());
        return ResponseEntity.status(exceptionResponse.status()).body(exceptionResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionResponse> handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                HttpStatus.CONFLICT, exception.getMessage(), Instant.now());
        log.error(exceptionResponse.message());
        return ResponseEntity.status(exceptionResponse.status()).body(exceptionResponse);
    }

    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionResponse> handleSpringDataIntegrityViolationException(
            org.springframework.dao.DataIntegrityViolationException exception) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                HttpStatus.CONFLICT, "Duplicate value: resource already exists", Instant.now());
        log.error(exceptionResponse.message(), exception);
        return ResponseEntity.status(exceptionResponse.status()).body(exceptionResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                HttpStatus.BAD_REQUEST, message, Instant.now());
        log.error(exceptionResponse.message());
        return ResponseEntity.status(exceptionResponse.status()).body(exceptionResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                HttpStatus.BAD_REQUEST, "Invalid value for parameter '" + exception.getName() + "'", Instant.now());
        log.error(exceptionResponse.message());
        return ResponseEntity.status(exceptionResponse.status()).body(exceptionResponse);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ExceptionResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException exception) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                HttpStatus.BAD_REQUEST, "Missing required parameter '" + exception.getParameterName() + "'", Instant.now());
        log.error(exceptionResponse.message());
        return ResponseEntity.status(exceptionResponse.status()).body(exceptionResponse);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ExceptionResponse> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException exception) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                HttpStatus.BAD_REQUEST, "File exceeds the maximum allowed size", Instant.now());
        log.error(exceptionResponse.message());
        return ResponseEntity.status(exceptionResponse.status()).body(exceptionResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalArgumentException(IllegalArgumentException exception) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                HttpStatus.BAD_REQUEST, exception.getMessage(), Instant.now());
        log.error(exceptionResponse.message());
        return ResponseEntity.status(exceptionResponse.status()).body(exceptionResponse);
    }

}
