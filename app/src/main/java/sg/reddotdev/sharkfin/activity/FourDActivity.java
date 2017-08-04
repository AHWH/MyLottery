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
import sg.reddotdev.sharkfin.data.model.impl.FourDLotteryResult;
import sg.reddotdev.sharkfin.data.parser.ResultParser;
import sg.reddotdev.sharkfin.data.parser.impl.FourDHTMLParser;
import sg.reddotdev.sharkfin.data.transaction.ResultDatabaseManager;
import sg.reddotdev.sharkfin.data.transaction.impl.FourDResultDatabaseManager;
import sg.reddotdev.sharkfin.manager.base.ResultRetrievalManager;
import sg.reddotdev.sharkfin.manager.impl.FourDRetrievalManager;
import sg.reddotdev.sharkfin.util.constants.AppErrorCode;
import sg.reddotdev.sharkfin.util.constants.AppLocale;
import sg.reddotdev.sharkfin.view.root.FourDRootView;
import sg.reddotdev.sharkfin.view.root.RootViewMVP;

public class FourDActivity extends AppCompatActivity
        implements ResultRetrievalManager.ResultRetrievalManagerListener,
        ResultDatabaseManager.ResultDataManagerListener, RootViewMVP.RootViewMVPListener {

    private String LOGTAG = getClass().getSimpleName();

    private ResultRetrievalManager fourDRetrievalManager;
    private ResultDatabaseManager fourDDatabaseManager;
    private SharedPreferences sharedPreferences;

    private FourDRootView rootViewMVP;

    private TreeMap<String, List<FourDLotteryResult>> fourDLotteryMap;
    private int newDrawNo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*TODO: check if its first start*/
        sharedPreferences = getSharedPreferences("GlobalPrefs", MODE_PRIVATE);

        rootViewMVP = new FourDRootView(LayoutInflater.from(this), null, this);
        rootViewMVP.registerListener(this);
        setContentView(rootViewMVP.getRootView());

        fourDRetrievalManager = new FourDRetrievalManager();
        fourDRetrievalManager.registerListener(this);

        fourDDatabaseManager = new FourDResultDatabaseManager();
        fourDDatabaseManager.registerListener(this);

        fourDLotteryMap = new TreeMap<>(new TreeMapInversedComparator());
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fourDDatabaseManager.retrieve();
        /*Check onSuccessfulRetrieved & onFailureRetrieved listener for what's next*/
        if(needToRetrieveNewResult()) {
            Log.d(LOGTAG, "HERE?");
            fourDRetrievalManager.createRequest(newDrawNo);
            fourDRetrievalManager.retrieve();
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
        fourDRetrievalManager.unregisterListener();
        fourDDatabaseManager.unregisterListener();
        rootViewMVP.onDestroyAdapterListener();
        rootViewMVP.unregisterListener();
        super.onDestroy();

    }



    /*Listeners for ResultRetrieve*/
    @Override
    public void onSuccessfulRetrievedResult(String response) {
        /*Parse the result into 4D Result Objects*/
        /*To be saved into DB and view to show*/
        ResultParser fourDHTMLParcer = new FourDHTMLParser(response);
        final LotteryResult fourDLotteryResult = fourDHTMLParcer.parse();
        fourDDatabaseManager.save(fourDLotteryResult);

        /*Add to global Map and redo adapter list*/
        addTo4DMap((FourDLotteryResult) fourDLotteryResult);
        mergedList(fourDLotteryMap);
    }

    @Override
    public void onFailureRetrievedResult() {
        rootViewMVP.createSnackBar(AppErrorCode.RESULT_ERROR);
    }

    @Override
    public void onRetryRetrievingResult() {
        if(fourDRetrievalManager != null) {
            fourDRetrievalManager.retrieve();
        }
    }


    /*Listeners for ResultDataManager*/
    /*For Saving*/
    @Override
    public void onSuccessSave() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("lastDrawNo_4D", newDrawNo);
        editor.apply();
    }

    @Override
    public void onFailureSave() {
        /*TODO: implement proper error handling mechanism*/

    }

    /*For Retrieving*/
    @Override
    public void onSuccessRetrieve(List<? extends LotteryResult> resultList) {
        for(LotteryResult result: resultList) {
            addTo4DMap((FourDLotteryResult) result);
        }
        mergedList(fourDLotteryMap);
    }

    @Override
    public void onFailureRetrieve() {
        /*TODO: implement proper error handling mechanism*/
    }



    /*Listeners for RootViewMVP*/
    //For navigation view//
    @Override
    public void onBottomNavigationSelected(int itemID) {
        Intent intent = null;
        switch (itemID) {
            case R.id.bottomNav_4D:
                Toast.makeText(this, "You are here!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bottomNav_Toto:
                intent = new Intent(this, TotoActivity.class);
                startActivity(intent);
                break;
            case R.id.bottomNav_BigSweep:
                intent = new Intent(this, BigSweepActivity.class);
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
    /*TODO: check what if 2/3 results behind*/
    private boolean needToRetrieveNewResult() {
        /*First 4D available draw is Draw 1000 on  Sun, 24 Dec 1995*/
        /*From then till Sun, 06 Aug 2000, Draw 1482. Draws happen every Sat and Sun per week*/
        /*Subsequently, each week 3 draws happen on Wed, Sat and Sun*/

        /*We use Sun, 06 Aug 2000 as base*/
        ZonedDateTime baseDate = ZonedDateTime.of(2000, 8, 6, 18, 30, 0, 0, AppLocale.gmt8Zone);
        ZonedDateTime currentClock = ZonedDateTime.now(AppLocale.gmt8Zone);

        /* We calculate no of weeks passed since base date*/
        /* Then we x3 as there are 3 draws per week*/
        /* One week is from each Sunday 1830 to the next Sunday 1830! */
        long baseWeeksPassed = ChronoUnit.WEEKS.between(baseDate, currentClock);
        newDrawNo = (int) (1482 + baseWeeksPassed * 3);
        /* This ensures that if the week is not fully done yet, there will still be increment on Wed and Sat*/
        int currentDayOfWeek = currentClock.getDayOfWeek().getValue();
        if(currentDayOfWeek >= 3) {
            newDrawNo++;
            if(currentDayOfWeek >= 6) {
                newDrawNo++;
            }
        }
        /* Checking if the new Draw No > old one, if so proceed to retrieve*/
        int oldDrawNo = sharedPreferences.getInt("lastDrawNo_4D", 0);
        if(newDrawNo > oldDrawNo) {
            /* Final check if its correct time to retrieve */
            /* Eg. it could be Wed but its still before 6.30*/
            if(currentDayOfWeek == 3 || currentDayOfWeek == 6 || currentDayOfWeek == 7 && newDrawNo - oldDrawNo == 1) {
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
    private void mergedList(Map<String, List<FourDLotteryResult>> map) {
        List<Object> returnedList = new ArrayList<>();
        for(String key: map.keySet()) {
            returnedList.add(key);
            List<FourDLotteryResult> clonedList = new ArrayList<>(map.get(key));
            Collections.reverse(clonedList);
            returnedList.addAll(clonedList);
        }
        rootViewMVP.getLotteryResults().clear();
        rootViewMVP.getLotteryResults().addAll(returnedList);
        rootViewMVP.updateRecyclerViewAdapter();
    }


    /*Add to activity's global TreeMap*/
    private void addTo4DMap(FourDLotteryResult result) {
        ZonedDateTime date = result.getDate();
        String key = date.getMonth().getValue() + " " + date.getYear();
        List<FourDLotteryResult> list;
        if(fourDLotteryMap.containsKey(key)) {
            list = fourDLotteryMap.get(key);
        } else {
            list = new ArrayList<>()
        }
        list.add(result);
        fourDLotteryMap.put(key, list);
    }

    @Override
    public void onItemClick(LotteryResult lotteryResult) {
        /*TODO: Create new Activity here*/
        Intent resultActivityIntent = new Intent(this, FourDResultActivity.class);
        resultActivityIntent.put
    }
}
