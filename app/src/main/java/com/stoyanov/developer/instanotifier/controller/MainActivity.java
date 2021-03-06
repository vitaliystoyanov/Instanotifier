package com.stoyanov.developer.instanotifier.controller;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.joanzapata.android.asyncservice.api.annotation.InjectService;
import com.joanzapata.android.asyncservice.api.annotation.OnMessage;
import com.joanzapata.android.asyncservice.api.internal.AsyncService;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.stoyanov.developer.instanotifier.R;
import com.stoyanov.developer.instanotifier.controller.fragments.FeedFragment;
import com.stoyanov.developer.instanotifier.controller.fragments.SubscriberFragment;
import com.stoyanov.developer.instanotifier.model.multipleaccounts.OnChangeAccountListener;
import com.stoyanov.developer.instanotifier.model.multipleaccounts.AccountManager;
import com.stoyanov.developer.instanotifier.model.serviceevents.RefreshProfilesEvent;
import com.stoyanov.developer.instanotifier.model.pojo.Account;
import com.stoyanov.developer.instanotifier.model.serviceevents.ShowSnackbarEvent;
import com.stoyanov.developer.instanotifier.model.services.AuthorizationService;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final static String TAG_FRAGMENT_FEED = "FRAGMENT_FEED";
    private final static String TAG_FRAGMENT_PROFILE = "FRAGMENT_PROFILE";
    private final static String TAG_FRAGMENT_FOLLOW = "FRAGMENT_FOLLOW";
    private static final int BUTTON_ADD_ACCOUNT = 8;
    private static final int BUTTON_LOGOUT = 9;
    private AccountHeader headerNavigationDrawer;
    private Drawer navigationDrawer;
    private FragmentManager fragmentManager;
    private FeedFragment feedFragment;
    private SubscriberFragment subscriberFragment;
    private View content;
    private AccountManager manager;

    @InjectService
    public AuthorizationService authorizationService;

    @Override
    protected void onResume() {
        super.onResume();
        AccountManager manager = new AccountManager(getApplicationContext());
        if (manager.getCount() == 0) {
            startLoginActivity();
        }
        else {
            authorizationService.refreshProfiles();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        AsyncService.inject(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if (navigationDrawer != null && navigationDrawer.isDrawerOpen())
            navigationDrawer.closeDrawer();
        else
            super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        content = findViewById(R.id.coordinator);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);
        fragmentManager = getSupportFragmentManager();
        feedFragment = new FeedFragment();
        subscriberFragment = new SubscriberFragment();
        manager = new AccountManager(getApplicationContext());
        manager.setOnChangeAccountListener(new OnChangeAccountListener() {
            @Override
            public void onChangeAccount(Account oldAccount, Account newAccount) {
                makeFragmentTransaction(feedFragment, TAG_FRAGMENT_FEED);
            }
        });
        headerNavigationDrawer = createAccountsHeader();
        navigationDrawer =  new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withDisplayBelowStatusBar(true)
                .withAccountHeader(headerNavigationDrawer)
                .addDrawerItems(new PrimaryDrawerItem()
                        .withName(R.string.drawer_item_feed)
                        .withIdentifier(2)
                        .withIcon(new IconicsDrawable(MainActivity.this)
                                .icon(FontAwesome.Icon.faw_home)
                                .color(Color.BLACK)
                                .paddingDp(4))
                        , new PrimaryDrawerItem()
                        .withName(R.string.drawer_item_profile)
                        .withIdentifier(3)
                        .withIcon(new IconicsDrawable(MainActivity.this)
                                .icon(FontAwesome.Icon.faw_user)
                                .color(Color.BLACK)
                                .paddingDp(4))
                        , new PrimaryDrawerItem()
                        .withName(R.string.drawer_item_nearby)
                        .withIdentifier(4)
                        .withIcon(new IconicsDrawable(MainActivity.this)
                                .icon(FontAwesome.Icon.faw_map_marker)
                                .color(Color.BLACK)
                                .paddingDp(4))
                        , new PrimaryDrawerItem()
                        .withName(R.string.drawer_item_activity)
                        .withIdentifier(5)
                        .withIcon(new IconicsDrawable(MainActivity.this)
                                .icon(FontAwesome.Icon.faw_commenting)
                                .color(Color.BLACK)
                                .paddingDp(4))
                        , new PrimaryDrawerItem()
                        .withName(R.string.drawer_item_follow)
                        .withIdentifier(6)
                        .withIcon(new IconicsDrawable(MainActivity.this)
                                .icon(FontAwesome.Icon.faw_user_plus)
                                .color(Color.BLACK)
                                .paddingDp(4))
                        , new PrimaryDrawerItem()
                        .withName(R.string.drawer_item_notification)
                        .withIdentifier(8)
                        .withIcon(new IconicsDrawable(MainActivity.this)
                                .icon(FontAwesome.Icon.faw_bell)
                                .color(Color.BLACK)
                                .paddingDp(4))
                        , new PrimaryDrawerItem()
                        .withName(R.string.drawer_item_static)
                        .withIdentifier(9)
                        .withIcon(new IconicsDrawable(MainActivity.this)
                                .icon(FontAwesome.Icon.faw_bar_chart)
                                .color(Color.BLACK)
                                .paddingDp(4))
                        , new DividerDrawerItem()
                        .withIdentifier(5)
                        , new PrimaryDrawerItem()
                        .withName("Настройки")
                        .withIdentifier(10)
                        .withIcon(new IconicsDrawable(MainActivity.this)
                                .icon(FontAwesome.Icon.faw_cog)
                                .color(Color.BLACK)
                                .paddingDp(4))
                        , new PrimaryDrawerItem()
                        .withName("О приложении")
                        .withIdentifier(11))
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        ActionBar actionBar = getSupportActionBar();

                        switch (drawerItem.getIdentifier()) {
                            case 2: {
                                actionBar.setTitle(R.string.drawer_item_feed);
                                makeFragmentTransaction(feedFragment, TAG_FRAGMENT_FEED);
                                break;
                            }
                            case 3: {
                                startProfileActivity();
                                break;
                            }
                            case 6: {
                                makeFragmentTransaction(subscriberFragment, TAG_FRAGMENT_FOLLOW);
                                break;
                            }
                        }
                        return false;
                    }
                })
                .build();
        if (savedInstanceState == null) {
            makeFragmentTransaction(feedFragment, TAG_FRAGMENT_FEED);
        }
    }

    private void makeFragmentTransaction(Fragment fragment, String tag) {
        if (fragment != null && tag != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, fragment, tag);
            fragmentTransaction.commit();
        } else {
            throw new NullPointerException();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AsyncService.unregister(this);
    }

    private AccountHeader createAccountsHeader() {
        return new AccountHeaderBuilder()
                    .withActivity(this)
                    .withCompactStyle(true)
                    .withHeaderBackground(R.drawable.back)
                    .withResetDrawerOnProfileListClick(true)
                    .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {

                        @Override
                        public boolean onProfileChanged(View view, IProfile iProfile, boolean b) {
                            switch (iProfile.getIdentifier()) {
                                case BUTTON_ADD_ACCOUNT: {
                                    if (manager.isAvailableAdd())
                                        startLoginActivity();
                                    else
                                    Snackbar.make(content,
                                            "Ей ей порень, много аккаунтов!",
                                            Snackbar.LENGTH_SHORT).show();
                                    return false;
                                }
                                case BUTTON_LOGOUT: {
                                    final Account account = manager.getCurrent();
                                    if (account != null) {
                                        new MaterialDialog.Builder(MainActivity.this)
                                                .theme(Theme.LIGHT)
                                                .content("Вы уверены что хотите выйти?")
                                                .callback(new MaterialDialog.ButtonCallback() {
                                                    @Override
                                                    public void onPositive(MaterialDialog dialog) {
                                                        super.onPositive(dialog);
                                                        if (account != null) {
                                                            manager.remove(account);
                                                            if (manager.getCount() == 0) {
                                                                startLoginActivity();
                                                            }
                                                            authorizationService.refreshProfiles();
                                                            Snackbar.make(content,
                                                                    "Вышел!",
                                                                    Snackbar.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                })
                                                .positiveText("Выйти")
                                                .negativeText("Отменить")
                                                .show();
                                    }
                                    return false;
                                }
                                default: {
                                    Log.i("DBG", "[MainActivity]User changed at - " + iProfile.getName().getText());
                                    manager.setCurrent(manager.find(iProfile.getName().getText()));
                                    return true;
                                }
                            }
                        }
                    })
                    .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @OnMessage(ShowSnackbarEvent.class)
    public void onSnackShowEvent(ShowSnackbarEvent event) {
        Snackbar.make(content, event.getText(), Snackbar.LENGTH_SHORT).show();
    }

    @OnMessage(RefreshProfilesEvent.class)
    public void onRefreshProfiles(RefreshProfilesEvent list) {
        ArrayList<IProfile> profiles = list.getProfiles();
        ProfileSettingDrawerItem itemAdd = new ProfileSettingDrawerItem()
                .withName(getString(R.string.drawer_add_profile))
                .withIdentifier(8)
                .withIcon(new IconicsDrawable(MainActivity.this)
                        .icon(FontAwesome.Icon.faw_plus)
                        .color(Color.BLACK)
                        .paddingDp(5));
        ProfileSettingDrawerItem itemLogout = new ProfileSettingDrawerItem()
                .withName(getString(R.string.drawer_logout))
                .withIdentifier(9)
                .withIcon(new IconicsDrawable(MainActivity.this)
                        .icon(FontAwesome.Icon.faw_sign_out)
                        .color(Color.BLACK)
                        .paddingDp(5));
        if (profiles.size() > 0) {
            if (!profiles.contains(itemAdd) && !profiles.contains(itemLogout)) {
                profiles.add(itemAdd);
                profiles.add(itemLogout);
            }
        } else {
            profiles.add(itemAdd);
        }
        headerNavigationDrawer.setProfiles(profiles);
        setCurrentProfile(profiles);
    }

    private void setCurrentProfile(ArrayList<IProfile> profiles) {
        ArrayList<IProfile> listProfiles = profiles;
        Account currentAccount = manager.getCurrent();
        for (IProfile profile : listProfiles) {
            if (currentAccount.getUsername().equals(profile.getName().toString())) {
                Log.i("DBG", "[MainActivity] Set headerNavigation the account = " + profile.getName());
                headerNavigationDrawer.setActiveProfile(profile);
                break;
            }
        }
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void startProfileActivity() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
}
