package com.uom.las3014.dao;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Stories")
public class Story {
    @Id
    private Long storyId;
    private Integer score;
    private String title;
    private String url;
    private Timestamp dateCreated;
    private boolean deleted;
    @OneToMany(mappedBy = "storyId", cascade = CascadeType.ALL)
    private Set<Digest> digests;

    public Story(){}

    public Story(Long storyId, Integer score, String title, String url, Timestamp dateCreated) {
        this.storyId = storyId;
        this.score = score;
        this.title = title;
        this.url = url;
        this.dateCreated = dateCreated;
        this.deleted = false;
        digests = new HashSet<>();
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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Set<Digest> getDigests() {
        if (this.digests == null){
            return new HashSet<>();
        } else {
            return digests;
        }
    }

    @Override
    public String toString() {
        return "Story{" +
                "storyId=" + storyId +
                ", score=" + score +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", dateCreated=" + dateCreated +
                ", deleted=" + deleted +
                '}';
    }

    public int compareTo(final Story story){
        return this.getScore().compareTo(story.getScore());
    }
}
