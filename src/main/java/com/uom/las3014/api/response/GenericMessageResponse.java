package com.uom.las3014.api.response;

public class GenericMessageResponse {
    private String message;

    public GenericMessageResponse(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
