package com.uom.las3014.exceptions;

/**
 * Created by niki on 06/05/17.
 */
public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {}

    public InvalidCredentialsException(final String message) {
        super(message);
    }

    public InvalidCredentialsException(final Throwable cause) {
        super(cause);
    }

    public InvalidCredentialsException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
