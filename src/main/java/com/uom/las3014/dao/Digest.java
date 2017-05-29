package com.uom.las3014.dao;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "digests")
public class Digest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long entryId;
    private Timestamp week_timestamp;
    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topicId;
    @ManyToOne
    @JoinColumn(name = "story_id")
    private Story storyId;

    public Digest() {}

    public Digest(Timestamp week_timestamp, Topic topicId, Story storyId) {
        this.week_timestamp = week_timestamp;
        this.topicId = topicId;
        this.storyId = storyId;
    }

    public Long getEntryId() {
        return entryId;
    }

    public void setEntryId(Long entryId) {
        this.entryId = entryId;
    }

    public Timestamp getWeek_timestamp() {
        return week_timestamp;
    }

    public void setWeek_timestamp(Timestamp week_timestamp) {
        this.week_timestamp = week_timestamp;
    }

    public Topic getTopicId() {
        return topicId;
    }

    public void setTopicId(Topic topicId) {
        this.topicId = topicId;
    }

    public Story getStoryId() {
        return storyId;
    }

    public void setStoryId(Story storyId) {
        this.storyId = storyId;
    }
}