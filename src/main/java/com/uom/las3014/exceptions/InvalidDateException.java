package com.uom.las3014.exceptions;

import java.sql.Date;

/**
 * Used when the {@link Date} provided is not a valid date or when the latter {@link Date} is before the former {@link Date}
 */
public class InvalidDateException extends RuntimeException {

    public InvalidDateException() {
    }

    public InvalidDateException(final String message) {
        super(message);
    }

    public InvalidDateException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public InvalidDateException(final Throwable cause) {
        super(cause);
    }

    public InvalidDateException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
