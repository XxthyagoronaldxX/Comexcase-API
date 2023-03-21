package com.startup.comexcase_api.domain.exceptions;

public class EntityConflictException extends RuntimeException {
    public EntityConflictException(String message) {
        super(message);
    }

    public EntityConflictException() {
        super("EntityConflictException");
    }
}
