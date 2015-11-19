package com.stoyanov.developer.instanotifier.model.serviceevents;

import java.io.Serializable;

public class ShowSnackbarEvent implements Serializable  {

    private String text;

    public ShowSnackbarEvent(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
