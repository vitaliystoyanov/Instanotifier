package com.stoyanov.developer.instanotifier.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import com.joanzapata.android.asyncservice.api.annotation.InjectService;
import com.joanzapata.android.asyncservice.api.annotation.OnMessage;
import com.joanzapata.android.asyncservice.api.internal.AsyncService;
import com.stoyanov.developer.instanotifier.R;
import com.stoyanov.developer.instanotifier.controller.adapters.PhotoProfileRecycleAdapter;
import com.stoyanov.developer.instanotifier.model.serviceevents.GetPhotoProfileEvent;
import com.stoyanov.developer.instanotifier.model.services.ProfileService;

public class ProfileActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PhotoProfileRecycleAdapter profilePhotoAdapter;

    @InjectService
    public ProfileService profileService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        AsyncService.inject(this);
        recyclerView = (RecyclerView) findViewById(R.id.profile_recycleview);
        profilePhotoAdapter = new PhotoProfileRecycleAdapter(getApplicationContext());
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(profilePhotoAdapter);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                profilePhotoAdapter.setLockedAnimations(true);
            }
        });
        profileService.getUserPhotos();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        AsyncService.unregister(this);
    }

    @OnMessage(GetPhotoProfileEvent.class)
    public void onEvent(GetPhotoProfileEvent event) {
        profilePhotoAdapter.addData(event.getPhotos());
        Log.i("DBG", "Count photos = " + event.getPhotos().size());
    }
}
