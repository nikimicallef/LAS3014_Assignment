package com.uom.las3014.dao;

import com.uom.las3014.resources.Resources;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Entity
@Table(name = "Users")
public class User {
    @Id
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "session_Token")
    private String sessionToken;

    @Column(name = "session_Token_Created")
    private Timestamp sessionTokenCreated;

    @Column(name = "session_Token_Last_Used")
    private Timestamp sessionTokenLastUsed;

    public User() {}

    public User(final String username, final String password) {
        this.username = username;
        this.password = password;
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

    public boolean hasActiveSessionToken(){
        final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        timestamp.toLocalDateTime();

        return sessionToken != null
                && ((sessionTokenCreated != null
                        && ChronoUnit.SECONDS.between(sessionTokenCreated.toLocalDateTime(), timestamp.toLocalDateTime()) <= 30)
                    || (sessionTokenLastUsed != null
                        && ChronoUnit.SECONDS.between(sessionTokenLastUsed.toLocalDateTime(), timestamp.toLocalDateTime()) <= 30));
    }

    @Override
    public String toString() {
        final Map<String, String> jsonBodyKeyValuePair = new HashMap<>();
        jsonBodyKeyValuePair.put("username", username);
        jsonBodyKeyValuePair.put("password", password);
        jsonBodyKeyValuePair.put("sessionToken", sessionToken);
        jsonBodyKeyValuePair.put("sessionTokenCreated", sessionTokenCreated.toString());
        jsonBodyKeyValuePair.put("sessionTokenLastUsed", sessionTokenLastUsed.toString() );

        return Resources.jsonMessageBuilder(jsonBodyKeyValuePair);
    }
}
