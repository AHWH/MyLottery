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
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import sg.reddotdev.sharkfin.data.model.LotteryNumber;
import sg.reddotdev.sharkfin.data.model.LotteryResult;
import sg.reddotdev.sharkfin.data.model.impl.FourDLotteryResult;
import sg.reddotdev.sharkfin.view.result.FourDResultView;
import sg.reddotdev.sharkfin.view.result.ResultViewMVP;

/**
 * Created by weihong on 4/8/17.
 */

public class FourDResultActivity extends AppCompatActivity implements ResultViewMVP.ResultViewMVPListener {
    private String LOGTAG = getClass().getSimpleName();

    private FourDResultView viewMVP;

    private FourDLotteryResult fourDLotteryResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewMVP = new FourDResultView(LayoutInflater.from(this), null, this);
        viewMVP.registerListener(this);

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
        viewMVP.unregisterListener();
        super.onDestroy();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(LotteryResult lotteryResult) {
        if(lotteryResult != null) {
            fourDLotteryResult = (FourDLotteryResult) lotteryResult;
            updateUI();
        }
    }

    private void updateUI() {
        viewMVP.initDrawDetails(fourDLotteryResult.getLotteryID());
        viewMVP.initStandaloneNumbers(fourDLotteryResult.getFirstPrize(), fourDLotteryResult.getSecondPrize(), fourDLotteryResult.getThirdPrize());
        viewMVP.initStarterNumbers(collateNumbers(fourDLotteryResult.getStarterNumbers()));
        viewMVP.initConsolationNumbers(collateNumbers(fourDLotteryResult.getConsolationNumbers()));
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
