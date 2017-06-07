package com.uom.las3014.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.uom.las3014.dao.Story;
import com.uom.las3014.dao.Topic;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the top stories per topics for a specific date.
 * Contains 2 parameters;
 * - {@link GroupTopStoriesByDateResponse#effectiveDate}: Gives the date of when the top stories provided are valid
 * - {@link GroupTopStoriesByDateResponse#topics}: Top Topic-stories pairs for the specified effective date
 */
@JsonPropertyOrder({ "effective_date", "topics" })
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

    /**
     * Represents the top stories for a specific {@link Topic}.
     * Contains 2 parameters;
     * - {@link TopStoriesForTopicResponse#topicName}: Name of the topic. Can be set to weekly when showing the top stories for the week
     * - {@link TopStoriesForTopicResponse#topStories}: Collections of top stories for the topic specified
     */
    public class TopStoriesForTopicResponse {
        @JsonProperty("topic")
        private String topicName;
        @JsonProperty("top_stories")
        private List<TopStoryResponse> topStories;

        public TopStoriesForTopicResponse(final String topicName) {
            this.topicName = topicName;
            this.topStories = new ArrayList<>();
        }

        public void setTopicName(final String topicName) {
            this.topicName = topicName;
        }

        public String getTopicName() {
            return topicName;
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

            return topicName != null ? topicName.equals(that.topicName) : that.topicName == null;
        }

        @Override
        public int hashCode() {
            return topicName != null ? topicName.hashCode() : 0;
        }

        /**
         * Represents a {@link Story}.
         * Contains 3 parameters;
         * - {@link TopStoryResponse#title}: Title of the story
         * - {@link TopStoryResponse#url}: URL of the story
         * - {@link TopStoryResponse#score}: Score of the story
         */
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
