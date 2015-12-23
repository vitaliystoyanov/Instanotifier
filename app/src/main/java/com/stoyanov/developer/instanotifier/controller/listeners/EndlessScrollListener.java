package com.stoyanov.developer.instanotifier.controller.listeners;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

public abstract class EndlessScrollListener extends RecyclerView.OnScrollListener {

    private static final String TAG = "dbg";
    private LinearLayoutManager layoutManager;
    private int visibleItemCount;
    private int totalItemCount;
    private int pastVisiblesItems;
    private int lastItems;
    private boolean loading = true;

    public EndlessScrollListener(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof LinearLayoutManager) {
            this.layoutManager = (LinearLayoutManager) layoutManager;
        } else {
            throw new ClassCastException();
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (dy > 0) {

            visibleItemCount = layoutManager.getChildCount();
            totalItemCount = layoutManager.getItemCount();
            pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

            if (totalItemCount > lastItems) {
                lastItems = totalItemCount;
                loading = true;
            }

            if (loading) {
                if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                    loading = false;
                    Log.i(TAG, "onScrolled: visibleItemCount - " + visibleItemCount
                            + ", totalItemCount - " + totalItemCount
                            + ", pastVisiblesItems - " + pastVisiblesItems);
                    loadNextItems();
                }
            }
        }
    }

    public abstract void loadNextItems();
}
