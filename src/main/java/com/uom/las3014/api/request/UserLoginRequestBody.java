package com.uom.las3014.api.request;

import javax.validation.constraints.NotNull;

/**
 * Typically used as a representation of and input body, for example when logging in.
 * Requires 2 input parameters;
 * - {@link UserLoginRequestBody#username}: username of the new user
 * - {@link UserLoginRequestBody#password}: password of the new used
 */
public class UserLoginRequestBody {
    @NotNull(message="Username can not be empty")
    private String username;

    @NotNull(message="Password can not be empty")
    private String password;

    public UserLoginRequestBody() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }
}
