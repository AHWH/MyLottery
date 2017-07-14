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
import sg.reddotdev.sharkfin.manager.impl.TotoRetrievalManager;


public class TotoActivity extends AppCompatActivity implements ResultRetrievalManager.ResultRetrievalManagerListener {
    private ResultRetrievalManager totoRetrievalManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*Through a event listener, it will pass out something regarding its status*/
        totoRetrievalManager = new TotoRetrievalManager();
        totoRetrievalManager.createRequest();

        totoRetrievalManager.registerListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        totoRetrievalManager.retrieve();
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
        totoRetrievalManager.unregisterListener();
        super.onDestroy();
    }

    @Override
    public void onSuccessfulRetrievedResult() {

    }

    @Override
    public void onFailureRetrievedResult() {

    }
}
