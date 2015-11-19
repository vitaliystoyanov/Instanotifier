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
import com.stoyanov.developer.instanotifier.controller.adapters.FeedRecycleViewAdapter;
import com.stoyanov.developer.instanotifier.controller.listeners.EndlessScrollListener;
import com.stoyanov.developer.instanotifier.model.serviceevents.LoadFeedPostsEvent;
import com.stoyanov.developer.instanotifier.model.services.FeedService;

/**
 * @author Vitaliy Stoyanov <developer.stoyanov@gmail.com>
 */
public class FeedFragment extends Fragment {

    private LinearLayoutManager layoutManager;
    private CircularProgressView progressView;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private FeedRecycleViewAdapter feedAdapter;

    @InjectService
    public FeedService feedService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        AsyncService.inject(this);
        feedAdapter = new FeedRecycleViewAdapter();
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.feed_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.addOnScrollListener(new EndlessScrollListener(recyclerView, layoutManager));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(feedAdapter);

        refreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_layout);
        //refreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                feedService.getPosts();
            }
        });
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                refreshLayout.setEnabled(layoutManager.findFirstCompletelyVisibleItemPosition() == 0);
            }
        });

        progressView = (CircularProgressView) getActivity().findViewById(R.id.feed_progress_view);
        int colorProgressBar = getResources().getColor(R.color.primary_second_color);
        progressView.setColor(colorProgressBar);
        progressView.setVisibility(View.VISIBLE);
        progressView.startAnimation();
        feedService.getPosts();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        AsyncService.unregister(this);
    }

    @OnMessage(LoadFeedPostsEvent.class)
    public void onGetFeedPostsEvent(LoadFeedPostsEvent event) {
        progressView.setVisibility(View.INVISIBLE);
        refreshLayout.setRefreshing(false);
        feedAdapter.clearData();
        feedAdapter.addData(event.getList());
    }
}
