package com.k955.Coden.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceId, String resourceName) {
        super(resourceName + " not found with Id: " + resourceId);
    }
}
