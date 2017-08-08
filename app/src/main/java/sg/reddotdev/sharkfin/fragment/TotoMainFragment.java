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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.TextStyle;
import org.threeten.bp.temporal.ChronoUnit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import sg.reddotdev.sharkfin.activity.TotoResultActivity;
import sg.reddotdev.sharkfin.data.comparator.TreeMapInversedComparator;
import sg.reddotdev.sharkfin.data.model.LotteryResult;
import sg.reddotdev.sharkfin.data.model.impl.TotoLotteryResult;
import sg.reddotdev.sharkfin.data.parser.ResultParser;
import sg.reddotdev.sharkfin.data.parser.impl.TotoHTMLParser;
import sg.reddotdev.sharkfin.data.transaction.ResultDatabaseManager;
import sg.reddotdev.sharkfin.data.transaction.impl.TotoResultDatabaseManager;
import sg.reddotdev.sharkfin.manager.base.ResultRetrievalManager;
import sg.reddotdev.sharkfin.manager.impl.TotoRetrievalManager;
import sg.reddotdev.sharkfin.util.constants.AppLocale;
import sg.reddotdev.sharkfin.view.main.fragment.MainFragmentViewMVP;
import sg.reddotdev.sharkfin.view.main.fragment.TotoFragmentView;
import sg.reddotdev.sharkfin.view.result.TotoResultView;

import static android.content.Context.MODE_PRIVATE;


public class TotoMainFragment extends Fragment
    implements ResultRetrievalManager.ResultRetrievalManagerListener, ResultDatabaseManager.ResultDataManagerListener, MainFragmentViewMVP.MainFragmentViewMVPListener {

    private String LOGTAG = getClass().getSimpleName();

    private AppCompatActivity parentActivity;
    private SharedPreferences globalSharedPreferences;

    private TotoFragmentView viewController;

    private ResultRetrievalManager totoRetrievalManager;
    private ResultDatabaseManager totoDatabaseManager;

    private TreeMap<String, List<TotoLotteryResult>> totoLotteryMap;
    private int newDrawNo;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentActivity = (AppCompatActivity) getActivity();
        globalSharedPreferences = parentActivity.getSharedPreferences("GlobalPreferences", MODE_PRIVATE);

        viewController = new TotoFragmentView(LayoutInflater.from(context), parentActivity);
        viewController.registerListener(this);

        totoRetrievalManager = new TotoRetrievalManager();
        totoRetrievalManager.registerListener(this);

        totoDatabaseManager = new TotoResultDatabaseManager();
        totoDatabaseManager.registerListener(this);

        totoLotteryMap = new TreeMap<>(new TreeMapInversedComparator());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(needToRetrieveFromDB()) {
            totoDatabaseManager.retrieve();
        }

        if(needToRetrieveResultOnline()) {
            totoRetrievalManager.createRequest(newDrawNo);
            totoRetrievalManager.retrieve();
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
        totoRetrievalManager.unregisterListener();
        totoDatabaseManager.unregisterListener();
        viewController.onDestroyAdapterListener();
        viewController.unregisterListener();
        super.onDetach();
    }



    /*Below are listeners*/
    /*For Result retrieval*/
    @Override
    public void onSuccessfulRetrievedResult(String response) {
        ResultParser totoHTMLParser = new TotoHTMLParser(response);
        final LotteryResult totoLotteryResult = totoHTMLParser.parse();
        totoDatabaseManager.save(totoLotteryResult);

        addToTotoMap((TotoLotteryResult) totoLotteryResult);
        mergedList(totoLotteryMap);
    }


    @Override
    public void onFailureRetrievedResult() {

    }

    /*For Result's database management*/
    @Override
    public void onSuccessSave() {
        SharedPreferences.Editor editor = globalSharedPreferences.edit();
        editor.putInt("LastDrawNo_Toto", newDrawNo);
        editor.putBoolean("FirstStart_Toto", false);
        editor.apply();
    }

    @Override
    public void onFailureSave() {

    }

    @Override
    public void onSuccessRetrieve(List<? extends LotteryResult> lotteryResultList) {
        for(LotteryResult result: lotteryResultList) {
            addToTotoMap((TotoLotteryResult) result);
        }
        mergedList(totoLotteryMap);
    }

    @Override
    public void onFailureRetrieve() {

    }


    private boolean needToRetrieveFromDB() {
        /*If it's first start of app, straight jump to retrieve results online*/
        if(globalSharedPreferences.getBoolean("FirstStart_Toto", true)) {
            return false;
        }
        /*If the existing map is still in memory, do not waste I/O resources to retrieve again*/
        if(!totoLotteryMap.isEmpty()) {
            return false;
        }
        return true;
    }

    private boolean needToRetrieveResultOnline() {
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

        /* This ensures that if the week is not fully done yet, there will still be increment on Mon and Thurs*/
        int currentDayOfWeek = currentClock.getDayOfWeek().getValue();
        if(currentDayOfWeek >= 1) {
            newDrawNo++;
            if(currentDayOfWeek >= 4) {
                newDrawNo++;
            }
        }
        /* Checking if the new Draw No > old one, if so proceed to retrieve*/
        int oldDrawNo = globalSharedPreferences.getInt("LastDrawNo_Toto", 0);
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

    /*Creates a global list to submit to RecyclerView's adapter*/
    private void mergedList(Map<String, List<TotoLotteryResult>> map) {
        List<Object> returnedList = new ArrayList<>();
        for(String key: map.keySet()) {
            returnedList.add(key);
            List<TotoLotteryResult> clonedList = new ArrayList<>(map.get(key));
            Collections.reverse(clonedList);
            returnedList.addAll(clonedList);
        }
        viewController.getLotteryResults().clear();
        viewController.getLotteryResults().addAll(returnedList);
        viewController.updateRecyclerViewAdapter();
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
        Intent resultActivityIntent = new Intent(getActivity(), TotoResultActivity.class);
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
