/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import sg.reddotdev.sharkfin.data.model.LotteryNumber;
import sg.reddotdev.sharkfin.data.model.LotteryResult;
import sg.reddotdev.sharkfin.data.model.impl.BigSweepLotteryResult;
import sg.reddotdev.sharkfin.view.result.BigSweepResultView;

public class BigSweepResultActivity extends AppCompatActivity {
    private String LOGTAG = getClass().getSimpleName();

    private BigSweepResultView viewMVP;

    private BigSweepLotteryResult bigSweepLotteryResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewMVP = new BigSweepResultView(LayoutInflater.from(this), null, this);

        String dateStr = getIntent().getStringExtra("Date");
        viewMVP.setupToolbar(dateStr);
        setContentView(viewMVP.getRootView());
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(LotteryResult lotteryResult) {
        if(lotteryResult != null) {
            bigSweepLotteryResult = (BigSweepLotteryResult) lotteryResult;
            updateUI();
        }
    }

    private void updateUI() {
        viewMVP.initDrawDetails(bigSweepLotteryResult.getLotteryID());
        viewMVP.initStandaloneNumbers(bigSweepLotteryResult.getFirstNumber(), bigSweepLotteryResult.getSecondNumber(), bigSweepLotteryResult.getThirdNumber());
        viewMVP.initSuperSweep(bigSweepLotteryResult.getSuperSweepNumber());
        viewMVP.initCascade(bigSweepLotteryResult.getCascadeNumber());
        viewMVP.initJackpotNumbers(collateNumbers(bigSweepLotteryResult.getJackpotNumbers()));
        viewMVP.initLuckyNumbers(collateNumbers(bigSweepLotteryResult.getLuckyNumbers()));
        viewMVP.initGiftNumbers(collateNumbers(bigSweepLotteryResult.getGiftNumbers()));
        viewMVP.initConsolationNumbers(collateNumbers(bigSweepLotteryResult.getConsolationNumbers()));
        viewMVP.initParticipationNumbers(collateNumbers(bigSweepLotteryResult.getParticipationNumbers()));
        viewMVP.initDelight2DNumbers(collateNumbers(bigSweepLotteryResult.getDelight2DNumbers()));
        viewMVP.initDelight3DNumbers(collateNumbers(bigSweepLotteryResult.getDelight3DNumbers()));
    }

    private List<Integer> collateNumbers(List<? extends LotteryNumber> lotteryNumbers) {
        List<Integer> collatedNumbers = new ArrayList<>();
        for(LotteryNumber lotteryNumber: lotteryNumbers) {
            collatedNumbers.add(lotteryNumber.getNum());
        }
        return collatedNumbers;
    }

    @Override
    public boolean onSupportNavigateUp() {
        EventBus.getDefault().removeAllStickyEvents();
        onBackPressed();
        return true;
    }
}
