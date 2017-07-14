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
import sg.reddotdev.sharkfin.manager.impl.BigSweepRetrievalManager;


public class BigSweepActivity extends AppCompatActivity implements ResultRetrievalManager.ResultRetrievalManagerListener {
    private ResultRetrievalManager bigSweepResultRetrievalManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bigSweepResultRetrievalManager = new BigSweepRetrievalManager();
        bigSweepResultRetrievalManager.createRequest();

        bigSweepResultRetrievalManager.registerListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bigSweepResultRetrievalManager.retrieve();
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
        bigSweepResultRetrievalManager.unregisterListener();
        super.onDestroy();
    }

    @Override
    public void onSuccessfulRetrievedResult() {

    }

    @Override
    public void onFailureRetrievedResult() {

    }
}


