package com.uom.las3014.dao;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "user_topic_mapping")
public class UserTopicMapping implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long mappingId;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
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

    public Long getMappingId() {
        return mappingId;
    }

    public void setMappingId(Long mappingId) {
        this.mappingId = mappingId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserTopicMapping that = (UserTopicMapping) o;

        return mappingId.equals(that.mappingId);
    }

    @Override
    public int hashCode() {
        return mappingId.hashCode();
    }
}