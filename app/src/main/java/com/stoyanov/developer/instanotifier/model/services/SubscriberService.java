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
import com.stoyanov.developer.instanotifier.model.pojo.Subscriber;
import com.stoyanov.developer.instanotifier.model.serviceevents.LoadSubscribersEvent;

import org.jinstagram.Instagram;
import org.jinstagram.auth.model.Token;
import org.jinstagram.entity.users.feed.UserFeed;
import org.jinstagram.entity.users.feed.UserFeedData;
import org.jinstagram.exceptions.InstagramException;

import java.util.ArrayList;
import java.util.List;

@AsyncService
public class SubscriberService {

    @ApplicationContext
    protected static Context context;

    @Init
    static void init() {
        // Executed once for this service
    }

    public LoadSubscribersEvent getFollowsList() {
        Instagram instagram = performAuthorization();
        UserFeed feed = null;
        try {
            feed = instagram.getUserFollowList(getCurrentUserID());
        } catch (InstagramException e) {
            e.printStackTrace();
        }
        ArrayList<Subscriber> result = convert(feed.getUserList());
        Log.i("DBG", "[SubscriberService] getFollowsList()");
        return new LoadSubscribersEvent(result);
    }

    @NonNull
    private Instagram performAuthorization() {
        Instagram instagram = new Instagram(Configuration.CLIENT_ID);
        Token token = new Token(getAccessToken(), Configuration.CLIENT_ID);
        instagram.setAccessToken(token);
        return instagram;
    }

    private String getAccessToken() {
        AccountManager manager = new AccountManager(context);
        Account account = manager.getCurrent();
        return account.getToken();
    }

    private String getCurrentUserID() {
        AccountManager manager = new AccountManager(context);
        Account account = manager.getCurrent();
        return account.getUserId();
    }

    private ArrayList<Subscriber> convert(List<UserFeedData> list) {
        ArrayList<Subscriber> resultList = new ArrayList<>();
        for (UserFeedData data : list) {
            Log.i("DBG", "[SubscriberService]user = " + data.getUserName());
            resultList.add(new Subscriber(data.getUserName(), 0, 0, data.getProfilePictureUrl()));
        }
        return resultList;
    }
}
