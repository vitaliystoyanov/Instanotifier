package com.stoyanov.developer.instanotifier.model.services;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.joanzapata.android.asyncservice.api.annotation.ApplicationContext;
import com.joanzapata.android.asyncservice.api.annotation.AsyncService;
import com.joanzapata.android.asyncservice.api.annotation.Init;
import com.stoyanov.developer.instanotifier.model.Configuration;
import com.stoyanov.developer.instanotifier.model.serviceevents.LoadFeedPostsEvent;
import com.stoyanov.developer.instanotifier.model.multipleaccounts.AccountManager;
import com.stoyanov.developer.instanotifier.model.pojo.Account;
import com.stoyanov.developer.instanotifier.model.pojo.Post;

import org.jinstagram.Instagram;
import org.jinstagram.auth.model.Token;
import org.jinstagram.entity.common.Pagination;
import org.jinstagram.entity.users.feed.MediaFeed;
import org.jinstagram.entity.users.feed.MediaFeedData;
import org.jinstagram.exceptions.InstagramException;

import java.util.ArrayList;
import java.util.List;

@AsyncService
public class FeedService {

    private static final int MEDIA_COUNT = 10;

    @ApplicationContext
    protected static Context context;

    @Init
    static void init() {
        // Executed once for this service
    }

    public LoadFeedPostsEvent getPosts() {
        List<MediaFeedData> mediaList = null;
        Instagram instagram = performAuthorization();
        try {
            MediaFeed mediaFeed = instagram.getUserFeeds();
            mediaList = mediaFeed.getData();
            Pagination pagination = mediaFeed.getPagination();
            MediaFeed userMediaNextPage = instagram.getUserFeeds(pagination.getNextMaxId(), pagination.getNextMinId(), MEDIA_COUNT);
            int counter = 0;
            while (userMediaNextPage.getPagination() != null && counter < 1) {
                mediaList.addAll(userMediaNextPage.getData());
                Pagination pg = userMediaNextPage.getPagination();
                userMediaNextPage = instagram.getUserFeeds(pg.getNextMaxId(), pg.getNextMinId(), MEDIA_COUNT);
                counter++;
            }
            int mediaCount = mediaList.size();
            Log.i("DBG", "[FeedService] Media count = " + mediaCount);
        } catch (InstagramException | NullPointerException e) {
            e.printStackTrace();
        }
        ArrayList<Post> list = convert(mediaList);
        return new LoadFeedPostsEvent(list);
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

    private ArrayList<Post> convert(List<MediaFeedData> listData) {
        ArrayList<Post> list = new ArrayList<>();
        for (MediaFeedData data : listData) {
            Post post = new Post();
            post.setUsername(data.getUser().getUserName());
            post.setDateTime(data.getCreatedTime());
            post.setPostImageURL(data.getImages().getStandardResolution().getImageUrl());
            post.setLikes(data.getLikes().getCount());
            post.setComments(data.getComments().getCount());
            if (data.getCaption() != null) {
                post.setCaption(data.getCaption().getText() + "");
            } else {
                post.setCaption("");
            }
            list.add(post);
        }
        return list;
    }
}
