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
import sg.reddotdev.sharkfin.data.model.impl.TotoLotteryResult;
import sg.reddotdev.sharkfin.view.result.TotoResultView;


public class TotoResultActivity extends AppCompatActivity {
    private String LOGTAG = getClass().getSimpleName();

    private TotoResultView viewMVP;

    private TotoLotteryResult totoLotteryResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewMVP = new TotoResultView(LayoutInflater.from(this), null, this);
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
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(LotteryResult lotteryResult) {
        if(lotteryResult != null) {
            totoLotteryResult = (TotoLotteryResult) lotteryResult;
            updateUI();
        }
    }

    private void updateUI() {
        viewMVP.initDrawDetails(totoLotteryResult.getLotteryID());
        viewMVP.initWinningNumbers(collateNumbers(totoLotteryResult.getWinningNumbers()));
        viewMVP.initAdditionalWinningNumbers(totoLotteryResult.getAdditionalNumber());
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
