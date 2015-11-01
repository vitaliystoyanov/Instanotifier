package com.stoyanov.developer.instanotifier.model.events;

import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.io.Serializable;
import java.util.ArrayList;

public class RefreshProfilesListEvent implements Serializable {

    private ArrayList<IProfile> profiles;

    public RefreshProfilesListEvent(ArrayList<IProfile> profiles) {
        this.profiles = profiles;
    }

    public ArrayList<IProfile> getProfiles() {
        return profiles;
    }

    public void setProfiles(ArrayList<IProfile> profiles) {
        this.profiles = profiles;
    }
}
