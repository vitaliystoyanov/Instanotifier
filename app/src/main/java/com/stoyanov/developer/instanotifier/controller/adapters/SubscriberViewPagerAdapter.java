package com.stoyanov.developer.instanotifier.controller.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.stoyanov.developer.instanotifier.controller.fragments.FollowsSubscriberPageFragment;
import java.util.List;

public class SubscriberViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<String> mList;
    FollowsSubscriberPageFragment fragment;

    public SubscriberViewPagerAdapter(FragmentManager fm, List<String> list) {
        super(fm);
        this.mList = list;
        fragment = new FollowsSubscriberPageFragment();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0 : {
                return fragment;
            }
            default: {
                return new Fragment();
            }
        }
    }

    @Override
    public int getCount() {
        return mList.size();
    }
}
