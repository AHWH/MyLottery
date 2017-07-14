/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.data.model.impl;

import com.raizlabs.android.dbflow.annotation.Column;
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
public class BigSweepLotteryResult extends LotteryResult {
    @Column
    private int firstNumber;
    @Column
    private int secondNumber;
    @Column
    private int thirdNumber;

    /*Not in used for newer sweeps*/
    @Column
    private String superSweepNumber;
    @Column
    private int cascadeNumber;

    List<BigSweepLottery7DNumber> jackpotNumbers;
    List<BigSweepLottery7DNumber> luckyNumbers;
    List<BigSweepLottery7DNumber> giftNumbers;
    List<BigSweepLottery7DNumber> consolationNumbers;
    /*Only appear in newest sweeps*/
    List<BigSweepLottery7DNumber> participationNumbers;
    List<BigSweepLottery2DNumber> delight2DNumbers;

    /*Only appear in older sweeps*/
    List<BigSweepLottery3DNumber> delight3DNumbers;


    public BigSweepLotteryResult() {
    }

    public BigSweepLotteryResult(int lotteryID, Calendar date, List<Integer> standaloneNos, String superSweepNumber, int cascadeNumber,
                                 List<BigSweepLottery7DNumber> jackpotNumbers, List<BigSweepLottery7DNumber> luckyNumbers, List<BigSweepLottery7DNumber> giftNumbers,
                                 List<BigSweepLottery7DNumber> consolationNumbers, List<BigSweepLottery7DNumber> participationNumbers, List<BigSweepLottery2DNumber> delight2DNumbers,
                                 List<BigSweepLottery3DNumber> delight3DNumbers) {
        super(lotteryID, date);
        this.firstNumber = standaloneNos.get(0);
        this.secondNumber = standaloneNos.get(1);
        this.thirdNumber = standaloneNos.get(2);
        this.superSweepNumber = superSweepNumber;
        this.cascadeNumber = cascadeNumber;
        this.jackpotNumbers = jackpotNumbers;
        this.luckyNumbers = luckyNumbers;
        this.giftNumbers = giftNumbers;
        this.consolationNumbers = consolationNumbers;
        this.participationNumbers = participationNumbers;
        this.delight2DNumbers = delight2DNumbers;
        this.delight3DNumbers = delight3DNumbers;
    }


    public int getFirstNumber() {
        return firstNumber;
    }

    public void setFirstNumber(int firstNumber) {
        this.firstNumber = firstNumber;
    }

    public int getSecondNumber() {
        return secondNumber;
    }

    public void setSecondNumber(int secondNumber) {
        this.secondNumber = secondNumber;
    }

    public int getThirdNumber() {
        return thirdNumber;
    }

    public void setThirdNumber(int thirdNumber) {
        this.thirdNumber = thirdNumber;
    }

    public String getSuperSweepNumber() {
        return superSweepNumber;
    }

    public void setSuperSweepNumber(String superSweepNumber) {
        this.superSweepNumber = superSweepNumber;
    }

    public int getCascadeNumber() {
        return cascadeNumber;
    }

    public void setCascadeNumber(int cascadeNumber) {
        this.cascadeNumber = cascadeNumber;
    }

    public List<BigSweepLottery7DNumber> getJackpotNumbers() {
        return jackpotNumbers;
    }

    public void setJackpotNumbers(List<BigSweepLottery7DNumber> jackpotNumbers) {
        this.jackpotNumbers = jackpotNumbers;
    }

    public List<BigSweepLottery7DNumber> getLuckyNumbers() {
        return luckyNumbers;
    }

    public void setLuckyNumbers(List<BigSweepLottery7DNumber> luckyNumbers) {
        this.luckyNumbers = luckyNumbers;
    }

    public List<BigSweepLottery7DNumber> getGiftNumbers() {
        return giftNumbers;
    }

    public void setGiftNumbers(List<BigSweepLottery7DNumber> giftNumbers) {
        this.giftNumbers = giftNumbers;
    }

    public List<BigSweepLottery7DNumber> getConsolationNumbers() {
        return consolationNumbers;
    }

