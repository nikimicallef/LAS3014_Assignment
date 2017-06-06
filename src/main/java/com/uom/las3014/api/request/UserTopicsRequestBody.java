package com.uom.las3014.api.request;

import java.util.List;

public class UserTopicsRequestBody {
    private List<String> additions;

    private List<String> removals;

    public UserTopicsRequestBody() {
    }

    public List<String> getAdditions() {
        return additions;
    }

    public void setAdditions(List<String> additions) {
        this.additions = additions;
    }

    public List<String> getRemovals() {
        return removals;
    }

    public void setRemovals(List<String> removals) {
        this.removals = removals;
    }
}
