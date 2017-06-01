package com.uom.las3014.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({ "effective_date", "topic" })
public class TopicsTopStoryResponse {
    @JsonProperty("effective_date")
    private String effectiveDate;
    private List<TopicTopStoryResponse> topics;

    public TopicsTopStoryResponse(final LocalDate effectiveDate) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        this.effectiveDate = effectiveDate.format(formatter);
        topics = new ArrayList<>();
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public List<TopicTopStoryResponse> getTopics() {
        if(topics == null){
            return new ArrayList<>();
        } else {
            return topics;
        }
    }

    public class TopicTopStoryResponse {
        private String topic;
        @JsonProperty("top_stories")
        private List<TopStoryResponse> topStories;

        public TopicTopStoryResponse(final String topic) {
            this.topic = topic;
            this.topStories = new ArrayList<>();
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }

        public String getTopic() {
            return topic;
        }

        public List<TopStoryResponse> getTopStories() {
            if(topStories == null){
                return new ArrayList<>();
            } else {
                return topStories;
            }
        }

        public class TopStoryResponse {
            private String title;
            private String url;
            private Integer score;

            public TopStoryResponse(final String title, final String url, final Integer score) {
                this.title = title;
                this.url = url;
                this.score = score;
            }

            public String getTitle() {
                return title;
            }

            public String getUrl() {
                return url;
            }

            public Integer getScore() {
                return score;
            }
        }
    }
}
