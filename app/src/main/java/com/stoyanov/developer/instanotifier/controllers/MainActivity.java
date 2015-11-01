package com.stoyanov.developer.instanotifier.controllers;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
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
import com.stoyanov.developer.instanotifier.model.multiaccounts.AccountManager;
import com.stoyanov.developer.instanotifier.model.events.RefreshProfilesListEvent;
import com.stoyanov.developer.instanotifier.model.pojo.Account;
import com.stoyanov.developer.instanotifier.model.services.Service;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int BUTTON_ADD_ACCOUNT = 8;
    private static final int BUTTON_LOGOUT = 9;
    private FragmentManager fragmentManager;
    private final static String TAG_FRAGMENT_LOGIN = "FRAGMENT_LOGIN";
    private Drawer navigationDrawer;
    private AccountHeader headerNavigationDrawer;

    @InjectService
    public Service service;

    @Override
    protected void onResume() {
        super.onResume();
        AccountManager manager = new AccountManager(getApplicationContext());
        if (manager.getAllAccounts().size() == 0)
            startLoginActivity();
        else
            service.refreshProfiles();
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

        final AccountManager manager =new AccountManager(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

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
                        .withIdentifier(3)
                        .withIcon(new IconicsDrawable(MainActivity.this)
                                .icon(FontAwesome.Icon.faw_map_marker)
                                .color(Color.BLACK)
                                .paddingDp(4))
                        , new PrimaryDrawerItem()
                        .withName(R.string.drawer_item_activity)
                        .withIdentifier(3)
                        .withIcon(new IconicsDrawable(MainActivity.this)
                                .icon(FontAwesome.Icon.faw_commenting)
                                .color(Color.BLACK)
                                .paddingDp(4))
                        , new PrimaryDrawerItem()
                        .withName(R.string.drawer_item_follow)
                        .withIdentifier(3)
                        .withIcon(new IconicsDrawable(MainActivity.this)
                                .icon(FontAwesome.Icon.faw_user_plus)
                                .color(Color.BLACK)
                                .paddingDp(4))
                        , new PrimaryDrawerItem()
                        .withName(R.string.drawer_item_notification)
                        .withIdentifier(4)
                        .withIcon(new IconicsDrawable(MainActivity.this)
                                .icon(FontAwesome.Icon.faw_bell)
                                .color(Color.BLACK)
                                .paddingDp(4))
                        , new PrimaryDrawerItem()
                        .withName(R.string.drawer_item_static)
                        .withIdentifier(3)
                        .withIcon(new IconicsDrawable(MainActivity.this)
                                .icon(FontAwesome.Icon.faw_bar_chart)
                                .color(Color.BLACK)
                                .paddingDp(4))
                        , new DividerDrawerItem()
                        .withIdentifier(5)
                        , new PrimaryDrawerItem()
                        .withName("Настройки")
                        .withIdentifier(6)
                        .withIcon(new IconicsDrawable(MainActivity.this)
                                .icon(FontAwesome.Icon.faw_cog)
                                .color(Color.BLACK)
                                .paddingDp(4))
                        , new PrimaryDrawerItem()
                        .withName("О приложении")
                        .withIdentifier(6))
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch (drawerItem.getIdentifier()) {
                            case 6: {
                                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                                startActivity(intent);
                            }
                            break;
                        }

                        return false;
                    }
                })
                .build();

        /*
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            FeedFragment feedFragment = new FeedFragment();

            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();
            fragmentTransaction.add(R.id.container, feedFragment, TAG_FRAGMENT_LOGIN);
            fragmentTransaction.commit();
        }
        */

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
        .build();
        ImageLoader.getInstance().init(config);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AsyncService.unregister(this);
    }

    private AccountHeader createAccountsHeader() {
        final AccountManager manager =
                new AccountManager(getApplicationContext());
        final View content = findViewById(R.id.coordinator);
        return new AccountHeaderBuilder()
                    .withActivity(this)
                    .withCompactStyle(true)
                    .withHeaderBackground(R.drawable.back)
                    .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                        @Override
                        public boolean onProfileChanged(View view, IProfile iProfile, boolean b) {
                            switch (iProfile.getIdentifier()) {
                                case BUTTON_ADD_ACCOUNT: {
                                    if (manager.isAvaliableAdd())
                                        startLoginActivity();
                                    else
                                    Snackbar.make(content,
                                            "Ей ей порень, много аккаунтов!",
                                            Snackbar.LENGTH_SHORT).show();
                                    return false;
                                }
                                case BUTTON_LOGOUT: {
                                    String username = headerNavigationDrawer.getActiveProfile().getEmail().toString();
                                    Log.i("DBG", "Username changed on - " + username);
/*                                    if (username != null) {
                                        final Account account = manager.getAccountByUsername(username);
                                        new MaterialDialog.Builder(MainActivity.this)
                                                .theme(Theme.LIGHT)
                                                .content("Вы уверены что хотите выйти?")
                                                .callback(new MaterialDialog.ButtonCallback() {

                                                    @Override
                                                    public void onPositive(MaterialDialog dialog) {
                                                        super.onPositive(dialog);
                                                        if (account != null) {
                                                            manager.removeAccount(account);
                                                            service.refreshProfiles();
                                                            Snackbar.make(content,
                                                                    "Вышел!",
                                                                    Snackbar.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                })
                                                .positiveText("Выйти")
                                                .negativeText("Отменить")
                                                .show();
                                    }*/
                                }
                                return false;
                            }
                            return true;
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

    @OnMessage(RefreshProfilesListEvent.class)
    public void onRefreshProfiles(RefreshProfilesListEvent list) {
        ArrayList<IProfile> profiles;
        profiles = list.getProfiles();
        if (profiles != null && profiles.size() > 0) {
            profiles.add(new ProfileSettingDrawerItem()
                            .withName(getString(R.string.drawer_add_profile))
                            .withIdentifier(8)
                            .withIcon(new IconicsDrawable(MainActivity.this)
                                    .icon(FontAwesome.Icon.faw_plus)
                                    .color(Color.BLACK)
                                    .paddingDp(5))
            );
            profiles.add(new ProfileSettingDrawerItem()
                            .withName(getString(R.string.drawer_logout))
                            .withIdentifier(9)
                            .withIcon(new IconicsDrawable(MainActivity.this)
                                    .icon(FontAwesome.Icon.faw_sign_out)
                                    .color(Color.BLACK)
                                    .paddingDp(5))
            );
            headerNavigationDrawer.setProfiles(profiles);
        } else {
            profiles.add(new ProfileSettingDrawerItem()
                            .withName(getString(R.string.drawer_add_profile))
                            .withIdentifier(8)
                            .withIcon(new IconicsDrawable(MainActivity.this)
                                    .icon(FontAwesome.Icon.faw_plus)
                                    .color(Color.BLACK)
                                    .paddingDp(5))
            );
            headerNavigationDrawer.setProfiles(profiles);
        }
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
