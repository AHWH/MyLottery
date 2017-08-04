/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.view.result;

import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import sg.reddotdev.sharkfin.R;

/**
 * Created by weihong on 4/8/17.
 */

public class FourDResultView extends ResultViewImpl {
    public FourDResultView(LayoutInflater inflater, ViewGroup container, AppCompatActivity appCompatActivity) {
        super(inflater.inflate(R.layout.result_fourd_layout, container), appCompatActivity);

        setupToolbar();
    }

    protected void setupToolbar() {
        getToolbar().setTitle("4D Result");
    }
}
