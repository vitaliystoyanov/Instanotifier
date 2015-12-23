package com.stoyanov.developer.instanotifier.model.services;


import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.joanzapata.android.asyncservice.api.annotation.ApplicationContext;
import com.joanzapata.android.asyncservice.api.annotation.AsyncService;
import com.joanzapata.android.asyncservice.api.annotation.Init;
import com.stoyanov.developer.instanotifier.model.Configuration;
import com.stoyanov.developer.instanotifier.model.multipleaccounts.AccountManager;
import com.stoyanov.developer.instanotifier.model.pojo.Account;
import com.stoyanov.developer.instanotifier.model.pojo.Photo;
import com.stoyanov.developer.instanotifier.model.pojo.Post;
import com.stoyanov.developer.instanotifier.model.serviceevents.GetPhotoProfileEvent;

import org.jinstagram.Instagram;
import org.jinstagram.auth.model.Token;
import org.jinstagram.entity.users.feed.MediaFeed;
import org.jinstagram.entity.users.feed.MediaFeedData;
import org.jinstagram.exceptions.InstagramException;

import java.util.ArrayList;
import java.util.List;

@AsyncService
public class ProfileService {

    @ApplicationContext
    protected static Context context;

    @Init
    static void init() {
        // Executed once for this service
    }

    public GetPhotoProfileEvent getUserPhotos() {
        String token = getAccessToken();
        if (token == null) {
            Log.i("DBG", "[ProfileService] Token == null!");
            return null;
        }
        Instagram instagram = performAuthorization(token);
        MediaFeed mediaFeed = null;
        try {
            mediaFeed = instagram.getRecentMediaFeed(getUserID());

        } catch (InstagramException e) {
            e.printStackTrace();
        }
        List<MediaFeedData> list = mediaFeed.getData();

        return new GetPhotoProfileEvent(convert(list));
    }

    @NonNull
    private Instagram performAuthorization(String parameterToken) {
        Instagram instagram = new Instagram(Configuration.CLIENT_ID);
        Token token = new Token(parameterToken, Configuration.CLIENT_ID);
        instagram.setAccessToken(token);
        return instagram;
    }

    private String getAccessToken() {
        AccountManager manager = new AccountManager(context);
        Account account = manager.getCurrent();
        if (account == null) return null;
        return account.getToken();
    }

    private String getUserID() {
        AccountManager manager = new AccountManager(context);
        Account account = manager.getCurrent();
        if (account == null) return null;
        return account.getUserId();
    }

    private ArrayList<Photo> convert(List<MediaFeedData> listData) {
        ArrayList<Photo> list = new ArrayList<>();
        for (MediaFeedData data : listData) {
            Photo photo = new Photo(data.getImages().getStandardResolution().getImageUrl());
            list.add(photo);
        }
        return list;
    }
}
