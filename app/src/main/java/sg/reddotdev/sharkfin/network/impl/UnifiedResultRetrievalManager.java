/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.network.impl;

import android.util.Log;

import com.androidnetworking.common.ANRequest;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import javax.inject.Inject;

import sg.reddotdev.sharkfin.network.base.ResultRetrievalManager;


public class UnifiedResultRetrievalManager implements ResultRetrievalManager {
    private String LOGTAG = getClass().getSimpleName();
    private ResultRetrievalManagerListener listener;
    private ANRequest request;

    @Inject
    FourDRequestBuilder fourDRequestBuilder;
    @Inject
    TotoRequestBuilder totoRequestBuilder;
    @Inject
    BigSweepRequestBuilder bigSweepRequestBuilder;

    private int retryCount;

    public UnifiedResultRetrievalManager() {
        retryCount = 0;
    }


    public void create4DRequest(int drawNo) {
        fourDRequestBuilder.setDrawNo(drawNo);
        request = fourDRequestBuilder.createRequest();
        retryCount = 0;
    }

    public void createTotoRequest(int drawNo) {
        totoRequestBuilder.setDrawNo(drawNo);
        request = totoRequestBuilder.createRequest();
        retryCount = 0;
    }

    public void createBigSweepRequest(int drawNo) {
        bigSweepRequestBuilder.setDrawNo(drawNo);
        request = bigSweepRequestBuilder.createRequest();
        retryCount = 0;
    }


    public void retrieve() {
        if(request != null) {
            request.getAsString(new StringRequestListener() {
                @Override
                public void onResponse(String response) {
                    Log.d(LOGTAG, "Successfully retrieved!");
                    validate(response);
                }

                @Override
                public void onError(ANError anError) {
                    Log.d(LOGTAG, "Failed to retrieve!");
                    Log.d(LOGTAG, anError.getErrorDetail());
                    if(listener != null) {
                        listener.onFailureRetrievedResult();
                    }
                }
            });
        }
    }

    /*TODO: Validate more!*/
    private void validate(String response) {
        /*Checks if full content is loaded*/
        if(!response.contains("tables-wrap") && retryCount < 2) {
            retrieve();
            retryCount++;
            return;
        } else if(!response.contains("tables-wrap") && retryCount >= 2) {
            if(listener != null) {
                listener.onFailureRetrievedResult();
            }
            return;
        }
        retryCount = 0;
        if(listener != null) {
            listener.onSuccessfulRetrievedResult(response);
        }
    }

    @Override
    public void registerListener(ResultRetrievalManagerListener listener) {
        this.listener = listener;
    }

    @Override
    public void unregisterListener() {
        listener = null;
    }

    public void setRequest(ANRequest request) {
        this.request = request;
    }
}
