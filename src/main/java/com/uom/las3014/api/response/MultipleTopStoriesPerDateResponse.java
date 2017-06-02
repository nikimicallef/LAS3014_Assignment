package com.uom.las3014.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class MultipleTopStoriesPerDateResponse {
    @JsonProperty("digests")
    private List<GroupTopStoriesByDateResponse> topStoriesByDateResponses;

    public MultipleTopStoriesPerDateResponse() {
        topStoriesByDateResponses = new ArrayList<>();
    }

    public MultipleTopStoriesPerDateResponse(final List<GroupTopStoriesByDateResponse> topStoriesByDateResponses) {
        this.topStoriesByDateResponses = topStoriesByDateResponses;
    }

    public List<GroupTopStoriesByDateResponse> getTopStoriesByDateResponses() {
        return topStoriesByDateResponses;
    }
}
