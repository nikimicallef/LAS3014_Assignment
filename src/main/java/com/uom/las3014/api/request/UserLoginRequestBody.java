package com.uom.las3014.api.request;

import javax.validation.constraints.NotNull;

public class UserLoginRequestBody {
    @NotNull(message="Username can not be empty")
    private String username;

    @NotNull(message="Password can not be empty")
    private String password;

    public UserLoginRequestBody() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
