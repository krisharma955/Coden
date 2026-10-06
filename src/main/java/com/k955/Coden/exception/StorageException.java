package com.k955.Coden.exception;

public class StorageException extends RuntimeException {
    public StorageException(String message, Exception e) {
        super(message);
    }
}
