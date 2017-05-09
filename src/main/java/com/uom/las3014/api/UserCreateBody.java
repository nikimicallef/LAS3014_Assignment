package com.uom.las3014.api;

import javax.validation.constraints.NotNull;
import java.util.List;

public class UserCreateBody {
    @NotNull(message="Username can not be empty")
    private String username;

    @NotNull(message="Password can not be empty")
    private String password;

    @NotNull(message="List of interested topics must be present.")
    private List<String> interestedTopics;

    public UserCreateBody() {}

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

    public List<String> getInterestedTopics() {
        return interestedTopics;
    }

    public void setInterestedTopics(List<String> interestedTopics) {
        this.interestedTopics = interestedTopics;
    }
}
