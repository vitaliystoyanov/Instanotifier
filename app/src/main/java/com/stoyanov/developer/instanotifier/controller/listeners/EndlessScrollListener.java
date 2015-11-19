package com.stoyanov.developer.instanotifier.controller.listeners;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

public class EndlessScrollListener extends RecyclerView.OnScrollListener {

    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private int visibleThreshold = 5;
    private boolean loading = true;
    private int previousTotal = 0;
    private int firstVisibleItem;
    private int visibleItemCount;
    private int totalItemCount;

    public EndlessScrollListener(RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager) {
        this.recyclerView = recyclerView;
        if (layoutManager instanceof LinearLayoutManager) {
            this.layoutManager = (LinearLayoutManager) layoutManager;
        } else {
            throw new ClassCastException();
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = layoutManager.getItemCount();
        firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            // End has been reached

            Log.i("DBG", "End called!");

            // Do something

            loading = true;
        }
    }
}
