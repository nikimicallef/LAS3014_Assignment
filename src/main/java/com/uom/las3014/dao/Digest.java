package com.uom.las3014.dao;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "digests")
public class Digest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long entryId;
    private Date dayOfWeek;
    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topicId;
    @ManyToOne
    @JoinColumn(name = "story_id")
    private Story storyId;

    public Digest() {}

    public Digest(Date dayOfWeek, Topic topicId, Story storyId) {
        this.dayOfWeek = dayOfWeek;
        this.topicId = topicId;
        this.storyId = storyId;
    }

    public Long getEntryId() {
        return entryId;
    }

    public void setEntryId(Long entryId) {
        this.entryId = entryId;
    }

    public Date getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(Date dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
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