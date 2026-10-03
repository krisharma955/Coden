package com.k955.Coden.exception;

import org.springframework.http.HttpStatus;

import java.time.Instant;

public record ExceptionResponse(
        HttpStatus status,
        String message,
        Instant timestamp
) {
}
