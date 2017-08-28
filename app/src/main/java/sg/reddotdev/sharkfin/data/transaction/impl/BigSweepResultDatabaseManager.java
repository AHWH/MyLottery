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

import org.threeten.bp.ZonedDateTime;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import sg.reddotdev.sharkfin.data.database.LotteryDatabase;
import sg.reddotdev.sharkfin.data.model.LotteryResult;
import sg.reddotdev.sharkfin.data.model.impl.BigSweepLottery5DNumber;
import sg.reddotdev.sharkfin.data.model.impl.BigSweepLottery7DANumber;
import sg.reddotdev.sharkfin.data.model.impl.BigSweepLottery7DNumber;
import sg.reddotdev.sharkfin.data.model.impl.BigSweepLotteryResult;
import sg.reddotdev.sharkfin.data.transaction.ResultDatabaseManagerBase;
import sg.reddotdev.sharkfin.util.constants.LottoConst;
import sg.reddotdev.sharkfin.util.dagger.scope.PerFragment;

@PerFragment
public class BigSweepResultDatabaseManager extends ResultDatabaseManagerBase {
    private String LOGTAG = getClass().getSimpleName();

    private Transaction saveTransaction;
    private AsyncQuery<BigSweepLotteryResult> retrieveTransaction;

    @Inject
    public BigSweepResultDatabaseManager() {
        super();
    }

    @Override
    public void save(final LotteryResult lotteryResult) {
        DatabaseDefinition db = FlowManager.getDatabase(LotteryDatabase.class);
        saveTransaction = db.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                Log.d("Process", "Saving");
                BigSweepLotteryResult bigSweepLotteryResult = (BigSweepLotteryResult) lotteryResult;
                bigSweepLotteryResult.save();
                int lotteryID = bigSweepLotteryResult.getLotteryID();
                ZonedDateTime date = bigSweepLotteryResult.getDate();

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
                listener.onSuccessSave();
            }
        }).error(new Transaction.Error() {
            @Override
            public void onError(@NonNull Transaction transaction, @NonNull Throwable error) {
                Log.d("Process", "Failed to save!");
                listener.onFailureSave();
            }
        }).build();
        saveTransaction.execute();

    }

    @Override
    public void retrieve() {
        retrieveTransaction = SQLite.select().from(BigSweepLotteryResult.class).async().queryListResultCallback(new QueryTransaction.QueryResultListCallback<BigSweepLotteryResult>() {
            public void onListQueryResult(QueryTransaction transaction, @NonNull List<BigSweepLotteryResult> tResult) {
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
        if(saveTransaction != null) {
            saveTransaction.cancel();
        }
        if(retrieveTransaction != null) {
            retrieveTransaction.cancel();
        }
    }
}
