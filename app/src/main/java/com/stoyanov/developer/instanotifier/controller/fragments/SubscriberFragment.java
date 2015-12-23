package com.stoyanov.developer.instanotifier.controller.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.fafaldo.fabtoolbar.widget.FABToolbarLayout;
import com.stoyanov.developer.instanotifier.R;
import com.stoyanov.developer.instanotifier.controller.adapters.SubscriberViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

public class SubscriberFragment extends Fragment implements MaterialTabListener {

    private FABToolbarLayout layout;
    private Toolbar toolbar;
    private MaterialTabHost tabHost;
    private ViewPager pager;
    private SubscriberViewPagerAdapter adapter;
    private List<String> tabsListstringss;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_subscribe, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setElevation(0);

        layout = (FABToolbarLayout) getActivity().findViewById(R.id.fabtoolbar);

        tabsListstringss = new ArrayList<>();
        tabsListstringss.add("Follows");
        tabsListstringss.add("Followers");
        tabsListstringss.add("Non follows");

        pager = (ViewPager) getActivity().findViewById(R.id.viewpager);
        adapter = new SubscriberViewPagerAdapter(getActivity().getSupportFragmentManager(), tabsListstringss);
        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                tabHost.setSelectedNavigationItem(position);
            }
        });

        pager.setCurrentItem(0);
        tabHost = (MaterialTabHost) getActivity().findViewById(R.id.tabHost);
        tabHost.addTab(
                tabHost.newTab()
                        .setText("Follows")
                        .setTabListener(this)
        );
        tabHost.addTab(
                tabHost.newTab()
                        .setText("Followers")
                        .setTabListener(this)
        );
        tabHost.addTab(
                tabHost.newTab()
                        .setText("Non-back follows")
                        .setTabListener(this)
        );
        pager.setCurrentItem(0);
    }

    @Override
    public void onPause() {
        super.onPause();
        toolbar.setElevation(4);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onTabSelected(MaterialTab tab) {
        pager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab tab) {
    }

    @Override
    public void onTabUnselected(MaterialTab tab) {
    }
}
