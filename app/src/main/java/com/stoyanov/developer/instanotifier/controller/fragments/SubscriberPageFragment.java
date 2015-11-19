package com.stoyanov.developer.instanotifier.controller.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.joanzapata.android.asyncservice.api.annotation.InjectService;
import com.joanzapata.android.asyncservice.api.annotation.OnMessage;
import com.joanzapata.android.asyncservice.api.internal.AsyncService;
import com.stoyanov.developer.instanotifier.R;
import com.stoyanov.developer.instanotifier.controller.adapters.SubscriberRecycleViewAdapter;
import com.stoyanov.developer.instanotifier.model.serviceevents.LoadSubscribersEvent;
import com.stoyanov.developer.instanotifier.model.services.SubscriberService;

public class SubscriberPageFragment extends Fragment {

    private CircularProgressView progressView;
    private SwipeRefreshLayout refreshLayout;

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private SubscriberRecycleViewAdapter adapter;

    @InjectService
    public SubscriberService subscriberService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_page_subscribe, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        AsyncService.inject(this);
        adapter = new SubscriberRecycleViewAdapter();
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.subscriber_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        //recyclerView.addOnScrollListener(new EndlessScrollListener(recyclerView, layoutManager));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        progressView = (CircularProgressView) getActivity().findViewById(R.id.subscribe_progress_view);
        int colorProgressBar = getResources().getColor(R.color.primary_second_color);
        progressView.setColor(colorProgressBar);
        progressView.setVisibility(View.VISIBLE);
        progressView.startAnimation();
        subscriberService.getFollowsList();
    }

    @Override
    public void onStop() {
        AsyncService.unregister(this);
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @OnMessage(LoadSubscribersEvent.class)
    public void onEvent(LoadSubscribersEvent event) {
        progressView.setVisibility(View.INVISIBLE);
        adapter.addData(event.getList());
    }
}
