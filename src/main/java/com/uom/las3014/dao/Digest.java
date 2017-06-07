package com.uom.las3014.dao;

import javax.persistence.*;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * {@link Entity} for the digests table
 */
@Entity
@Table(name = "digests")
public class Digest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long digestId;
    private Date dayOfWeek;
    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topicId;
    @ManyToOne
    @JoinColumn(name = "story_id")
    private Story storyId;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_digest_mapping", joinColumns = @JoinColumn(name = "digest_id", referencedColumnName = "digestId"), inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "userId"))
    private Set<User> usersAssignedToDigest;

    public Digest() {}

    public Digest(final Date dayOfWeek, final Topic topicId, final Story storyId, final Set<User> usersAssignedToDigest) {
        this.dayOfWeek = dayOfWeek;
        this.topicId = topicId;
        this.storyId = storyId;
        this.usersAssignedToDigest = usersAssignedToDigest;
    }

    public Long getDigestId() {
        return digestId;
    }

    public void setDigestId(final Long digestId) {
        this.digestId = digestId;
    }

    public Date getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(final Date dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Topic getTopicId() {
        return topicId;
    }

    public void setTopicId(final Topic topicId) {
        this.topicId = topicId;
    }

    public Story getStoryId() {
        return storyId;
    }

    public void setStoryId(final Story storyId) {
        this.storyId = storyId;
    }

    public Set<User> getUsersAssignedToDigest() {
        if(usersAssignedToDigest == null){
            return new HashSet<>();
        } else {
            return usersAssignedToDigest;
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Digest digest = (Digest) o;

        return digestId != null ? digestId.equals(digest.digestId) : digest.digestId == null;
    }

    @Override
    public int hashCode() {
        return digestId != null ? digestId.hashCode() : 0;
    }
}