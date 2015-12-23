package com.stoyanov.developer.instanotifier.controller.adapters;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.stoyanov.developer.instanotifier.R;
import com.stoyanov.developer.instanotifier.model.pojo.Subscriber;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SubscriberRecycleViewAdapter extends RecyclerView.Adapter<SubscriberRecycleViewAdapter.ViewHolder> {

    private static final int DURATION_MILLIS_ANIMATION = 500;
    private ArrayList<Subscriber> dataSet;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView username;
        public TextView follows;
        public TextView followers;
        public CircleImageView image;

        public ViewHolder(View view) {
            super(view);
            username = (TextView) view.findViewById(R.id.subscriber_username);
            follows = (TextView) view.findViewById(R.id.subscriber_follows_user);
            followers = (TextView) view.findViewById(R.id.subscriber_followers_user);
            image = (CircleImageView) view.findViewById(R.id.subscriber_profile_image);
        }
    }

    {
        dataSet = new ArrayList<>();
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                //.cacheInMemory(true)
                //.cacheOnDisk(true)
                .resetViewBeforeLoading(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new FadeInBitmapDisplayer(DURATION_MILLIS_ANIMATION, true, true, true))
                .build();
    }

    public SubscriberRecycleViewAdapter() {
    }

    public SubscriberRecycleViewAdapter(ArrayList<Subscriber> dataSet) {
        this.dataSet = dataSet;
    }

    public void clearData() {
        int size = dataSet.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.dataSet.remove(0);
            }
            this.notifyItemRangeRemoved(0, size);
        }
    }

    public void addData(List<Subscriber> subscriberList) {
        dataSet.addAll(subscriberList);
        this.notifyItemRangeInserted(0, subscriberList.size() - 1);
    }

    @Override
    public SubscriberRecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_subscribe, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Subscriber subscriber = dataSet.get(position);
        holder.username.setText(subscriber.getUsername());
        //holder.follows.setText(String.valueOf(subscriber.getCountFollows()));
        //holder.followers.setText(String.valueOf(subscriber.getCountFollowers()));
        imageLoader.displayImage(subscriber.getProfileImageURL(), holder.image, options);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
