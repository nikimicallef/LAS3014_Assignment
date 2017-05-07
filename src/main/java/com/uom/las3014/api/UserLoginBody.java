package com.uom.las3014.api;

import javax.validation.constraints.NotNull;

public class UserLoginBody {
    @NotNull(message="Username can not be empty")
    private String username;

    @NotNull(message="Password can not be empty")
    private String password;

    public UserLoginBody() {}

    public UserLoginBody(final String username, final String password) {
        this.username = username;
        this.password = password;
    }

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
