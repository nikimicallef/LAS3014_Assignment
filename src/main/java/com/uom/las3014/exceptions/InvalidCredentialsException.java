package com.uom.las3014.exceptions;

import com.uom.las3014.dao.User;

/**
 * Used when the provided {@link User#username} and {@link User#password} pair or {@link User#sessionToken} isinvalid
 */
public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {}

    public InvalidCredentialsException(final String message) {
        super(message);
    }

    public InvalidCredentialsException(final Throwable cause) {
        super(cause);
    }

    public InvalidCredentialsException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public InvalidCredentialsException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
