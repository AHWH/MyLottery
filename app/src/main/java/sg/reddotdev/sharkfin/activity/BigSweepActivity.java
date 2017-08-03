/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.temporal.TemporalAdjusters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import sg.reddotdev.sharkfin.R;
import sg.reddotdev.sharkfin.data.comparator.TreeMapInversedComparator;
import sg.reddotdev.sharkfin.data.model.LotteryResult;
import sg.reddotdev.sharkfin.data.model.impl.BigSweepLotteryResult;
import sg.reddotdev.sharkfin.data.parser.ResultParser;
import sg.reddotdev.sharkfin.data.parser.impl.BigSweepHTMLParser;
import sg.reddotdev.sharkfin.data.transaction.ResultDatabaseManager;
import sg.reddotdev.sharkfin.data.transaction.impl.BigSweepResultDatabaseManager;
import sg.reddotdev.sharkfin.manager.base.ResultRetrievalManager;
import sg.reddotdev.sharkfin.manager.impl.BigSweepRetrievalManager;
import sg.reddotdev.sharkfin.util.constants.AppErrorCode;
import sg.reddotdev.sharkfin.util.constants.AppLocale;
import sg.reddotdev.sharkfin.view.root.BigSweepRootView;
import sg.reddotdev.sharkfin.view.root.RootViewMVP;


