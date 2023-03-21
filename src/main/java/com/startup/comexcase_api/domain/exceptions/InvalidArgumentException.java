package com.startup.comexcase_api.domain.exceptions;

public class InvalidArgumentException extends RuntimeException {
    public InvalidArgumentException(String message) {
        super(message);
    }

    public InvalidArgumentException() {
        super("InvalidArgumentException");
    }
}
