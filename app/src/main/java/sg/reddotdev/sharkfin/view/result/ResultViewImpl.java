/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.view.result;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import sg.reddotdev.sharkfin.R;

/**
 * Created by weihong on 4/8/17.
 */

public abstract class ResultViewImpl implements ResultViewMVP {
    private View view;
    private AppCompatActivity appCompatActivity;

    private Toolbar toolbar;

    private ResultViewMVPListener listener;

    public ResultViewImpl(View view, AppCompatActivity appCompatActivity) {
        this.view = view;
        this.appCompatActivity = appCompatActivity;

        setToolbar();
    }

    protected abstract void setupTheme();
    protected abstract void setupRecyclerViews();


    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public View getRootView() {
        return view;
    }

    @Override
    public Bundle getViewState() {
        return null;
    }

    @Override
    public void registerListener(ResultViewMVPListener listener) {
        this.listener = listener;
    }

    @Override
    public void unregisterListener() {
        listener = null;
    }


    private void setToolbar() {
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    }

    protected void initToolbar() {
        if(toolbar != null) {
            appCompatActivity.setSupportActionBar(toolbar);
            appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            appCompatActivity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }
}