    public void setConsolationNumbers(List<BigSweepLottery7DNumber> consolationNumbers) {
        this.consolationNumbers = consolationNumbers;
    }

    public List<BigSweepLottery7DNumber> getParticipationNumbers() {
        return participationNumbers;
    }

    public void setParticipationNumbers(List<BigSweepLottery7DNumber> participationNumbers) {
        this.participationNumbers = participationNumbers;
    }

    public List<BigSweepLottery2DNumber> getDelight2DNumbers() {
        return delight2DNumbers;
    }

    public void setDelight2DNumbers(List<BigSweepLottery2DNumber> delight2DNumbers) {
        this.delight2DNumbers = delight2DNumbers;
    }

    public List<BigSweepLottery3DNumber> getDelight3DNumbers() {
        return delight3DNumbers;
    }

    public void setDelight3DNumbers(List<BigSweepLottery3DNumber> delight3DNumbers) {
        this.delight3DNumbers = delight3DNumbers;
    }


    /*DBFlow's One-to-Many methods*/
    @OneToMany(methods = {OneToMany.Method.ALL}, variableName = "jackpotNumbers")
    public List<BigSweepLottery7DNumber> getJackpotNumbersList() {
        if(jackpotNumbers == null || jackpotNumbers.isEmpty()) {
            jackpotNumbers = SQLite.select()
                                    .from(BigSweepLottery7DNumber.class)
                                    .where(BigSweepLottery7DNumber_Table.lotteryID.eq(this.getLotteryID()))
                                    .and(BigSweepLottery7DNumber_Table.date.eq(this.getDate()))
                                    .and(BigSweepLottery7DNumber_Table.type.eq(LottoConst.SGPOOLS_SWEEP_JACKPOT))
                                    .queryList();
        }
        return jackpotNumbers;
    }

    @OneToMany(methods = {OneToMany.Method.ALL}, variableName = "luckyNumbers")
    public List<BigSweepLottery7DNumber> getLuckyNumbersList() {
        if(luckyNumbers == null || luckyNumbers.isEmpty()) {
            luckyNumbers = SQLite.select()
                    .from(BigSweepLottery7DNumber.class)
                    .where(BigSweepLottery7DNumber_Table.lotteryID.eq(this.getLotteryID()))
                    .and(BigSweepLottery7DNumber_Table.date.eq(this.getDate()))
                    .and(BigSweepLottery7DNumber_Table.type.eq(LottoConst.SGPOOLS_SWEEP_LUCKY))
                    .queryList();
        }
        return luckyNumbers;
    }

    @OneToMany(methods = {OneToMany.Method.ALL}, variableName = "giftNumbers")
    public List<BigSweepLottery7DNumber> getGiftNumbersList() {
        if(giftNumbers == null || giftNumbers.isEmpty()) {
            giftNumbers = SQLite.select()
                    .from(BigSweepLottery7DNumber.class)
                    .where(BigSweepLottery7DNumber_Table.lotteryID.eq(this.getLotteryID()))
                    .and(BigSweepLottery7DNumber_Table.date.eq(this.getDate()))
                    .and(BigSweepLottery7DNumber_Table.type.eq(LottoConst.SGPOOLS_SWEEP_GIFT))
                    .queryList();
        }
        return giftNumbers;
    }

    @OneToMany(methods = {OneToMany.Method.ALL}, variableName = "consolationNumbers")
    public List<BigSweepLottery7DNumber> getConsolationNumbersList() {
        if(consolationNumbers == null || consolationNumbers.isEmpty()) {
            consolationNumbers = SQLite.select()
                    .from(BigSweepLottery7DNumber.class)
                    .where(BigSweepLottery7DNumber_Table.lotteryID.eq(this.getLotteryID()))
                    .and(BigSweepLottery7DNumber_Table.date.eq(this.getDate()))
                    .and(BigSweepLottery7DNumber_Table.type.eq(LottoConst.SGPOOLS_SWEEP_CONSOLATION))
                    .queryList();
        }
        return consolationNumbers;
    }

