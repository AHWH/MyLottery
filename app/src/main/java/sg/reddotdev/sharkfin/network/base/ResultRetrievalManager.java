/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.network.base;

import com.androidnetworking.common.ANRequest;

import sg.reddotdev.sharkfin.data.model.LotteryResult;

/*This interface is the blueprint of every retrieval mechanism for 4D/TOTO/BigSweep*/
public interface ResultRetrievalManager {
    /*Internal interface to be implemented by Presenter (Activity/Fragment)
      Used for callbacks purposes */
    interface ResultRetrievalManagerListener {
        void onSuccessfulRetrievedResult(String response);

        void onFailureRetrievedResult();
    }

    /*Retrieved Result*/
    void retrieve();

    void registerListener(ResultRetrievalManagerListener listener);
    void unregisterListener();
}
