/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.data.model.impl;

import com.raizlabs.android.dbflow.annotation.Table;

import org.threeten.bp.ZonedDateTime;

import sg.reddotdev.sharkfin.data.database.LotteryDatabase;
import sg.reddotdev.sharkfin.data.model.LotteryNumber;

@Table(database = LotteryDatabase.class)
public class BigSweepLottery2DNumber extends LotteryNumber {
    public BigSweepLottery2DNumber() {
    }

    public BigSweepLottery2DNumber(int num, int lotteryID, ZonedDateTime date, int type) {
        super(num, lotteryID, date, type);
    }
}
