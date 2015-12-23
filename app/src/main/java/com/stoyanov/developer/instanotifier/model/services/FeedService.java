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
    private static MediaFeed userMediaNextPage;


    @ApplicationContext
    protected static Context context;

    @Init
    static void init() {
        // Executed once for this service
    }

    public LoadFeedPostsEvent getPosts() {
        List<MediaFeedData> mediaList = null;
        String token = getAccessToken();
        if (token == null) {
            Log.i("DBG", "[FeedService] Token == null! Return null.");
            return null;
        }
        Instagram instagram = performAuthorization(token);
        try {
            MediaFeed mediaFeed = instagram.getUserFeeds();
            mediaList = mediaFeed.getData();

            Pagination pagination = mediaFeed.getPagination();
            userMediaNextPage = instagram.getUserFeeds(pagination.getNextMaxId(), pagination.getNextMinId(), MEDIA_COUNT);
            mediaList.addAll(userMediaNextPage.getData());

            Log.i("DBG", "[FeedService] Media count = " + mediaList.size());
        } catch (InstagramException | NullPointerException e) {
            e.printStackTrace();
        }
        ArrayList<Post> list = convert(mediaList);
        return new LoadFeedPostsEvent(list);
    }

    public LoadFeedPostsEvent getNextPosts() {
        ArrayList<MediaFeedData> mediaList = new ArrayList<>();
        String token = getAccessToken();
        if (token == null) {
            Log.i("DBG", "[FeedService] Token == null! Return null.");
            return null;
        }
        Instagram instagram = performAuthorization(token);

        if (userMediaNextPage != null && userMediaNextPage.getPagination() != null) {
            mediaList.addAll(userMediaNextPage.getData());
            Pagination pg = userMediaNextPage.getPagination();
            try {
                userMediaNextPage = instagram.getUserFeeds(pg.getNextMaxId(), pg.getNextMinId(), MEDIA_COUNT);
            } catch (InstagramException e) {
                e.printStackTrace();
            }
        }

        ArrayList<Post> nextList = convert(userMediaNextPage.getData());
        LoadFeedPostsEvent event = new LoadFeedPostsEvent(nextList);
        event.setOnlyAppend(true);
        return event;
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
