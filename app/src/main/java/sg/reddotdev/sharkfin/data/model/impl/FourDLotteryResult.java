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

import org.threeten.bp.ZonedDateTime;

import java.util.Calendar;
import java.util.List;

import sg.reddotdev.sharkfin.data.database.LotteryDatabase;
import sg.reddotdev.sharkfin.data.model.LotteryNumber;
import sg.reddotdev.sharkfin.data.model.LotteryResult;
import sg.reddotdev.sharkfin.util.constants.LottoConst;

/**
 * Created by weiho on 3/7/2017.
 */
@Table(database = LotteryDatabase.class)
public class FourDLotteryResult extends LotteryResult {
    /*This 3 variables are really just placeholders*/
    @Column
    private int firstPrize;
    @Column
    private int secondPrize;
    @Column
    private int thirdPrize;

    List<FourDLotteryNumber> starterNumbers;
    List<FourDLotteryNumber> consolationNumbers;

    public FourDLotteryResult() {}

    public FourDLotteryResult(int lotteryID, ZonedDateTime date, List<Integer> standaloneNos, List<FourDLotteryNumber> starterNumbers, List<FourDLotteryNumber> consolationNumbers) {
        super(lotteryID, date);
        firstPrize = standaloneNos.get(0);
        secondPrize = standaloneNos.get(1);
        thirdPrize = standaloneNos.get(2);
        this.starterNumbers = starterNumbers;
        this.consolationNumbers = consolationNumbers;
    }


    public int getFirstPrize() {
        return firstPrize;
    }

    public void setFirstPrize(int firstPrize) {
        this.firstPrize = firstPrize;
    }

    public int getSecondPrize() {
        return secondPrize;
    }

    public void setSecondPrize(int secondPrize) {
        this.secondPrize = secondPrize;
    }

    public int getThirdPrize() {
        return thirdPrize;
    }

    public void setThirdPrize(int thirdPrize) {
        this.thirdPrize = thirdPrize;
    }


    public List<FourDLotteryNumber> getStarterNumbers() {
        return starterNumbers;
    }

    public void setStarterNumbers(List<FourDLotteryNumber> starterNumbers) {
        this.starterNumbers = starterNumbers;
    }

    public List<FourDLotteryNumber> getConsolationNumbers() {
        return consolationNumbers;
    }

    public void setConsolationNumbers(List<FourDLotteryNumber> consolationNumbers) {
        this.consolationNumbers = consolationNumbers;
    }


    /*DBFlow One-to-Many use only!*/
    @OneToMany(methods = {OneToMany.Method.ALL}, variableName = "starterNumbers")
    public List<FourDLotteryNumber> getStarterNoList() {
        if(starterNumbers == null || starterNumbers.isEmpty()) {
            starterNumbers = SQLite.select()
                                    .from(FourDLotteryNumber.class)
                                    .where(FourDLotteryNumber_Table.lotteryID.eq(this.getLotteryID()))
                                    .and(FourDLotteryNumber_Table.date.eq(this.getDate()))
                                    .and(FourDLotteryNumber_Table.type.eq(LottoConst.SGPOOLS_4D_STARTER))
                                    .queryList();
        }
        return starterNumbers;
    }

    @OneToMany(methods = {OneToMany.Method.ALL}, variableName = "consolationNumbers")
    public List<FourDLotteryNumber> getConsolationNoList() {
        if(consolationNumbers == null || consolationNumbers.isEmpty()) {
            consolationNumbers = SQLite.select()
                                        .from(FourDLotteryNumber.class)
                                        .where(FourDLotteryNumber_Table.lotteryID.eq(this.getLotteryID()))
                                        .and(FourDLotteryNumber_Table.date.eq(this.getDate()))
                                        .and(FourDLotteryNumber_Table.type.eq(LottoConst.SGPOOLS_4D_CONSOLATION))
                                        .queryList();
        }
        return consolationNumbers;
    }


    @Override
    public String toString() {
        String toBePrinted = super.toString() + "\n"
                            + "First Prize: " + getFirstPrize() + "\n"
                            + "Second Prize: " + getSecondPrize() + "\n"
                            + "Third Prize: " + getThirdPrize() + "\n\n"
                            + "Starter Prizes: ";

        for(LotteryNumber starterNo: starterNumbers) {
            toBePrinted += starterNo.getNum() + "\n";
        }
        toBePrinted += "\nConsolation Prizes: ";

        for(LotteryNumber consolationNo: consolationNumbers) {
            toBePrinted += consolationNo.getNum() + "\n";
        }

        return toBePrinted + "\n";
    }
}
