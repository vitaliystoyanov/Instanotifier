package com.stoyanov.developer.instanotifier.model.serviceevents;

import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.io.Serializable;
import java.util.ArrayList;

public class RefreshProfilesEvent implements Serializable {

    private ArrayList<IProfile> profiles;

    public RefreshProfilesEvent(ArrayList<IProfile> profiles) {
        this.profiles = profiles;
    }

    public ArrayList<IProfile> getProfiles() {
        return profiles;
    }

    public void setProfiles(ArrayList<IProfile> profiles) {
        this.profiles = profiles;
    }
}
