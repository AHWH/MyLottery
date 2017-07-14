/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.data.model.impl;

import com.raizlabs.android.dbflow.annotation.Table;

import java.util.Calendar;

import sg.reddotdev.sharkfin.data.database.LotteryDatabase;
import sg.reddotdev.sharkfin.data.model.LotteryNumber;

@Table(database = LotteryDatabase.class)
public class BigSweepLottery7DNumber extends LotteryNumber {
    public BigSweepLottery7DNumber() {
    }

    public BigSweepLottery7DNumber(int num, int lotteryID, Calendar date, int type) {
        super(num, lotteryID, date, type);
    }
}