    @OneToMany(methods = {OneToMany.Method.ALL}, variableName = "participationNumbers")
    public List<BigSweepLottery7DNumber> getParticipationNumbersList() {
        if(participationNumbers == null || participationNumbers.isEmpty()) {
            participationNumbers = SQLite.select()
                    .from(BigSweepLottery7DNumber.class)
                    .where(BigSweepLottery7DNumber_Table.lotteryID.eq(this.getLotteryID()))
                    .and(BigSweepLottery7DNumber_Table.date.eq(this.getDate()))
                    .and(BigSweepLottery7DNumber_Table.type.eq(LottoConst.SGPOOLS_SWEEP_PARTICIPATION))
                    .queryList();
        }
        return participationNumbers;
    }

    @OneToMany(methods = {OneToMany.Method.ALL}, variableName = "delight2DNumbers")
    public List<BigSweepLottery2DNumber> getDelight2DNumbersList() {
        if(delight2DNumbers == null || delight2DNumbers.isEmpty()) {
            delight2DNumbers = SQLite.select()
                    .from(BigSweepLottery2DNumber.class)
                    .where(BigSweepLottery2DNumber_Table.lotteryID.eq(this.getLotteryID()))
                    .and(BigSweepLottery2DNumber_Table.date.eq(this.getDate()))
                    .and(BigSweepLottery2DNumber_Table.type.eq(LottoConst.SGPOOLS_SWEEP_2D))
                    .queryList();
        }
        return delight2DNumbers;
    }

    @OneToMany(methods = {OneToMany.Method.ALL}, variableName = "delight3DNumbers")
    public List<BigSweepLottery3DNumber> getDelight3DNumbersList() {
        if(delight3DNumbers == null || delight3DNumbers.isEmpty()) {
            delight3DNumbers = SQLite.select()
                    .from(BigSweepLottery3DNumber.class)
                    .where(BigSweepLottery3DNumber_Table.lotteryID.eq(this.getLotteryID()))
                    .and(BigSweepLottery3DNumber_Table.date.eq(this.getDate()))
                    .and(BigSweepLottery3DNumber_Table.type.eq(LottoConst.SGPOOLS_SWEEP_3D))
                    .queryList();
        }
        return delight3DNumbers;
    }

    @Override
    public String toString() {
        String toBePrinted = super.toString() + "\n"
                + "1st Prize: " + firstNumber + "\n"
                + "2nd Prize: " + secondNumber + "\n"
                + "3rd Prize: " + thirdNumber + "\n"
                + "Super Sweep: " + superSweepNumber + "\n"
                + "Cascade Number: " + cascadeNumber + "\n\n"
                + "Jackpot Prizes: " + "\n";

        for(LotteryNumber jackpotNum: jackpotNumbers) {
            toBePrinted += jackpotNum.getNum() + "\n";
        }

        toBePrinted += "\nLucky Prizes: " + "\n";

        for(LotteryNumber luckyNum: luckyNumbers) {
            toBePrinted += luckyNum.getNum() + "\n";
        }

        toBePrinted += "\nGift Prizes: " + "\n";

        for(LotteryNumber giftNum: giftNumbers) {
            toBePrinted += giftNum.getNum() + "\n";
        }

        toBePrinted += "\nConsolation Prizes: " + "\n";

        for(LotteryNumber consolationNumbers: getConsolationNumbers()) {
            toBePrinted += consolationNumbers.getNum() + "\n";
        }

        toBePrinted += "\nParticipation Prizes: " + "\n";

        for(LotteryNumber participationNumbers: getParticipationNumbers()) {
            toBePrinted += participationNumbers.getNum() + "\n";
        }

        toBePrinted += "\n2D Delight Prizes: " + "\n";

        for(LotteryNumber delight2DNum: delight2DNumbers) {
            toBePrinted += delight2DNum.getNum() + "\n";
        }

        toBePrinted += "\n3D Delight Prizes: " + "\n";

        for(LotteryNumber delight3DNum: delight3DNumbers) {
            toBePrinted += delight3DNum.getNum() + "\n";
        }

        return toBePrinted + "\n";
    }
}
