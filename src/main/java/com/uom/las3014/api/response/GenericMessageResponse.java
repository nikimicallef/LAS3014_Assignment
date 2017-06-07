package com.uom.las3014.api.response;

/**
 * Shows that an operation has succeeded but not specific parameters need to be returned.
 * A similar response but for a failure case: {@link GenericErrorMessageResponse}
 * Contains 1 parameter;
 * - {@link GenericMessageResponse#message}: Generic placeholder with a success message
 */
public class GenericMessageResponse {
    private String message;

    public GenericMessageResponse(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
