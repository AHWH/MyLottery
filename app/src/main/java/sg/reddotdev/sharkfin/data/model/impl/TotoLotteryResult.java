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
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.Calendar;
import java.util.List;

import sg.reddotdev.sharkfin.data.database.LotteryDatabase;
import sg.reddotdev.sharkfin.data.model.LotteryNumber;
import sg.reddotdev.sharkfin.data.model.LotteryResult;
import sg.reddotdev.sharkfin.util.LottoConst;

/**
 * Created by weiho on 3/7/2017.
 */
@Table(database = LotteryDatabase.class)
public class TotoLotteryResult extends LotteryResult {
    List<TotoLotteryNumber> winningNumbers;
    /*Placeholder*/
    @Column
    private int additionalNumber;
    @Column
    private double firstPrizeAmt;

    @ForeignKey(stubbedRelationship = true, tableClass = TotoWinningBoard.class, references = {@ForeignKeyReference(columnName = "totoWinningBoard", foreignKeyColumnName = "id")})
    TotoWinningBoard totoWinningBoard;


    public TotoLotteryResult() {
    }

    public TotoLotteryResult(int lotteryID, Calendar date, List<TotoLotteryNumber> winningNumbers, int additionalNumber, double firstPrizeAmt, TotoWinningBoard winningBoard) {
        super(lotteryID, date);
        this.winningNumbers = winningNumbers;
        this.additionalNumber = additionalNumber;
        this.firstPrizeAmt = firstPrizeAmt;
        totoWinningBoard = winningBoard;
    }


    public List<TotoLotteryNumber> getWinningNumbers() {
        return winningNumbers;
    }

    public void setWinningNumbers(List<TotoLotteryNumber> winningNumbers) {
        this.winningNumbers = winningNumbers;
    }

    public int getAdditionalNumber() {
        return additionalNumber;
    }

    public void setAdditionalNumber(int additionalNumber) {
        this.additionalNumber = additionalNumber;
    }

    public double getFirstPrizeAmt() {
        return firstPrizeAmt;
    }

    public void setFirstPrizeAmt(double firstPrizeAmt) {
        this.firstPrizeAmt = firstPrizeAmt;
    }

    public TotoWinningBoard getTotoWinningBoard() {
        return totoWinningBoard;
    }

    public void setTotoWinningBoard(TotoWinningBoard totoWinningBoard) {
        this.totoWinningBoard = totoWinningBoard;
    }

    /*DBFlow One-to-Many method*/
    @OneToMany(methods = {OneToMany.Method.ALL}, variableName = "winningNumbers")
    public List<TotoLotteryNumber> getWinningNumbersList() {
        if(winningNumbers == null || winningNumbers.isEmpty()) {
            winningNumbers = SQLite.select()
                                    .from(TotoLotteryNumber.class)
                                    .where(TotoLotteryNumber_Table.lotteryID.eq(this.getLotteryID()))
                                    .and(TotoLotteryNumber_Table.date.eq(this.getDate()))
                                    .and(TotoLotteryNumber_Table.type.eq(LottoConst.SGPOOLS_TOTO_NUM))
                                    .queryList();
        }
        return winningNumbers;
    }


    @Override
    public String toString() {
        String toBePrinted = super.toString() + "\n"
                + "Winning Numbers: " + "\n";

        for(LotteryNumber winningNum: winningNumbers) {
            toBePrinted += winningNum.getNum() + "\n";
        }
        toBePrinted += "\nAdditional Number: " + additionalNumber;

        return toBePrinted + "\n";
    }
}
