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

import sg.reddotdev.sharkfin.R;
import sg.reddotdev.sharkfin.data.model.LotteryResult;
import sg.reddotdev.sharkfin.data.parser.ResultParser;
import sg.reddotdev.sharkfin.data.parser.impl.FourDHTMLParser;
import sg.reddotdev.sharkfin.data.transaction.ResultDatabaseManager;
import sg.reddotdev.sharkfin.data.transaction.impl.FourDResultDatabaseManager;
import sg.reddotdev.sharkfin.manager.base.ResultRetrievalManager;
import sg.reddotdev.sharkfin.manager.impl.FourDRetrievalManager;
import sg.reddotdev.sharkfin.view.ViewMVP;
import sg.reddotdev.sharkfin.view.root.FourDRootView;

public class FourDActivity extends AppCompatActivity
        implements ResultRetrievalManager.ResultRetrievalManagerListener,
        ResultDatabaseManager.ResultDataManagerListener {

    private ResultRetrievalManager fourDRetrievalManager;
    private ResultDatabaseManager fourDDatabaseManager;

    private ViewMVP rootViewMVP;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootViewMVP = new FourDRootView(LayoutInflater.from(this), null, this);
        setContentView(rootViewMVP.getRootView());

        setSupportActionBar();

        fourDRetrievalManager = new FourDRetrievalManager();
        fourDRetrievalManager.createRequest();

        fourDRetrievalManager.registerListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*Check onSuccessfulRetrieved & onFailureRetrieved listener for what's next*/
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
        fourDDatabaseManager.unregisterListener();
        super.onDestroy();

    }



    /*Listeners for ResultRetrieve*/
    @Override
    public void onSuccessfulRetrievedResult(String response) {
        /*Parse the result into 4D Result Objects*/
        /*To be saved into DB and view to show*/
        ResultParser fourDHTMLParcer = new FourDHTMLParser(response);
        final LotteryResult fourDLotteryResult = fourDHTMLParcer.parse();
        fourDDatabaseManager = new FourDResultDatabaseManager();
        fourDDatabaseManager.registerListener(this);
        fourDDatabaseManager.save(fourDLotteryResult);
    }

    @Override
    public void onFailureRetrievedResult() {
        /*TODO: implement proper error handling mechanism*/
    }

    /*Listeners for ResultDataManager*/
    @Override
    public void onSuccessSave() {

    }

    @Override
    public void onFailureSave() {
        /*TODO: implement proper error handling mechanism*/
    }

    @Override
    public void onSuccessRetrieve() {

    }

    @Override
    public void onFailureRetrieve() {
        /*TODO: implement proper error handling mechanism*/
    }
}
