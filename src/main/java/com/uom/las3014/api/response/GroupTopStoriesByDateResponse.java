package com.uom.las3014.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({ "effective_date", "topic" })
public class GroupTopStoriesByDateResponse {
    @JsonProperty("effective_date")
    private String effectiveDate;
    private List<TopStoriesForTopicResponse> topics;

    public GroupTopStoriesByDateResponse() {
    }

    public GroupTopStoriesByDateResponse(final LocalDate effectiveDate) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        this.effectiveDate = effectiveDate.format(formatter);
        topics = new ArrayList<>();
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public List<TopStoriesForTopicResponse> getTopics() {
        if(topics == null){
            return new ArrayList<>();
        } else {
            return topics;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GroupTopStoriesByDateResponse that = (GroupTopStoriesByDateResponse) o;

        return effectiveDate != null ? effectiveDate.equals(that.effectiveDate) : that.effectiveDate == null;
    }

    @Override
    public int hashCode() {
        return effectiveDate != null ? effectiveDate.hashCode() : 0;
    }

    public class TopStoriesForTopicResponse {
        private String topic;
        @JsonProperty("top_stories")
        private List<TopStoryResponse> topStories;

        public TopStoriesForTopicResponse(final String topic) {
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TopStoriesForTopicResponse that = (TopStoriesForTopicResponse) o;

            return topic != null ? topic.equals(that.topic) : that.topic == null;
        }

        @Override
        public int hashCode() {
            return topic != null ? topic.hashCode() : 0;
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

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                TopStoryResponse that = (TopStoryResponse) o;

                if (title != null ? !title.equals(that.title) : that.title != null) return false;
                if (url != null ? !url.equals(that.url) : that.url != null) return false;
                return score != null ? score.equals(that.score) : that.score == null;
            }

            @Override
            public int hashCode() {
                int result = title != null ? title.hashCode() : 0;
                result = 31 * result + (url != null ? url.hashCode() : 0);
                result = 31 * result + (score != null ? score.hashCode() : 0);
                return result;
            }
        }
    }
}
