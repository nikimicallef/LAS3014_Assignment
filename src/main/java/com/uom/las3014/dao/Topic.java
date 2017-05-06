package com.uom.las3014.dao;

import javax.annotation.Generated;
import javax.persistence.*;

@Entity
@Table(name = "Topics")
public class Topic {
    //TODO: Implement joins
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer topicId;

    private String topicName;

    public Topic(){}

    public Topic(final Integer topicId, final String topicName) {
        this.topicId = topicId;
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
