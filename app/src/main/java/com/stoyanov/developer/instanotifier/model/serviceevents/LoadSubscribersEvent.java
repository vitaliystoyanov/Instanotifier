package com.stoyanov.developer.instanotifier.model.serviceevents;

import com.stoyanov.developer.instanotifier.model.pojo.Subscriber;

import java.util.ArrayList;

public class LoadSubscribersEvent {

    private ArrayList<Subscriber> list;

    public LoadSubscribersEvent(ArrayList<Subscriber> list) {
        this.list = list;
    }

    public ArrayList<Subscriber> getList() {
        return list;
    }
}
