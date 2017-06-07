package com.uom.las3014.dao;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * {@link Entity} for the user_topic_mapping table
 */
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

    public UserTopicMapping(final User user, final Topic topic, final Timestamp interestedFrom) {
        this.user = user;
        this.topic = topic;
        this.interestedFrom = interestedFrom;
        this.isEnabled = true;
    }

    public Long getMappingId() {
        return mappingId;
    }

    public void setMappingId(final Long mappingId) {
        this.mappingId = mappingId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(final Topic topic) {
        this.topic = topic;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(final boolean enabled) {
        isEnabled = enabled;
    }

    public Timestamp getInterestedFrom() {
        return interestedFrom;
    }

    public void setInterestedFrom(final Timestamp interestedFrom) {
        this.interestedFrom = interestedFrom;
    }

    public Timestamp getInterestedTo() {
        return interestedTo;
    }

    public void setInterestedTo(final Timestamp interestedTo) {
        this.interestedTo = interestedTo;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserTopicMapping that = (UserTopicMapping) o;

        if (isEnabled != that.isEnabled) return false;
        if (user != null ? !user.getUsername().equals(that.user.getUsername()) : that.user != null) return false;
        if (topic != null ? !topic.getTopicName().equals(that.topic.getTopicName()) : that.topic != null) return false;
        return interestedFrom != null ? interestedFrom.equals(that.interestedFrom) : that.interestedFrom == null;
    }

    @Override
    public int hashCode() {
        int result = user != null ? user.getUsername().hashCode() : 0;
        result = 31 * result + (topic != null ? topic.getTopicName().hashCode() : 0);
        result = 31 * result + (isEnabled ? 1 : 0);
        result = 31 * result + (interestedFrom != null ? interestedFrom.hashCode() : 0);
        return result;
    }
}