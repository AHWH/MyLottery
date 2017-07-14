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

import sg.reddotdev.sharkfin.data.database.LotteryDatabase;
import sg.reddotdev.sharkfin.data.model.LotteryResult;
import sg.reddotdev.sharkfin.data.model.impl.FourDLotteryNumber;
import sg.reddotdev.sharkfin.data.model.impl.FourDLotteryResult;
import sg.reddotdev.sharkfin.data.transaction.ResultTransact;
import sg.reddotdev.sharkfin.util.LottoConst;


public class FourDResultTransact implements ResultTransact {

    public void save(final LotteryResult lotteryResult) {
        DatabaseDefinition db = FlowManager.getDatabase(LotteryDatabase.class);
        Transaction transaction = db.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                Log.d("Process", "Saving");
                FourDLotteryResult fourDLotteryResult = (FourDLotteryResult) lotteryResult;
                fourDLotteryResult.save();
                new FourDLotteryNumber(fourDLotteryResult.getFirstPrize(), fourDLotteryResult.getLotteryID(), fourDLotteryResult.getDate(), LottoConst.SGPOOLS_4D_FIRST).save();
                new FourDLotteryNumber(fourDLotteryResult.getSecondPrize(), fourDLotteryResult.getLotteryID(), fourDLotteryResult.getDate(), LottoConst.SGPOOLS_4D_SECOND).save();
                new FourDLotteryNumber(fourDLotteryResult.getThirdPrize(), fourDLotteryResult.getLotteryID(), lotteryResult.getDate(), LottoConst.SGPOOLS_4D_THIRD).save();
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
