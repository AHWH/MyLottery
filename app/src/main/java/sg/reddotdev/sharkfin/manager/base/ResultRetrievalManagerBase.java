/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.manager.base;

import android.util.Log;

import com.androidnetworking.common.ANRequest;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

/**
 * Created by weihong on 15/7/17.
 */

public abstract class ResultRetrievalManagerBase implements ResultRetrievalManager{
    private ResultRetrievalManagerListener listener;
    private ANRequest request;

    private int retryCount;

    public ResultRetrievalManagerBase() {
        retryCount = 0;
    }

    public abstract void createRequest();


    public void retrieve() {
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

    @Override
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
