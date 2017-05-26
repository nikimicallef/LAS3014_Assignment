package com.uom.las3014.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SessionTokenAndMessageResponse {
    private String message;
    @JsonProperty("session_token")
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