public class BigSweepActivity extends AppCompatActivity
        implements ResultRetrievalManager.ResultRetrievalManagerListener,
        ResultDatabaseManager.ResultDataManagerListener, RootViewMVP.RootViewMVPListener {

    private String LOGTAG = getClass().getSimpleName();

    private ResultRetrievalManager bigSweepResultRetrievalManager;
    private ResultDatabaseManager bigSweepResultDatabaseManager;
    private SharedPreferences sharedPreferences;

    private BigSweepRootView rootViewMVP;

    private TreeMap<String, List<BigSweepLotteryResult>> bigSweepLotteryMap;
    private int newDrawNo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*TODO: check if its first start*/
        sharedPreferences = getSharedPreferences("GlobalPrefs", MODE_PRIVATE);

        rootViewMVP = new BigSweepRootView(LayoutInflater.from(this), null, this);
        rootViewMVP.registerListener(this);
        setContentView(rootViewMVP.getRootView());

        bigSweepResultRetrievalManager = new BigSweepRetrievalManager();
        bigSweepResultRetrievalManager.registerListener(this);

        bigSweepResultDatabaseManager = new BigSweepResultDatabaseManager();
        bigSweepResultDatabaseManager.registerListener(this);

        bigSweepLotteryMap = new TreeMap<>(new TreeMapInversedComparator());
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bigSweepResultDatabaseManager.retrieve();
        if(needToRetrieveNewResult()) {
            Log.d(LOGTAG, "Helllo?");
            bigSweepResultRetrievalManager.createRequest(newDrawNo);
            bigSweepResultRetrievalManager.retrieve();
        }
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
        bigSweepResultRetrievalManager.unregisterListener();
        bigSweepResultDatabaseManager.unregisterListener();
        rootViewMVP.onDestroyAdapterListener();
        rootViewMVP.unregisterListener();
        super.onDestroy();
    }



    /*Listeners for ResultRetrieval*/
    @Override
    public void onSuccessfulRetrievedResult(String response) {
        /*Parse the result into bigSweep Result Objects*/
        /*To be saved into DB and view to show*/
        ResultParser fourDHTMLParcer = new BigSweepHTMLParser(response);
        final LotteryResult bigSweepLotteryResult = fourDHTMLParcer.parse();
        bigSweepResultDatabaseManager.save(bigSweepLotteryResult);

        addToBigSweepMap((BigSweepLotteryResult) bigSweepLotteryResult);
        mergedList(bigSweepLotteryMap);
    }

    @Override
    public void onFailureRetrievedResult() {
        rootViewMVP.createSnackBar(AppErrorCode.RESULT_ERROR);
    }

    @Override
    public void onRetryRetrievingResult() {
        if(bigSweepResultRetrievalManager != null) {
            bigSweepResultRetrievalManager.retrieve();
        }
    }

    /*Listeners for ResultDataManager*/
    @Override
    public void onSuccessSave() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("lastDrawNo_BigSweep", newDrawNo);
        editor.apply();
    }

    @Override
    public void onFailureSave() {
        /*TODO: implement proper error handling mechanism*/
    }

    @Override
    public void onSuccessRetrieve(List<? extends LotteryResult> resultList) {
        for(LotteryResult result: resultList) {
            addToBigSweepMap((BigSweepLotteryResult) result);
        }

        mergedList(bigSweepLotteryMap);
    }

    @Override
    public void onFailureRetrieve() {
        /*TODO: implement proper error handling mechanism*/
    }

    @Override
    public void onBottomNavigationSelected(int itemID) {
        Intent intent = null;
        switch (itemID) {
            case R.id.bottomNav_4D:
                intent = new Intent(this, FourDActivity.class);
                break;
            case R.id.bottomNav_Toto:
                intent = new Intent(this, TotoActivity.class);
                startActivity(intent);
                break;
            case R.id.bottomNav_BigSweep:
                Toast.makeText(this, "You are here!", Toast.LENGTH_SHORT).show();
        }

        if(intent != null) {
            startActivity(intent);
        }
    }

    @Override
    public void onNavigationSelected(int itemID) {
        switch (itemID) {
            case R.id.drawer_settings:
                Toast.makeText(this, "Redirects you to SettingsActivity", Toast.LENGTH_SHORT).show();
        }
    }

    /*Internal Private methods*/
    /*To check if there is a need to retrieve newest*/
    /*TODO: What if results is 2/3 behind the latest one?*/
    private boolean needToRetrieveNewResult() {
        /*First Big Sweep available draw is Draw 07/1996 on  Wed, 3 Jul 1996*/
        /*Draws happen every first Wed of the month*/

        /*We use Sun, 06 Aug 2000 as base*/
        int oldDrawNo = sharedPreferences.getInt("lastDrawNo_BigSweep", 0);

        ZonedDateTime currentClock = ZonedDateTime.now(AppLocale.gmt8Zone);
        ZonedDateTime nextLatestDrawDate = currentClock.with(TemporalAdjusters.firstInMonth(DayOfWeek.WEDNESDAY)).withHour(18).withMinute(30).withSecond(0).withNano(0);
        newDrawNo = nextLatestDrawDate.getMonth().getValue() * 10000 + nextLatestDrawDate.getYear();

        ZonedDateTime oldDrawDate;
        if(oldDrawNo != 0) {
            int year = oldDrawNo % 10000;
            int month = oldDrawNo / 10000;
            oldDrawDate = ZonedDateTime.of(year, month, 1, 18, 30, 0, 0, AppLocale.gmt8Zone);
            oldDrawDate = oldDrawDate.with(TemporalAdjusters.firstInMonth(DayOfWeek.WEDNESDAY));

            /*If current DateTime is equal or later than the next draw DateTime*/
            /*and that the next draw DateTime is not the same as in system's last retrieved records*/
            return currentClock.compareTo(nextLatestDrawDate) >= 0 & nextLatestDrawDate.compareTo(oldDrawDate) != 0;
        }
        return true;
    }

    /*Creates a global list to submitted RecyclerView's adapter*/
    private void mergedList(Map<String, List<BigSweepLotteryResult>> map) {
        List<Object> returnedList = new ArrayList<>();
        for(String key: map.keySet()) {
            returnedList.add(key);
            List<BigSweepLotteryResult> clonedList = new ArrayList<>(map.get(key));
            Collections.reverse(clonedList);
            returnedList.addAll(clonedList);
        }
        rootViewMVP.getLotteryResults().clear();
        rootViewMVP.getLotteryResults().addAll(returnedList);
        rootViewMVP.updateRecyclerViewAdapter();
    }


    /*Add to activity's global TreeMap*/
    private void addToBigSweepMap(BigSweepLotteryResult result) {
        ZonedDateTime date = result.getDate();
        String key = date.getMonth().getValue() + " " + date.getYear();
        List<BigSweepLotteryResult> list;
        if(bigSweepLotteryMap.containsKey(key)) {
            list = bigSweepLotteryMap.get(key);
        } else {
            list = new ArrayList<>();
        }
        list.add(result);
        bigSweepLotteryMap.put(key, list);
    }

    @Override
    public void onItemClick(LotteryResult lotteryResult) {

    }
}


