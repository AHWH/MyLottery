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

import sg.reddotdev.sharkfin.R;
import sg.reddotdev.sharkfin.manager.base.ResultRetrievalManager;
import sg.reddotdev.sharkfin.manager.impl.FourDRetrievalManager;

public class FourDActivity extends AppCompatActivity implements ResultRetrievalManager.ResultRetrievalManagerListener{
    private ResultRetrievalManager fourDRetrievalManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fourDRetrievalManager = new FourDRetrievalManager();
        fourDRetrievalManager.createRequest();

        fourDRetrievalManager.registerListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fourDRetrievalManager.retrieve();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        fourDRetrievalManager.unregisterListener();
        super.onDestroy();

    }

    @Override
    public void onSuccessfulRetrievedResult() {

    }

    @Override
    public void onFailureRetrievedResult() {

    }
}
