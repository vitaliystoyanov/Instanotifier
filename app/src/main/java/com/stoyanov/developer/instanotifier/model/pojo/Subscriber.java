package com.stoyanov.developer.instanotifier.model.pojo;

public class Subscriber {

    private String username;
    private int countFollowers;
    private int countFollows;
    private String profileImageURL;

    public Subscriber(String username, int countFollowers, int countFollows, String profileImageURL) {
        this.username = username;
        this.countFollowers = countFollowers;
        this.countFollows = countFollows;
        this.profileImageURL = profileImageURL;
    }

    public int getCountFollowers() {
        return countFollowers;
    }

    public void setCountFollowers(int countFollowers) {
        this.countFollowers = countFollowers;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getCountFollows() {
        return countFollows;
    }

    public void setCountFollows(int countFollows) {
        this.countFollows = countFollows;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public void setProfileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }
}
