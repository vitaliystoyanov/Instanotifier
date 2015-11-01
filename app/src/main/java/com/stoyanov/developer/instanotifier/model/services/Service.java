package com.stoyanov.developer.instanotifier.model.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;

import com.joanzapata.android.asyncservice.api.annotation.ApplicationContext;
import com.joanzapata.android.asyncservice.api.annotation.AsyncService;
import com.joanzapata.android.asyncservice.api.annotation.Init;
import com.joanzapata.android.asyncservice.api.annotation.Ui;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.stoyanov.developer.instanotifier.model.Configuration;
import com.stoyanov.developer.instanotifier.model.multiaccounts.AccountManager;
import com.stoyanov.developer.instanotifier.model.events.RefreshProfilesListEvent;
import com.stoyanov.developer.instanotifier.model.events.FinishLoginActivityEvent;
import com.stoyanov.developer.instanotifier.model.pojo.Account;

import org.jinstagram.Instagram;
import org.jinstagram.auth.model.Token;
import org.jinstagram.entity.users.basicinfo.UserInfo;
import org.jinstagram.entity.users.basicinfo.UserInfoData;
import org.jinstagram.exceptions.InstagramException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


@AsyncService
public class Service {

    @ApplicationContext
    protected static Context context;

    @Init
    static void init() {
        // Executed once for this service
    }

    public RefreshProfilesListEvent refreshProfiles() {
        ArrayList<IProfile> listProfiles = new ArrayList<>();
        AccountManager manager = new AccountManager(context);
        ArrayList<Account> accounts = manager.getAllAccounts();

        Instagram instagram =  new Instagram(Configuration.CLIENT_ID);
        for (int i = 0; i < accounts.size(); i++) {
            try {
                Token token = new Token(accounts.get(i).getToken(), Configuration.CLIENT_ID);
                instagram.setAccessToken(token);
                UserInfo userInfo = instagram.getCurrentUserInfo();
                UserInfoData userInfoData = userInfo.getData();

                Drawable profileImage = drawableFromUrl(userInfoData.getProfile_picture());
                listProfiles.add(new ProfileDrawerItem()
                        //.withEmail(session.getUsername())
                        .withName(userInfoData.getUsername())
                        .withIcon(profileImage));
            } catch (InstagramException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (listProfiles != null && listProfiles.size() > 0) {
            Log.i("DBG", "listprofiles != null or > 0");
            return new RefreshProfilesListEvent(listProfiles);
        } else {
            return null;
        }
    }

    public FinishLoginActivityEvent auth(String accessToken) {
        Instagram instagram = new Instagram(Configuration.CLIENT_ID);
        Token token = new Token(accessToken, Configuration.CLIENT_ID);
        instagram.setAccessToken(token);
        UserInfo userInfo = null;
        try {
            userInfo = instagram.getCurrentUserInfo();
        } catch (InstagramException e) {
            e.printStackTrace();
        }

        UserInfoData infoData = userInfo.getData();
        AccountManager manager = new AccountManager(context);
        Account account = new Account(infoData.getId());
        account.setToken(accessToken);
        account.setUsername(infoData.getUsername());
        if (manager.containe(account) != true) {
            manager.add(account);
            displayMessage("Аккаунт добавлен!");
        } else
            displayMessage("Такой аккаунт уже есть!");
        return new FinishLoginActivityEvent();
    }

    @Ui
    protected void displayMessage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    private Drawable drawableFromUrl(String url) throws IOException {
        Bitmap x;

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.connect();
        InputStream input = connection.getInputStream();

        x = BitmapFactory.decodeStream(input);
        return new BitmapDrawable(x);
    }
}
