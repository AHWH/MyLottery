/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.data.model.impl;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.Table;

import java.util.Calendar;

import sg.reddotdev.sharkfin.data.database.LotteryDatabase;
import sg.reddotdev.sharkfin.data.model.LotteryNumber;

@Table(database = LotteryDatabase.class)
public class BigSweepLottery7DANumber extends LotteryNumber {
    @Column
    private char finalAlphabet;

    public BigSweepLottery7DANumber(){
    }

    public BigSweepLottery7DANumber(int num, int lotteryID, Calendar date, int type, char finalAlphabet) {
        super(num, lotteryID, date, type);
        this.finalAlphabet = finalAlphabet;
    }

    public char getFinalAlphabet() {
        return finalAlphabet;
    }

    public void setFinalAlphabet(char finalAlphabet) {
        this.finalAlphabet = finalAlphabet;
    }

    @Override
    public String toString() {
        return super.toString() + finalAlphabet;
    }
}
