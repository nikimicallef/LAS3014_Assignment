package com.uom.las3014.api.response;

public class SessionTokenAndMessageResponse {
    private String message;
    private String sessionToken;

    public SessionTokenAndMessageResponse(final String message, final String sessionToken) {
        this.message = message;
        this.sessionToken = sessionToken;
    }

    public String getMessage() {
        return message;
    }

    public String getSessionToken() {
        return sessionToken;
    }
}
