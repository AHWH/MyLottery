/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.manager.impl;

import android.util.Log;

import com.androidnetworking.common.ANRequest;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import sg.reddotdev.sharkfin.data.network.impl.FourDRequestBuilder;
import sg.reddotdev.sharkfin.manager.base.ResultRetrievalManager;


public class FourDRetrievalManager implements ResultRetrievalManager {
    private ResultRetrievalManagerListener listener;
    private ANRequest request;
    private int retryCount;

    public void createRequest() {
        request = new FourDRequestBuilder().createRequest();
    }

    @Override
    public void retrieve() {
        retryCount = 0;
        if(request != null) {
            request.getAsString(new StringRequestListener() {
                @Override
                public void onResponse(String response) {
                    Log.d("Process", "Successfully retrieved!");
                    validate(response);
                }

                @Override
                public void onError(ANError anError) {
                    Log.d("Process", "Failed to retrieve!");
                    Log.d("Process", anError.getErrorDetail());
                    if(listener != null) {
                        listener.onFailureRetrievedResult();
                    }
                }
            });
        }
    }

    public void validate(String response) {
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
            listener.onSuccessfulRetrievedResult();
        }
    }


    /*Listeners registration*/
    @Override
    public void registerListener(ResultRetrievalManagerListener listener) {
        this.listener = listener;
    }

    @Override
    public void unregisterListener() {
        listener = null;
    }
}
