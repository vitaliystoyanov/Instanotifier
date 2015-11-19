package com.stoyanov.developer.instanotifier.model.pojo;

public class Post {

    private String profileImageURL;
    private String postImageURL;
    private String dateTime;
    private String username;
    private String caption;
    private int comments;
    private int likes;

    public Post() {
    }

    public Post(int likes, int comments, String username) {
        this.likes = likes;
        this.comments = comments;
        this.username = username;
    }

    public String getDateAndTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public void setProfileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }

    public String getPostImageURL() {
        return postImageURL;
    }

    public void setPostImageURL(String postImageURL) {
        this.postImageURL = postImageURL;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
}
