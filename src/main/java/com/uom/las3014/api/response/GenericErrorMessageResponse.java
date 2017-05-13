package com.uom.las3014.api.response;

public class GenericErrorMessageResponse {
    private String error;

    public GenericErrorMessageResponse(final String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
