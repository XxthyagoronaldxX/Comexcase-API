package com.startup.comexcase_api.domain.exceptions;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException() {
        super("EntityNotFoundException");
    }
}
