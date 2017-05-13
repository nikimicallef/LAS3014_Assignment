package com.uom.las3014.dao;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Topics")
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer topicId;
    private String topicName;
    @OneToMany(mappedBy = "topic")
    private Set<UserTopicMapping> userTopicMappings;

    public Topic(){}

    public Topic(final String topicName) {
        this.topicName = topicName;
    }

    public Integer getTopicId() {
        return topicId;
    }

    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public Set<UserTopicMapping> getUserTopics() {
        if (userTopicMappings == null){
            userTopicMappings = new HashSet<>();
            return userTopicMappings;
        } else {
            return userTopicMappings;
        }
    }
}
