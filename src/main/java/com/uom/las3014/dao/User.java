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
    private Long userId;
    private String username;
    private String password;
    private String sessionToken;
    private Timestamp sessionTokenCreated;
    private Timestamp sessionTokenLastUsed;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserTopicMapping> userTopicMappings;

    public User(){}

    public User(final String username, final String password) {
        this.username = username;
        this.password = password;
        userTopicMappings = new HashSet<>();
    }

//    public User(final String username, final String password, final Set<UserTopicMapping> userTopicMappings) {
//        this.username = username;
//        this.password = password;
//        this.userTopicMappings = userTopicMappings;
//    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
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

    public Set<UserTopicMapping> getUserTopics() {
        if (userTopicMappings == null){
            userTopicMappings = new HashSet<>();
            return userTopicMappings;
        } else {
            return userTopicMappings;
        }
    }

    public boolean hasActiveSessionToken(){
        final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        timestamp.toLocalDateTime();

        //TODO: 30 minutes not 30 seconds

        return sessionToken != null
                && ((sessionTokenCreated != null
                        && ChronoUnit.MINUTES.between(sessionTokenCreated.toLocalDateTime(), timestamp.toLocalDateTime()) <= 30)
                    || (sessionTokenLastUsed != null
                        && ChronoUnit.MINUTES.between(sessionTokenLastUsed.toLocalDateTime(), timestamp.toLocalDateTime()) <= 30));
    }
}
