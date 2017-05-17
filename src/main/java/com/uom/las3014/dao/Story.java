package com.uom.las3014.dao;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Stories")
public class Story {
    @Id
    private Integer storyId;
    private Integer score;
    private String title;

    public Story(){}

    public Story(Integer storyId, Integer score, String title) {
        this.storyId = storyId;
        this.score = score;
        this.title = title;
    }

    public Integer getStoryId() {
        return storyId;
    }

    public void setStoryId(Integer storyId) {
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

    @Override
    public String toString() {
        return "Story{" +
                "storyId=" + storyId +
                ", score=" + score +
                ", title='" + title + '\'' +
                '}';
    }
}
