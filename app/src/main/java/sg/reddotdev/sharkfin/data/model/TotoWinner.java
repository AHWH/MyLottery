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

import java.util.Calendar;

import sg.reddotdev.sharkfin.data.database.LotteryDatabase;
import sg.reddotdev.sharkfin.data.model.impl.TotoLotteryResult;

/*This table is only for Group 1 and 2 Winner*/
@Table(database = LotteryDatabase.class)
public class TotoWinner {
    @ForeignKey(tableClass = LotteryResult.class, references = {@ForeignKeyReference(columnName = "lotteryID", foreignKeyColumnName = "lotteryID")})
    @PrimaryKey
    private int lotteryID;
    @ForeignKey(tableClass = LotteryResult.class, references = {@ForeignKeyReference(columnName = "date", foreignKeyColumnName = "date")})
    private Calendar date;

    @PrimaryKey
    private int winningGrp;

    @ForeignKey(stubbedRelationship = true, tableClass = Branch.class, references = {@ForeignKeyReference(columnName = "branch", foreignKeyColumnName = "name")})
    private Branch branch;

    @Column
    private String winningType;

    @PrimaryKey
    private int duplicatePrevention;


    public TotoWinner() {
    }

    public TotoWinner(int lotteryID, Calendar date, int duplicatePrevention, Branch branch, int winningGrp, String winningType) {
        this.lotteryID = lotteryID;
        this.date = date;
        this.duplicatePrevention = duplicatePrevention;
        this.branch = branch;
        this.winningGrp = winningGrp;
        this.winningType = winningType;
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

    public int getDuplicatePrevention() {
        return duplicatePrevention;
    }

    public void setDuplicatePrevention(int duplicatePrevention) {
        this.duplicatePrevention = duplicatePrevention;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public int getWinningGrp() {
        return winningGrp;
    }

    public void setWinningGrp(int winningGrp) {
        this.winningGrp = winningGrp;
    }

    public String getWinningType() {
        return winningType;
    }

    public void setWinningType(String winningType) {
        this.winningType = winningType;
    }
}
