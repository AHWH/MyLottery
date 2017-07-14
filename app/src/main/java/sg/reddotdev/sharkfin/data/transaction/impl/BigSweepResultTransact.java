/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.data.transaction.impl;

import android.support.annotation.NonNull;
import android.util.Log;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;

import java.util.Calendar;

import sg.reddotdev.sharkfin.data.database.LotteryDatabase;
import sg.reddotdev.sharkfin.data.model.LotteryResult;
import sg.reddotdev.sharkfin.data.model.impl.BigSweepLottery5DNumber;
import sg.reddotdev.sharkfin.data.model.impl.BigSweepLottery7DANumber;
import sg.reddotdev.sharkfin.data.model.impl.BigSweepLottery7DNumber;
import sg.reddotdev.sharkfin.data.model.impl.BigSweepLotteryResult;
import sg.reddotdev.sharkfin.data.transaction.ResultTransact;
import sg.reddotdev.sharkfin.util.LottoConst;


public class BigSweepResultTransact implements ResultTransact {
    @Override
    public void save(final LotteryResult lotteryResult) {
        DatabaseDefinition db = FlowManager.getDatabase(LotteryDatabase.class);
        Transaction transaction = db.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                Log.d("Process", "Saving");
                BigSweepLotteryResult bigSweepLotteryResult = (BigSweepLotteryResult) lotteryResult;
                bigSweepLotteryResult.save();
                int lotteryID = bigSweepLotteryResult.getLotteryID();
                Calendar date = bigSweepLotteryResult.getDate();

                new BigSweepLottery7DNumber(bigSweepLotteryResult.getFirstNumber(), lotteryID, date, LottoConst.SGPOOLS_SWEEP_FIRST).save();
                new BigSweepLottery7DNumber(bigSweepLotteryResult.getSecondNumber(), lotteryID, date, LottoConst.SGPOOLS_SWEEP_SECOND).save();
                new BigSweepLottery7DNumber(bigSweepLotteryResult.getThirdNumber(), lotteryID, date, LottoConst.SGPOOLS_SWEEP_THIRD).save();

                String superSweepStr = bigSweepLotteryResult.getSuperSweepNumber();

                if(!superSweepStr.equals("")) {
                    new BigSweepLottery7DANumber(Integer.parseInt(superSweepStr.substring(0, 7)), lotteryID, date, LottoConst.SGPOOLS_SWEEP_SUPER_SWEEP, superSweepStr.charAt(7)).save();
                }

                int cascadeNo = bigSweepLotteryResult.getCascadeNumber();
                if(cascadeNo != 0) {
                    new BigSweepLottery5DNumber(cascadeNo, lotteryID, date, LottoConst.SGPOOLS_SWEEP_CASCADE).save();
                }
            }
        }).success(new Transaction.Success() {
            @Override
            public void onSuccess(@NonNull Transaction transaction) {
                Log.d("Process", "Successfully saved!");
                //retrieveResults();
            }
        }).error(new Transaction.Error() {
            @Override
            public void onError(@NonNull Transaction transaction, @NonNull Throwable error) {
                Log.d("Process", "Failed to save!");
            }
        }).build();
        transaction.execute();

    }

    @Override
    public LotteryResult retrieve() {
        return null;
    }
}
