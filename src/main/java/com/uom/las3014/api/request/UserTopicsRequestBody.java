package com.uom.las3014.api.request;

import java.util.List;

/**
 * Typically used as a representation of and input body, for example when adding or removing topics for a user.
 * Contains 2 input parameters;
 * - {@link UserTopicsRequestBody#additions}: A list of topic names the user wants to add to his set of interested topics.
 * - {@link UserTopicsRequestBody#removals}: A list of topic names the user wants to remove from his set of interested topics.
 */
public class UserTopicsRequestBody {
    private List<String> additions;

    private List<String> removals;

    public UserTopicsRequestBody() {
    }

    public List<String> getAdditions() {
        return additions;
    }

    public void setAdditions(final List<String> additions) {
        this.additions = additions;
    }

    public List<String> getRemovals() {
        return removals;
    }

    public void setRemovals(final List<String> removals) {
        this.removals = removals;
    }
}
