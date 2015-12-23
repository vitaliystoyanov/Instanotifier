package com.stoyanov.developer.instanotifier.model.serviceevents;

import com.stoyanov.developer.instanotifier.model.pojo.Photo;

import java.io.Serializable;
import java.util.ArrayList;

public class GetPhotoProfileEvent implements Serializable {

    private ArrayList<Photo> photos;

    public GetPhotoProfileEvent(ArrayList<Photo> photos) {
        this.photos = photos;
    }

    public ArrayList<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<Photo> photos) {
        this.photos = photos;
    }
}
