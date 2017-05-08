package com.uom.las3014.dao;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer userId;

    private String username;

    private String password;

    private String sessionToken;

    private Timestamp sessionTokenCreated;

    private Timestamp sessionTokenLastUsed;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_topic_mapping",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "userId"),
            inverseJoinColumns = @JoinColumn(name = "topic_id", referencedColumnName = "topicId"))
    private Set<Topic> topics;

    public User(){}

    public User(final String username, final String password, final Set<Topic> interestedTopics) {
        this.username = username;
        this.password = password;
        this.topics = interestedTopics;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public Timestamp getSessionTokenCreated() {
        return sessionTokenCreated;
    }

    public void setSessionTokenCreated(final Timestamp sessionTokenCreated) {
        this.sessionTokenCreated = sessionTokenCreated;
    }

    public Timestamp getSessionTokenLastUsed() {
        return sessionTokenLastUsed;
    }

    public void setSessionTokenLastUsed(Timestamp sessionTokenLastUsed) {
        this.sessionTokenLastUsed = sessionTokenLastUsed;
    }

    public Set<Topic> getTopics() {
        if (topics == null){
            topics = new HashSet<>();
            return topics;
        } else {
            return topics;
        }
    }

    public boolean hasActiveSessionToken(){
        final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        timestamp.toLocalDateTime();

        //TODO: 30 minutes not 30 seconds

        return sessionToken != null
                && ((sessionTokenCreated != null
                        && ChronoUnit.SECONDS.between(sessionTokenCreated.toLocalDateTime(), timestamp.toLocalDateTime()) <= 30)
                    || (sessionTokenLastUsed != null
                        && ChronoUnit.SECONDS.between(sessionTokenLastUsed.toLocalDateTime(), timestamp.toLocalDateTime()) <= 30));
    }
}
