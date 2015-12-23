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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (userId != null ? !userId.equals(account.userId) : account.userId != null) return false;
        if (token != null ? !token.equals(account.token) : account.token != null) return false;
        return !(username != null ? !username.equals(account.username) : account.username != null);

    }

    @Override
    public String toString() {
        return "Account{" +
                "userId='" + userId + '\'' +
                ", token='" + token + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
