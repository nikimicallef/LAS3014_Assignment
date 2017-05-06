package com.uom.las3014.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException() {}

    public UserAlreadyExistsException(final String message) {
        super(message);
    }

    public UserAlreadyExistsException(final Throwable cause) {
        super(cause);
    }

    public UserAlreadyExistsException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
