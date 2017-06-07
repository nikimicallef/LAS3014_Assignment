package com.uom.las3014.dao;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * {@link Entity} for the stories table
 */
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

    public Story(final Long storyId, final Integer score, final String title, final String url, final Timestamp dateCreated) {
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

    public void setStoryId(final Long storyId) {
        this.storyId = storyId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(final Integer score) {
        this.score = score;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(final Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(final boolean deleted) {
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Story story = (Story) o;

        return storyId.equals(story.storyId);
    }

    @Override
    public int hashCode() {
        return storyId.hashCode();
    }
}
