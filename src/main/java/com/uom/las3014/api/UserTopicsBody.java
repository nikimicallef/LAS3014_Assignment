package com.uom.las3014.api;

import java.util.List;

public class UserTopicsBody {
    private List<String> additions;

    private List<String> removals;

    public UserTopicsBody() {
    }

    public UserTopicsBody(List<String> additions, List<String> removals) {
        this.additions = additions;
        this.removals = removals;
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
