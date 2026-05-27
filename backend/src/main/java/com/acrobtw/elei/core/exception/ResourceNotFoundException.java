package com.acrobtw.elei.core.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceName, Long id) {
        super(String.format("%s with id %s not found", resourceName, id));
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
