/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.data.transaction;

import sg.reddotdev.sharkfin.data.model.LotteryResult;

public interface ResultTransact {
    void save(final LotteryResult lotteryResult);

    LotteryResult retrieve();
}
