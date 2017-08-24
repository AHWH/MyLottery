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
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.queriable.AsyncQuery;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;

import java.util.ArrayList;
import java.util.List;

import sg.reddotdev.sharkfin.data.database.LotteryDatabase;
import sg.reddotdev.sharkfin.data.model.LotteryResult;
import sg.reddotdev.sharkfin.data.model.impl.FourDLotteryNumber;
import sg.reddotdev.sharkfin.data.model.impl.FourDLotteryResult;
import sg.reddotdev.sharkfin.data.transaction.ResultDatabaseManagerBase;
import sg.reddotdev.sharkfin.util.constants.LottoConst;


public class FourDResultDatabaseManager extends ResultDatabaseManagerBase {
    private String LOGTAG = getClass().getSimpleName();

    private Transaction saveTransaction;
    private AsyncQuery<FourDLotteryResult> retrieveTransaction;

    public void save(final LotteryResult lotteryResult) {
        DatabaseDefinition db = FlowManager.getDatabase(LotteryDatabase.class);
        saveTransaction = db.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                Log.d(LOGTAG, "Saving");
                FourDLotteryResult fourDLotteryResult = (FourDLotteryResult) lotteryResult;
                fourDLotteryResult.save();
                new FourDLotteryNumber(fourDLotteryResult.getFirstPrize(), fourDLotteryResult.getLotteryID(), fourDLotteryResult.getDate(), LottoConst.SGPOOLS_4D_FIRST).save();
                new FourDLotteryNumber(fourDLotteryResult.getSecondPrize(), fourDLotteryResult.getLotteryID(), fourDLotteryResult.getDate(), LottoConst.SGPOOLS_4D_SECOND).save();
                new FourDLotteryNumber(fourDLotteryResult.getThirdPrize(), fourDLotteryResult.getLotteryID(), lotteryResult.getDate(), LottoConst.SGPOOLS_4D_THIRD).save();
            }
        }).success(new Transaction.Success() {
            @Override
            public void onSuccess(@NonNull Transaction transaction) {
                Log.d(LOGTAG, "Successfully saved!");
                listener.onSuccessSave();
            }
        }).error(new Transaction.Error() {
            @Override
            public void onError(@NonNull Transaction transaction, @NonNull Throwable error) {
                Log.d(LOGTAG, "Failed to save!");
                listener.onFailureSave();
            }
        }).build();
        saveTransaction.execute();
    }

    @Override
    public void retrieve() {
        retrieveTransaction = SQLite.select().from(FourDLotteryResult.class).async().queryListResultCallback(new QueryTransaction.QueryResultListCallback<FourDLotteryResult>() {
            public void onListQueryResult(QueryTransaction transaction, @NonNull List<FourDLotteryResult> tResult) {
                Log.d(LOGTAG, "Retrieved, sending over to listener");
                listener.onSuccessRetrieve(tResult);
            }
        }).error(new Transaction.Error() {
            @Override
            public void onError(@NonNull Transaction transaction, @NonNull Throwable error) {
                Log.d(LOGTAG, "Failed to retrieved");
                listener.onFailureRetrieve();
            }
        });
        retrieveTransaction.execute();
    }

    @Override
    protected void cancelAllTransaction() {
        saveTransaction.cancel();
        retrieveTransaction.cancel();
    }
}
