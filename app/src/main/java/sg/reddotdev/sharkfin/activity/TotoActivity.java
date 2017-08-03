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

import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.temporal.ChronoUnit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import sg.reddotdev.sharkfin.R;
import sg.reddotdev.sharkfin.data.comparator.TreeMapInversedComparator;
import sg.reddotdev.sharkfin.data.model.LotteryResult;
import sg.reddotdev.sharkfin.data.model.impl.TotoLotteryResult;
import sg.reddotdev.sharkfin.data.parser.ResultParser;
import sg.reddotdev.sharkfin.data.parser.impl.TotoHTMLParser;
import sg.reddotdev.sharkfin.data.transaction.ResultDatabaseManager;
import sg.reddotdev.sharkfin.data.transaction.impl.TotoResultDatabaseManager;
import sg.reddotdev.sharkfin.manager.base.ResultRetrievalManager;
import sg.reddotdev.sharkfin.manager.impl.TotoRetrievalManager;
import sg.reddotdev.sharkfin.util.constants.AppErrorCode;
import sg.reddotdev.sharkfin.util.constants.AppLocale;
import sg.reddotdev.sharkfin.view.root.RootViewMVP;
import sg.reddotdev.sharkfin.view.root.TotoRootView;

public class TotoActivity extends AppCompatActivity
        implements ResultRetrievalManager.ResultRetrievalManagerListener,
        ResultDatabaseManager.ResultDataManagerListener, RootViewMVP.RootViewMVPListener {

    private String LOGTAG = getClass().getSimpleName();

    private boolean firstStart;

    private ResultRetrievalManager totoRetrievalManager;
    private ResultDatabaseManager totoDatabaseManager;

    private TotoRootView rootViewMVP;

    private SharedPreferences sharedPreferences;

    private TreeMap<String, List<TotoLotteryResult>> totoLotteryMap;
    private int newDrawNo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*TODO: check if its first start*/
        sharedPreferences = getSharedPreferences("GlobalPrefs", MODE_PRIVATE);
        firstStart = sharedPreferences.getBoolean("firstStart_Toto", true);

        rootViewMVP = new TotoRootView(LayoutInflater.from(this), null, this);
        rootViewMVP.registerListener(this);
        setContentView(rootViewMVP.getRootView());

        /*Through a event listener, it will pass out something regarding its status*/
        totoRetrievalManager = new TotoRetrievalManager();
        totoRetrievalManager.registerListener(this);

        totoDatabaseManager = new TotoResultDatabaseManager();
        totoDatabaseManager.registerListener(this);

        totoLotteryMap = new TreeMap<>(new TreeMapInversedComparator());
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        totoDatabaseManager.retrieve();
        if(needToRetrieveNewResult()) {
            totoRetrievalManager.createRequest(newDrawNo);
            totoRetrievalManager.retrieve();
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
        totoRetrievalManager.unregisterListener();
        totoDatabaseManager.unregisterListener();
        rootViewMVP.onDestroyAdapterListener();
        rootViewMVP.unregisterListener();
        super.onDestroy();
    }



    /*Listeners for ResultRetrieve*/
    @Override
    public void onSuccessfulRetrievedResult(String response) {
        /*Parse the result into 4D Result Objects*/
        /*To be saved into DB and view to show*/
        ResultParser fourDHTMLParcer = new TotoHTMLParser(response);
        final LotteryResult totoLotteryResult = fourDHTMLParcer.parse();
        totoDatabaseManager.save(totoLotteryResult);

        /*Once retrieved the first time, first start conditions no longer applies*/
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("firstStart_Toto", false);
        editor.apply();

        addToTotoMap((TotoLotteryResult) totoLotteryResult);
        mergedList(totoLotteryMap);
    }

    @Override
    public void onFailureRetrievedResult() {
        rootViewMVP.createSnackBar(AppErrorCode.RESULT_ERROR);
    }

    @Override
    public void onRetryRetrievingResult() {
        if(totoRetrievalManager != null) {
            totoRetrievalManager.retrieve();
        }
    }

    /*Listeners for ResultDataManager*/
    @Override
    public void onSuccessSave() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("lastDrawNo_Toto", newDrawNo);
        editor.apply();
    }

    @Override
    public void onFailureSave() {
        /*TODO: implement proper error handling mechanism*/
    }

    @Override
    public void onSuccessRetrieve(List<? extends LotteryResult> resultList) {
        for(LotteryResult result: resultList) {
            addToTotoMap((TotoLotteryResult) result);
        }
        mergedList(totoLotteryMap);
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
                Toast.makeText(this, "You are here!", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                break;
            case R.id.bottomNav_BigSweep:
                intent = new Intent(this, BigSweepActivity.class);
                startActivity(intent);
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
    private boolean needToRetrieveNewResult() {
        /*First Toto available draw is Draw 1001 on  Sun, 24 Dec 1995*/
        /*Draws happen every Mon and Thurs per week*/

        /*We use Mon, 11 May 1998 as base, Draw 1282*/
        ZonedDateTime baseDate = ZonedDateTime.of(1998, 5, 11, 18, 30, 0, 0, AppLocale.gmt8Zone);
        ZonedDateTime currentClock = ZonedDateTime.now(AppLocale.gmt8Zone);

        /* We calculate no of weeks passed since base date*/
        /* Then we x2 as there are 2 draws per week*/
        /* One week is from each Monday 1830 to the next Monday 1830! */
        long baseWeeksPassed = ChronoUnit.WEEKS.between(baseDate, currentClock);
        newDrawNo = (int) (1282 + baseWeeksPassed * 2);

        /*Immediately retrieve latest result if it's first start*/
        if(firstStart) {
            return true;
        }

        /* This ensures that if the week is not fully done yet, there will still be increment on Mon and Thurs*/
        int currentDayOfWeek = currentClock.getDayOfWeek().getValue();
        if(currentDayOfWeek >= 1) {
            newDrawNo++;
            if(currentDayOfWeek >= 4) {
                newDrawNo++;
            }
        }
        /* Checking if the new Draw No > old one, if so proceed to retrieve*/
        int oldDrawNo = sharedPreferences.getInt("lastDrawNo_Toto", 0);
        Log.d(LOGTAG, "OldDrawNo: " + oldDrawNo);
        if(newDrawNo > oldDrawNo) {
            /* Final check if its correct time to retrieve */
            /* Eg. it could be Wed but its still before 6.30*/
            if(currentDayOfWeek == 1 || currentDayOfWeek == 4 && newDrawNo - oldDrawNo == 1) {
                if(currentClock.getHour() >= 18 && currentClock.getMinute() >= 30) {
                    return true;
                }
            } else {
                return true;
            }
        }

        return false;
    }

    /*Creates a global list to submitted RecyclerView's adapter*/
    private void mergedList(Map<String, List<TotoLotteryResult>> map) {
        List<Object> returnedList = new ArrayList<>();
        for(String key: map.keySet()) {
            returnedList.add(key);
            List<TotoLotteryResult> clonedList = new ArrayList<>(map.get(key));
            Collections.reverse(clonedList);
            returnedList.addAll(clonedList);
        }
        rootViewMVP.getLotteryResults().clear();
        rootViewMVP.getLotteryResults().addAll(returnedList);
        rootViewMVP.updateRecyclerViewAdapter();
    }

    /*Add to activity's global TreeMap*/
    private void addToTotoMap(TotoLotteryResult result) {
        ZonedDateTime date = result.getDate();
        String key = date.getMonth().getValue() + " " + date.getYear();
        List<TotoLotteryResult> list;
        if(totoLotteryMap.containsKey(key)) {
            list = totoLotteryMap.get(key);
        } else {
            list = new ArrayList<>();
        }
        list.add(result);
        totoLotteryMap.put(key, list);
    }

    @Override
    public void onItemClick(LotteryResult lotteryResult) {

    }
}
