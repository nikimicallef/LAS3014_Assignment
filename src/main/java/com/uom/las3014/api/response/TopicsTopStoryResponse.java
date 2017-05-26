package com.uom.las3014.api.response;

import java.util.List;

public class TopicsTopStoryResponse {
    private List<TopicTopStoryResponse> topics;

    public TopicsTopStoryResponse(final List<TopicTopStoryResponse> topics) {
        this.topics = topics;
    }

    public List<TopicTopStoryResponse> getTopics() {
        return topics;
    }
}
