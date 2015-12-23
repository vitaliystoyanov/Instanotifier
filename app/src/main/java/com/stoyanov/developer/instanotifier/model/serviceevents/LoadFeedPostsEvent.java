package com.stoyanov.developer.instanotifier.model.serviceevents;

import com.stoyanov.developer.instanotifier.model.pojo.Post;

import java.io.Serializable;
import java.util.ArrayList;

public class LoadFeedPostsEvent implements Serializable {

    private ArrayList<Post> list;
    private boolean onlyAppend;

    public boolean isOnlyAppend() {
        return onlyAppend;
    }

    public void setOnlyAppend(boolean state) {
        onlyAppend = state;
    }

    public LoadFeedPostsEvent(ArrayList<Post> list) {
        this.list = list;
    }

    public ArrayList<Post> getData() {
        return list;
    }

    public void setList(ArrayList<Post> list) {
        this.list = list;
    }
}
