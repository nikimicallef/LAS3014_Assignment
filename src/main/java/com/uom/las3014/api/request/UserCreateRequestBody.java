package com.uom.las3014.api.request;

import com.uom.las3014.restcontroller.UserController;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Typically used as a representation of and input body for example when creating a new user
 * Requires 3 input parameters;
 * - {@link UserCreateRequestBody#username}: username of the new user
 * - {@link UserCreateRequestBody#password}: password of the new used
 * - {@link UserCreateRequestBody#interestedTopics}: a list of topic names which the user is interested in upon account creation. Can be empty.
 */
public class UserCreateRequestBody {
    @NotNull(message="Username can not be empty")
    private String username;

    @NotNull(message="Password can not be empty")
    private String password;

    @NotNull(message="List of interested topics must be present.")
    private List<String> interestedTopics;

    public UserCreateRequestBody() {}

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

    public List<String> getInterestedTopics() {
        return interestedTopics;
    }

    public void setInterestedTopics(final List<String> interestedTopics) {
        this.interestedTopics = interestedTopics;
    }
}
