/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.data.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.Calendar;

import sg.reddotdev.sharkfin.data.database.LotteryDatabase;

@Table(database = LotteryDatabase.class)
public class LotteryNumber extends BaseModel {
    @PrimaryKey
    private int num;
    @ForeignKey(tableClass = LotteryResult.class, references = {@ForeignKeyReference(columnName = "lotteryID", foreignKeyColumnName = "lotteryID")})
    @PrimaryKey
    private int lotteryID;
    @ForeignKey(tableClass = LotteryResult.class, references = {@ForeignKeyReference(columnName = "date", foreignKeyColumnName = "date")})
    private Calendar date;
    @Column
    private int type;

    public LotteryNumber() {
    }

    public LotteryNumber(int num, int lotteryID, Calendar date, int type) {
        this.num = num;
        this.lotteryID = lotteryID;
        this.date = date;
        this.type = type;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getLotteryID() {
        return lotteryID;
    }

    public void setLotteryID(int lotteryID) {
        this.lotteryID = lotteryID;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return Integer.toString(num);
    }
}
