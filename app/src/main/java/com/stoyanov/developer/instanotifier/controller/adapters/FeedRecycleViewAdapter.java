package com.stoyanov.developer.instanotifier.controller.adapters;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.stoyanov.developer.instanotifier.R;
import com.stoyanov.developer.instanotifier.model.pojo.Post;
import com.stoyanov.developer.instanotifier.view.TimeTextView;

import java.util.ArrayList;
import java.util.List;

public class FeedRecycleViewAdapter extends RecyclerView.Adapter<FeedRecycleViewAdapter.ViewHolder> {

    private static final String TAG = "dbg";
    private static final int DURATION_MILLIS_ANIMATION = 500;
    private ArrayList<Post> dataSet;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView username;
        public TimeTextView datetime;
        public TextView caption;
        public TextView likes;
        public TextView comments;
        public ImageView image;

        public ViewHolder(View view) {
            super(view);
            username = (TextView) view.findViewById(R.id.feed_username);
            datetime = (TimeTextView) view.findViewById(R.id.feed_datetime);
            caption = (TextView) view.findViewById(R.id.feed_caption);
            comments = (TextView) view.findViewById(R.id.feed_comments);
            likes = (TextView) view.findViewById(R.id.feed_likes);
            image = (ImageView) view.findViewById(R.id.feed_image);
        }
    }

    {
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .resetViewBeforeLoading(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new FadeInBitmapDisplayer(DURATION_MILLIS_ANIMATION, true, true, true))
                .build();
    }

    public FeedRecycleViewAdapter() {
        this.dataSet = new ArrayList<>();
    }

    public void clearData() {
        int size = this.dataSet.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.dataSet.remove(0);
            }
            this.notifyItemRangeRemoved(0, size);
        }
    }

    public void addData(List<Post> posts) {
        int positionStart = dataSet.size() + 1;
        dataSet.addAll(posts);
        Log.i(TAG, "[FeedRecycleViewAdapter] method addData(): amount of posts - "  + dataSet.size());
        notifyItemRangeInserted(positionStart, posts.size() + 1);
    }

    @Override
    public FeedRecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Post post = dataSet.get(position);
        holder.username.setText(post.getUsername());
        holder.datetime.setTime(post.getDateAndTime());
        holder.caption.setText(String.valueOf(post.getCaption()));
        holder.comments.setText(String.valueOf(post.getComments()));
        holder.likes.setText(String.valueOf(post.getLikes()));
        imageLoader.displayImage(post.getPostImageURL(), holder.image, options);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
