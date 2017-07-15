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
import sg.reddotdev.sharkfin.data.parser.impl.TotoHTMLParser;
import sg.reddotdev.sharkfin.data.transaction.ResultDatabaseManager;
import sg.reddotdev.sharkfin.data.transaction.impl.TotoDatabaseManager;
import sg.reddotdev.sharkfin.manager.base.ResultRetrievalManager;
import sg.reddotdev.sharkfin.manager.impl.TotoRetrievalManager;


public class TotoActivity extends AppCompatActivity
        implements ResultRetrievalManager.ResultRetrievalManagerListener,
        ResultDatabaseManager.ResultDataManagerListener {
    private ResultRetrievalManager totoRetrievalManager;
    private ResultDatabaseManager totoDatabaseManager;

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
        totoDatabaseManager.unregisterListener();
        super.onDestroy();
    }



    /*Listeners for ResultRetrieve*/
    @Override
    public void onSuccessfulRetrievedResult(String response) {
        /*Parse the result into 4D Result Objects*/
        /*To be saved into DB and view to show*/
        ResultParser fourDHTMLParcer = new TotoHTMLParser(response);
        final LotteryResult totoLotteryResult = fourDHTMLParcer.parse();
        totoDatabaseManager = new TotoDatabaseManager();
        totoDatabaseManager.registerListener(this);
        totoDatabaseManager.save(totoLotteryResult);
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
