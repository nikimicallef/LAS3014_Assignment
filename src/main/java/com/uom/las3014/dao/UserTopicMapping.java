package com.uom.las3014.dao;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "user_topic_mapping")
public class UserTopicMapping implements Serializable{
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Id
    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;
    private boolean isEnabled;
    private Timestamp interestedFrom;
    private Timestamp interestedTo;

    public UserTopicMapping() {}

    public UserTopicMapping(User user, Topic topic, Timestamp interestedFrom) {
        this.user = user;
        this.topic = topic;
        this.interestedFrom = interestedFrom;
        this.isEnabled = true;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public Timestamp getInterestedFrom() {
        return interestedFrom;
    }

    public void setInterestedFrom(Timestamp interestedFrom) {
        this.interestedFrom = interestedFrom;
    }

    public Timestamp getInterestedTo() {
        return interestedTo;
    }

    public void setInterestedTo(Timestamp interestedTo) {
        this.interestedTo = interestedTo;
    }
}