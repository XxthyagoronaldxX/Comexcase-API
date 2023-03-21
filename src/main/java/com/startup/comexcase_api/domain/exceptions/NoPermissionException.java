package com.startup.comexcase_api.domain.exceptions;

public class NoPermissionException extends RuntimeException {
    public NoPermissionException(String message) {
        super(message);
    }

    public NoPermissionException() {
        super("NoPermissionException");
    }
}
