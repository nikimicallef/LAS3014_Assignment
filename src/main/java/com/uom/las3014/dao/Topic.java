package com.uom.las3014.dao;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Topics")
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long topicId;
    private String topicName;
    @ManyToOne
    @JoinColumn(name = "top_story_id")
    private Story topStoryId;
    @OneToMany(mappedBy = "topic")
    private Set<UserTopicMapping> userTopicMappings;

    public Topic(){}

    public Topic(final String topicName) {
        this.topicName = topicName;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public Story getTopStoryId() {
        return topStoryId;
    }

    public void setTopStoryId(Story topStoryId) {
        this.topStoryId = topStoryId;
    }

    public Set<UserTopicMapping> getUserTopics() {
        if (userTopicMappings == null){
            userTopicMappings = new HashSet<>();
            return userTopicMappings;
        } else {
            return userTopicMappings;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Topic topic = (Topic) o;

        return topicName.equals(topic.topicName);
    }

    @Override
    public int hashCode() {
        return topicName.hashCode();
    }
}
