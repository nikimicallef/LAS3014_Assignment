package com.uom.las3014.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TopicTopStoryResponse {
    private String topic;
    @JsonProperty("top_story")
    private TopStoryResponse topStory;

    public TopicTopStoryResponse(final String topic, final TopStoryResponse topStory) {
        this.topic = topic;
        this.topStory = topStory;
    }

    public String getTopic() {
        return topic;
    }

    public TopStoryResponse getTopStory() {
        return topStory;
    }
}
