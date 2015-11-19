package com.stoyanov.developer.instanotifier.controller.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.stoyanov.developer.instanotifier.controller.fragments.SubscriberPageFragment;

import java.util.ArrayList;
import java.util.List;

public class SubscriberViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragments;

    public SubscriberViewPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
