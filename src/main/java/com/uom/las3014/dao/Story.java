package com.uom.las3014.dao;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "Stories")
public class Story {
    @Id
    private Long storyId;
    private Integer score;
    private String title;
    private String url;
    private Timestamp dateCreated;

    public Story(){}

    public Story(Long storyId, Integer score, String title, String url, Timestamp dateCreated) {
        this.storyId = storyId;
        this.score = score;
        this.title = title;
        this.url = url;
        this.dateCreated = dateCreated;
    }

    public Long getStoryId() {
        return storyId;
    }

    public void setStoryId(Long storyId) {
        this.storyId = storyId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public String toString() {
        return "Story{" +
                "storyId=" + storyId +
                ", score=" + score +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", dateCreated=" + dateCreated +
                '}';
    }
}
