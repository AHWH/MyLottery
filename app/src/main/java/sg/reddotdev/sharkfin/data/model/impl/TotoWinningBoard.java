/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.data.model.impl;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.Calendar;
import java.util.List;

import sg.reddotdev.sharkfin.data.database.LotteryDatabase;
import sg.reddotdev.sharkfin.data.model.LotteryResult;
import sg.reddotdev.sharkfin.data.model.TotoWinner;
import sg.reddotdev.sharkfin.data.model.TotoWinner_Table;
import sg.reddotdev.sharkfin.util.LottoConst;

@Table(database = LotteryDatabase.class)
public class TotoWinningBoard extends BaseModel {
    @PrimaryKey(autoincrement = true)
    private int id;

    @ForeignKey(tableClass = LotteryResult.class, references = {@ForeignKeyReference(columnName = "lotteryID", foreignKeyColumnName = "lotteryID")})
    private int lotteryID;
    @ForeignKey(tableClass = LotteryResult.class, references = {@ForeignKeyReference(columnName = "date", foreignKeyColumnName = "date")})
    private Calendar date;

    @Column
    private double group1PrizeAmt;
    List<TotoWinner> group1Winners;

    @Column
    private double group2PrizeAmt;
    List<TotoWinner> group2Winners;

    @Column
    private double group3PrizeAmt;
    @Column
    private int group3Winners;

    @Column
    private double group4PrizeAmt;
    @Column
    private int group4Winners;

    @Column
    private int group5Winners;

    @Column
    private int group6Winners;

    @Column
    private int group7Winners;

    @Column
    private double totalPrizeAmt;


    public TotoWinningBoard() {
    }

    public TotoWinningBoard(int lotteryID, Calendar date, List<Double> grp1To4Prizes, List<Integer> grp3To7WinnersAmt, List<TotoWinner> group1Winners, List<TotoWinner> group2Winners) {
        this.lotteryID = lotteryID;
        this.date = date;

        group1PrizeAmt = grp1To4Prizes.get(0);
        this.group1Winners = group1Winners;

        group2PrizeAmt = grp1To4Prizes.get(1);
        this.group2Winners = group2Winners;

        group3PrizeAmt = grp1To4Prizes.get(2);
        group3Winners = grp3To7WinnersAmt.get(0);

        group4PrizeAmt = grp1To4Prizes.get(3);
        group4Winners = grp3To7WinnersAmt.get(1);

        group5Winners = grp3To7WinnersAmt.get(2);
        group6Winners = grp3To7WinnersAmt.get(3);
        group7Winners = grp3To7WinnersAmt.get(4);

        /*TODO: implement calculate total prize money given out*/
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public double getGroup1PrizeAmt() {
        return group1PrizeAmt;
    }

    public void setGroup1PrizeAmt(double group1PrizeAmt) {
        this.group1PrizeAmt = group1PrizeAmt;
    }

    public List<TotoWinner> getGroup1Winners() {
        return group1Winners;
    }

    public void setGroup1Winners(List<TotoWinner> group1Winners) {
        this.group1Winners = group1Winners;
    }

    public double getGroup2PrizeAmt() {
        return group2PrizeAmt;
    }

    public void setGroup2PrizeAmt(double group2PrizeAmt) {
        this.group2PrizeAmt = group2PrizeAmt;
    }

    public List<TotoWinner> getGroup2Winners() {
        return group2Winners;
    }

    public void setGroup2Winners(List<TotoWinner> group2Winners) {
        this.group2Winners = group2Winners;
    }

    public double getGroup3PrizeAmt() {
        return group3PrizeAmt;
    }

    public void setGroup3PrizeAmt(double group3PrizeAmt) {
        this.group3PrizeAmt = group3PrizeAmt;
    }

    public int getGroup3Winners() {
        return group3Winners;
    }

    public void setGroup3Winners(int group3Winners) {
        this.group3Winners = group3Winners;
    }

    public double getGroup4PrizeAmt() {
        return group4PrizeAmt;
    }

    public void setGroup4PrizeAmt(double group4PrizeAmt) {
        this.group4PrizeAmt = group4PrizeAmt;
    }

    public int getGroup4Winners() {
        return group4Winners;
    }

    public void setGroup4Winners(int group4Winners) {
        this.group4Winners = group4Winners;
    }

    public int getGroup5Winners() {
        return group5Winners;
    }

    public void setGroup5Winners(int group5Winners) {
        this.group5Winners = group5Winners;
    }

    public int getGroup6Winners() {
        return group6Winners;
    }

    public void setGroup6Winners(int group6Winners) {
        this.group6Winners = group6Winners;
    }

    public int getGroup7Winners() {
        return group7Winners;
    }

    public void setGroup7Winners(int group7Winners) {
        this.group7Winners = group7Winners;
    }

    public double getTotalPrizeAmt() {
        return totalPrizeAmt;
    }

    public void setTotalPrizeAmt(double totalPrizeAmt) {
        this.totalPrizeAmt = totalPrizeAmt;
    }


    /*DBFlow One-to-Many Methods*/
    @OneToMany(methods = {OneToMany.Method.ALL}, variableName = "group1Winners")
    public List<TotoWinner> getGrp1TotoWinners() {
        if(group1Winners == null || group1Winners.isEmpty()) {
            group1Winners = SQLite.select()
                                    .from(TotoWinner.class)
                                    .where(TotoWinner_Table.lotteryID.eq(lotteryID))
                                    .and(TotoWinner_Table.winningGrp.eq(LottoConst.SGPOOLS_TOTO_WIN_G1))
                                    .queryList();
        }
        return group1Winners;
    }

    @OneToMany(methods = {OneToMany.Method.ALL}, variableName = "group2Winners")
    public List<TotoWinner> getGrp2TotoWinners() {
        if(group2Winners == null || group2Winners.isEmpty()) {
            group2Winners = SQLite.select()
                    .from(TotoWinner.class)
                    .where(TotoWinner_Table.lotteryID.eq(lotteryID))
                    .and(TotoWinner_Table.winningGrp.eq(LottoConst.SGPOOLS_TOTO_WIN_G2))
                    .queryList();
        }
        return group2Winners;
    }
}
