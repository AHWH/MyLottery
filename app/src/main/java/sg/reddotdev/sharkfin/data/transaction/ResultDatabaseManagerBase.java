/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.data.transaction;

import sg.reddotdev.sharkfin.data.model.LotteryResult;

/**
 * Created by weihong on 15/7/17.
 */

public abstract class ResultDatabaseManagerBase implements ResultDatabaseManager {
    protected ResultDataManagerListener listener;

    public abstract void save(final LotteryResult lotteryResult);

    public abstract LotteryResult retrieve();

    @Override
    public void registerListener(ResultDataManagerListener listener) {
        this.listener = listener;
    }

    @Override
    public void unregisterListener() {
        listener = null;
    }
}
