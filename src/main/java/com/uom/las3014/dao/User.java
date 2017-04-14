package com.uom.las3014.dao;

import com.uom.las3014.resources.Resources;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "Users")
public class User {
    @Id
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "session_token")
    private String session_token;

    @Column(name = "session_token_created")
    private Date session_token_created;

    @Column(name = "session_token_last_used")
    private Date session_token_last_used;

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

    public String getSession_token() {
        return session_token;
    }

    public void setSession_token(String session_token) {
        this.session_token = session_token;
    }

    public Date getSession_token_created() {
        return session_token_created;
    }

    public void setSession_token_created(Date session_token_created) {
        this.session_token_created = session_token_created;
    }

    public Date getSession_token_last_used() {
        return session_token_last_used;
    }

    public void setSession_token_last_used(Date session_token_last_used) {
        this.session_token_last_used = session_token_last_used;
    }

    @Override
    public String toString() {
        final Map<String, String> jsonBodyKeyValuePair = new HashMap<>();
        jsonBodyKeyValuePair.put("username", username);
        jsonBodyKeyValuePair.put("password", password);
        jsonBodyKeyValuePair.put("session_token", session_token);
        jsonBodyKeyValuePair.put("session_token_created", session_token_created.toString());
        jsonBodyKeyValuePair.put("session_token_last_used", session_token_last_used.toString() );

        return Resources.jsonMessageBuilder(jsonBodyKeyValuePair);
    }
}
