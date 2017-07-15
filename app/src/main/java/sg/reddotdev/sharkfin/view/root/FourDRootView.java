/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.view.root;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ToolbarWidgetWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import sg.reddotdev.sharkfin.R;
import sg.reddotdev.sharkfin.view.ViewMVP;

/**
 * Created by weihong on 15/7/17.
 */

public class FourDRootView implements ViewMVP {
    private View rootView;
    private AppCompatActivity mActivity;

    public FourDRootView(LayoutInflater inflater, ViewGroup container, AppCompatActivity activity) {
        rootView = inflater.inflate(R.layout.activity_main, container);
        initialise();

    }

    @Override
    public View getRootView() {
        return rootView;
    }

    @Override
    public Bundle getViewState() {
        return null;
    }

    private void initialise() {
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) rootView.findViewById(R.id.toolbar);
        if(toolbar != null) {
            mActivity.setSupportActionBar(toolbar);

        }
    }
}
