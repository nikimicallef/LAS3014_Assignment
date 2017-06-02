package com.uom.las3014.exceptions;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {}

    public InvalidCredentialsException(final String message) {
        super(message);
    }

    public InvalidCredentialsException(final Throwable cause) {
        super(cause);
    }

    public InvalidCredentialsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public InvalidCredentialsException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
