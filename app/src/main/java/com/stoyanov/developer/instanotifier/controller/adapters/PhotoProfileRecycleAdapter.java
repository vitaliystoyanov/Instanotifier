package com.stoyanov.developer.instanotifier.controller.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.stoyanov.developer.instanotifier.R;
import com.stoyanov.developer.instanotifier.model.pojo.Photo;
import com.stoyanov.developer.instanotifier.model.utills.ScreenUtils;
import com.stoyanov.developer.instanotifier.view.SquaredFrameLayout;

import java.util.ArrayList;
import java.util.List;

public class PhotoProfileRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int PHOTO_ANIMATION_DELAY = 600;
    private static final Interpolator INTERPOLATOR = new DecelerateInterpolator();
    private int cellSize;

    private List<Photo> photos;
    private Context context;
    private boolean lockedAnimations = false;
    private int lastAnimatedItem = -1;
    private final ImageLoader imageLoader;
    private DisplayImageOptions options;


    public PhotoProfileRecycleAdapter(Context context) {
        this.context = context;
        photos = new ArrayList<>();
        cellSize = ScreenUtils.getScreenWidth(context)/3;
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                //.cacheInMemory(true)
                //.cacheOnDisk(true)
                .resetViewBeforeLoading(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    public void clearData() {
        int size = this.photos.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.photos.remove(0);
            }
            this.notifyItemRangeRemoved(0, size);
        }
    }

    public void addData(List<Photo> photos) {
        this.photos.addAll(photos);
        this.notifyItemRangeInserted(0, photos.size() - 1);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.item_photo_profile, parent, false);
        StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
        layoutParams.height = cellSize;
        layoutParams.width = cellSize;
        layoutParams.setFullSpan(false);
        view.setLayoutParams(layoutParams);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindPhoto((PhotoViewHolder) holder, position);
    }

    private void bindPhoto(final PhotoViewHolder holder, int position) {
        imageLoader.displayImage(photos.get(position).getURL(), holder.photo, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                animatePhoto(holder);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
        if (lastAnimatedItem < position) lastAnimatedItem = position;
    }

    private void animatePhoto(PhotoViewHolder viewHolder) {
        if (!lockedAnimations) {
            if (lastAnimatedItem == viewHolder.getPosition()) {
                setLockedAnimations(true);
            }

            long animationDelay = PHOTO_ANIMATION_DELAY + viewHolder.getPosition() * 30;

            viewHolder.root.setScaleY(0);
            viewHolder.root.setScaleX(0);

            viewHolder.root.animate()
                    .scaleY(1)
                    .scaleX(1)
                    .setDuration(200)
                    .setInterpolator(INTERPOLATOR)
                    .setStartDelay(animationDelay)
                    .start();
        }
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {

        public FrameLayout root;
        public ImageView photo;

        public PhotoViewHolder(View view) {
            super(view);
            root = (SquaredFrameLayout) view.findViewById(R.id.profile_item_squared_root);
            photo = (ImageView) view.findViewById(R.id.profile_item_grid_imageview);
        }
    }

    public void setLockedAnimations(boolean lockedAnimations) {
        this.lockedAnimations = lockedAnimations;
    }
}
