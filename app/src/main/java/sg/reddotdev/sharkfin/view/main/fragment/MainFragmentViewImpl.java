/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.view.main.fragment;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sg.reddotdev.sharkfin.R;

public abstract class MainFragmentViewImpl implements MainFragmentViewMVP {
    private AppCompatActivity activity;
    private View view;

    private AppBarLayout appBarLayout;
    private Toolbar activityToolbar;
    private BottomNavigationView activityBottomNavigationView;
    private TextView activityNextDraw_textView;
    private TextView activityNextDrawDate_textView;
    private TextView activityNextDrawDay_textView;
    private RecyclerView resultsRecyclerView;

    private List<Object> lotteryResults;

    private MainFragmentViewMVPListener listener;

    public MainFragmentViewImpl(View view, AppCompatActivity activity) {
        this.activity = activity;
        this.view = view;
        lotteryResults = new ArrayList<>();
    }

    protected void init() {
        initAppBarLayout();
        initToolbar();
        initBottomBar();
        initNextDrawTextView();
        initNextDrawDateTextView();
        initNextDrawDayTextView();
        initRecyclerView();
    }

    private void initAppBarLayout() {
        appBarLayout = (AppBarLayout) activity.findViewById(R.id.appbar_layout);
    }

    private void initToolbar() {
        activityToolbar = (Toolbar) activity.findViewById(R.id.toolbar);
    }

    private void initBottomBar() {
        activityBottomNavigationView = (BottomNavigationView) activity.findViewById(R.id.bottom_navigation);
    }

    private void initNextDrawTextView() {
        activityNextDraw_textView = (TextView) activity.findViewById(R.id.nextDraw_textView);
    }

    private void initNextDrawDateTextView() {
        activityNextDrawDate_textView = (TextView) activity.findViewById(R.id.nextDraw_Date_textView);
    }

    private void initNextDrawDayTextView() {
        activityNextDrawDay_textView = (TextView) activity.findViewById(R.id.nextDraw_DayTime_textView);
    }

    private void initRecyclerView() {
        resultsRecyclerView = (RecyclerView) view.findViewById(R.id.results_list);
        resultsRecyclerView.setLayoutManager(new LinearLayoutManager(activity.getApplicationContext()));
        resultsRecyclerView.addItemDecoration(new DividerItemDecoration(activity.getApplicationContext(), DividerItemDecoration.VERTICAL));
    }


    /*Setup title and themes*/
    protected abstract void setupTheme();
    protected abstract void setupToolbar();
    protected abstract void setupBottomBar();

    protected abstract void setupNextDrawDate();

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    public AppCompatActivity getActivity() {
        return activity;
    }

    @Override
    public View getRootView() {
        return view;
    }

    @Override
    public Bundle getViewState() {
        return null;
    }

    protected AppBarLayout getAppBarLayout() {
        return appBarLayout;
    }

    protected Toolbar getActivityToolbar() {
        return activityToolbar;
    }

    protected BottomNavigationView getActivityBottomNavigationView() {
        return activityBottomNavigationView;
    }

    protected TextView getActivityNextDraw_textView() {
        return activityNextDraw_textView;
    }

    protected TextView getActivityNextDrawDate_textView() {
        return activityNextDrawDate_textView;
    }

    protected TextView getActivityNextDrawDay_textView() {
        return activityNextDrawDay_textView;
    }

    @Override
    public RecyclerView getResultsRecyclerView() {
        return resultsRecyclerView;
    }

    public List<Object> getLotteryResults() {
        return lotteryResults;
    }

    @Override
    public void registerListener(MainFragmentViewMVPListener listener) {
        this.listener = listener;
    }

    @Override
    public void unregisterListener() {
        listener = null;
    }

    protected MainFragmentViewMVPListener getListener() {
        return listener;
    }
}
