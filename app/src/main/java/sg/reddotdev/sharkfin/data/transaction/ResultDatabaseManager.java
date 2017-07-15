/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.data.transaction;

import javax.xml.transform.Result;

import sg.reddotdev.sharkfin.data.model.LotteryResult;

public interface ResultDatabaseManager {

    interface ResultDataManagerListener {

        void onSuccessSave();
        void onFailureSave();

        void onSuccessRetrieve();
        void onFailureRetrieve();
    }

    void save(final LotteryResult lotteryResult);

    LotteryResult retrieve();

    void registerListener(ResultDataManagerListener listener);
    void unregisterListener();
}
