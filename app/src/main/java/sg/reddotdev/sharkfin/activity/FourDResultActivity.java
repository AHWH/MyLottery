/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import sg.reddotdev.sharkfin.view.result.FourDResultView;
import sg.reddotdev.sharkfin.view.result.ResultViewMVP;

/**
 * Created by weihong on 4/8/17.
 */

public class FourDResultActivity extends AppCompatActivity implements ResultViewMVP.ResultViewMVPListener {
    private String LOGTAG = getClass().getSimpleName();

    private FourDResultView viewMVP;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewMVP = new FourDResultView(LayoutInflater.from(this), null, this);
        viewMVP.registerListener(this);
        setContentView(viewMVP.getRootView());
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        viewMVP.unregisterListener();
        super.onDestroy();
    }
}
