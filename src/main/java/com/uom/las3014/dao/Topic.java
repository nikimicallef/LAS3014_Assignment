package com.uom.las3014.dao;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Topics")
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer topicId;

    private String topicName;

    @ManyToMany(mappedBy = "topics")
    private Set<User> users;

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
}
