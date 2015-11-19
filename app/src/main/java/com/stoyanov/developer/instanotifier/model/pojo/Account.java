package com.stoyanov.developer.instanotifier.model.pojo;

public class Account {

    private String userId;
    private String token;
    private String username;

    public Account() {
    }

    public Account(String userId) {
        this.userId = userId;
    }

    public Account(String userID, String token) {
        this.userId = userID;
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
