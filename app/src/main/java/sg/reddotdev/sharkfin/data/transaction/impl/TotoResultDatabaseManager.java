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
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;

import java.util.List;

import sg.reddotdev.sharkfin.data.database.LotteryDatabase;
import sg.reddotdev.sharkfin.data.model.LotteryResult;
import sg.reddotdev.sharkfin.data.model.TotoWinner;
import sg.reddotdev.sharkfin.data.model.impl.TotoLotteryNumber;
import sg.reddotdev.sharkfin.data.model.impl.TotoLotteryResult;
import sg.reddotdev.sharkfin.data.model.impl.TotoWinningBoard;
import sg.reddotdev.sharkfin.data.transaction.ResultDatabaseManagerBase;
import sg.reddotdev.sharkfin.util.constants.LottoConst;


public class TotoResultDatabaseManager extends ResultDatabaseManagerBase {
    private String LOGTAG = getClass().getSimpleName();

    @Override
    public void save(final LotteryResult lotteryResult) {
        DatabaseDefinition db = FlowManager.getDatabase(LotteryDatabase.class);
        Transaction transaction = db.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                Log.d("Process", "Saving");
                TotoLotteryResult totoLotteryResult = (TotoLotteryResult) lotteryResult;
                totoLotteryResult.save();

                new TotoLotteryNumber(totoLotteryResult.getAdditionalNumber(), totoLotteryResult.getLotteryID(), totoLotteryResult.getDate(), LottoConst.SGPOOLS_TOTO_ADD_NUM).save();

                TotoWinningBoard totoWinningBoard = totoLotteryResult.getTotoWinningBoard();
                List<TotoWinner> grp1Winners = totoWinningBoard.getGroup1Winners();

                for(TotoWinner totoWinner: grp1Winners) {
                    totoWinner.getBranch().save();
                }

                List<TotoWinner> grp2Winners = totoWinningBoard.getGroup2Winners();

                for(TotoWinner totoWinner: grp2Winners) {
                    totoWinner.getBranch().save();
                }

                totoWinningBoard.save();
            }
        }).success(new Transaction.Success() {
            @Override
            public void onSuccess(@NonNull Transaction transaction) {
                Log.d("Process", "Successfully saved!");
                listener.onSuccessSave();
            }
        }).error(new Transaction.Error() {
            @Override
            public void onError(@NonNull Transaction transaction, @NonNull Throwable error) {
                Log.d("Process", "Failed to save!");
                listener.onFailureSave();
            }
        }).build();
        transaction.execute();
    }

    @Override
    public void retrieve() {
        SQLite.select().from(TotoLotteryResult.class).async().queryListResultCallback(new QueryTransaction.QueryResultListCallback<TotoLotteryResult>() {
            @Override
            public void onListQueryResult(QueryTransaction transaction, @NonNull List<TotoLotteryResult> tResult) {
                Log.d(LOGTAG, "Retrieved, sending over to listener");
                listener.onSuccessRetrieve(tResult);
            }
        }).error(new Transaction.Error() {
            @Override
            public void onError(@NonNull Transaction transaction, @NonNull Throwable error) {
                Log.d(LOGTAG, "Failed to retrieved");
                listener.onFailureRetrieve();
            }
        }).execute();
    }
}
