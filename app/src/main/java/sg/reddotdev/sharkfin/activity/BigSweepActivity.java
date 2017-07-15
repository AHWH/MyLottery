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
import sg.reddotdev.sharkfin.data.model.LotteryResult;
import sg.reddotdev.sharkfin.data.parser.ResultParser;
import sg.reddotdev.sharkfin.data.parser.impl.BigSweepHTMLParser;
import sg.reddotdev.sharkfin.data.transaction.ResultDatabaseManager;
import sg.reddotdev.sharkfin.data.transaction.impl.BigSweepResultDatabaseManager;
import sg.reddotdev.sharkfin.manager.base.ResultRetrievalManager;
import sg.reddotdev.sharkfin.manager.impl.BigSweepRetrievalManager;


public class BigSweepActivity extends AppCompatActivity
        implements ResultRetrievalManager.ResultRetrievalManagerListener,
        ResultDatabaseManager.ResultDataManagerListener {
    private ResultRetrievalManager bigSweepResultRetrievalManager;
    private ResultDatabaseManager bigSweepResultDatabaseManager;

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
        bigSweepResultDatabaseManager.unregisterListener();
        super.onDestroy();
    }



    /*Listeners for ResultRetrieval*/
    @Override
    public void onSuccessfulRetrievedResult(String response) {
                /*Parse the result into 4D Result Objects*/
        /*To be saved into DB and view to show*/
        ResultParser fourDHTMLParcer = new BigSweepHTMLParser(response);
        final LotteryResult bigSweepLotteryResult = fourDHTMLParcer.parse();
        bigSweepResultDatabaseManager = new BigSweepResultDatabaseManager();
        bigSweepResultDatabaseManager.registerListener(this);
        bigSweepResultDatabaseManager.save(bigSweepLotteryResult);
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


