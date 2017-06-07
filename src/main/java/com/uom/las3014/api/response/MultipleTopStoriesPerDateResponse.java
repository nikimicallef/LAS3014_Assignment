package com.uom.las3014.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents top stories per topics for a number of distinct dates
 * Contains 1 parameter;
 * - {@link MultipleTopStoriesPerDateResponse#topStoriesByDateResponses}: Group of top stories for a number of topics for a specific date
 */
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
