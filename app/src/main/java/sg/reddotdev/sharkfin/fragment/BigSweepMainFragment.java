/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.threeten.bp.DayOfWeek;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.TextStyle;
import org.threeten.bp.temporal.TemporalAdjusters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import sg.reddotdev.sharkfin.activity.BigSweepResultActivity;
import sg.reddotdev.sharkfin.data.comparator.TreeMapInversedComparator;
import sg.reddotdev.sharkfin.data.model.LotteryResult;
import sg.reddotdev.sharkfin.data.model.impl.BigSweepLotteryResult;
import sg.reddotdev.sharkfin.data.parser.ResultParser;
import sg.reddotdev.sharkfin.data.parser.impl.BigSweepHTMLParser;
import sg.reddotdev.sharkfin.data.transaction.ResultDatabaseManager;
import sg.reddotdev.sharkfin.data.transaction.impl.BigSweepResultDatabaseManager;
import sg.reddotdev.sharkfin.manager.base.ResultRetrievalManager;
import sg.reddotdev.sharkfin.manager.impl.BigSweepRetrievalManager;
import sg.reddotdev.sharkfin.util.constants.AppLocale;
import sg.reddotdev.sharkfin.view.main.fragment.BigSweepFragmentView;
import sg.reddotdev.sharkfin.view.main.fragment.MainFragmentViewMVP;

import static android.content.Context.MODE_PRIVATE;


public class BigSweepMainFragment extends Fragment
        implements ResultRetrievalManager.ResultRetrievalManagerListener, ResultDatabaseManager.ResultDataManagerListener, MainFragmentViewMVP.MainFragmentViewMVPListener {

    private String LOGTAG = getClass().getSimpleName();

    private AppCompatActivity parentActivity;
    private SharedPreferences globalSharedPreferences;

    private BigSweepFragmentView viewController;

    private BigSweepRetrievalManager bigSweepRetrievalManager;
    private BigSweepResultDatabaseManager bigSweepDatabaseManager;

    private TreeMap<String, List<BigSweepLotteryResult>> bigSweepLotteryMap;
    private int newDrawNo;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentActivity = (AppCompatActivity) getActivity();
        globalSharedPreferences = parentActivity.getSharedPreferences("GlobalPreferences", MODE_PRIVATE);

        viewController = new BigSweepFragmentView(LayoutInflater.from(context), parentActivity);
        viewController.registerListener(this);

        bigSweepRetrievalManager = new BigSweepRetrievalManager();
        bigSweepRetrievalManager.registerListener(this);

        bigSweepDatabaseManager = new BigSweepResultDatabaseManager();
        bigSweepDatabaseManager.registerListener(this);

        bigSweepLotteryMap = new TreeMap<>(new TreeMapInversedComparator());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(needToRetrieveFromDB()) {
            bigSweepDatabaseManager.retrieve();
        }

        if(needToRetrieveResultOnline()) {
            bigSweepRetrievalManager.createRequest(newDrawNo);
            bigSweepRetrievalManager.retrieve();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return viewController.getRootView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState == null) {
            viewController.setup();
        } else {
            parentActivity = (AppCompatActivity) getActivity();
            viewController.setActivity(parentActivity);
            viewController.setup();
        }
    }

    @Override
    public void onDetach() {
        bigSweepRetrievalManager.unregisterListener();
        bigSweepDatabaseManager.unregisterListener();
        viewController.onDestroyAdapterListener();
        viewController.unregisterListener();
        super.onDetach();
    }

    /*Below are listeners*/
    /*For Result retrieval*/
    @Override
    public void onSuccessfulRetrievedResult(String response) {
        ResultParser bigSweepHTMLParser = new BigSweepHTMLParser(response);
        final LotteryResult bigSweepLotteryResult = bigSweepHTMLParser.parse();
        bigSweepDatabaseManager.save(bigSweepLotteryResult);

        addToBigSweepMap((BigSweepLotteryResult) bigSweepLotteryResult);
        mergedList(bigSweepLotteryMap);
    }

    @Override
    public void onFailureRetrievedResult() {

    }

    /*For Result's database management*/
    @Override
    public void onSuccessSave() {
        SharedPreferences.Editor editor = globalSharedPreferences.edit();
        editor.putInt("LastDrawNo_BigSweep", newDrawNo);
        editor.putBoolean("FirstStart_BigSweep", false);
        editor.apply();
    }

    @Override
    public void onFailureSave() {

    }

    @Override
    public void onSuccessRetrieve(List<? extends LotteryResult> lotteryResultList) {
        for(LotteryResult result: lotteryResultList) {
            addToBigSweepMap((BigSweepLotteryResult) result);
        }
        mergedList(bigSweepLotteryMap);
    }

    @Override
    public void onFailureRetrieve() {

    }

    private boolean needToRetrieveFromDB() {
        /*If it's first start of app, straight jump to retrieve results online*/
        if(globalSharedPreferences.getBoolean("FirstStart_BigSweep", true)) {
            return false;
        }
        /*If the existing map is still in memory, do not waste I/O resources to retrieve again*/
        if(!bigSweepLotteryMap.isEmpty()) {
            return false;
        }
        return true;
    }

    private boolean needToRetrieveResultOnline() {
                /*First Big Sweep available draw is Draw 07/1996 on  Wed, 3 Jul 1996*/
        /*Draws happen every first Wed of the month*/

        /*We use Sun, 06 Aug 2000 as base*/
        int oldDrawNo = globalSharedPreferences.getInt("LastDrawNo_BigSweep", 0);

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
        viewController.getLotteryResults().clear();
        viewController.getLotteryResults().addAll(returnedList);
        viewController.updateRecyclerViewAdapter();
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
        Intent resultActivityIntent = new Intent(getActivity(), BigSweepResultActivity.class);
        ZonedDateTime date = lotteryResult.getDate();
        String dateStr = date.getDayOfMonth() + " "
                + date.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()) + " "
                + date.getYear() + " ("
                + date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault()) + ")";
        resultActivityIntent.putExtra("Date", dateStr);
        startActivity(resultActivityIntent);
        EventBus.getDefault().postSticky(lotteryResult);
    }
}
