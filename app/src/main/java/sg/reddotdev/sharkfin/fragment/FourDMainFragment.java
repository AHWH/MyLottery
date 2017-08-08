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

import sg.reddotdev.sharkfin.R;
import sg.reddotdev.sharkfin.activity.FourDResultActivity;
import sg.reddotdev.sharkfin.data.comparator.TreeMapInversedComparator;
import sg.reddotdev.sharkfin.data.model.LotteryResult;
import sg.reddotdev.sharkfin.data.model.impl.FourDLotteryResult;
import sg.reddotdev.sharkfin.data.parser.ResultParser;
import sg.reddotdev.sharkfin.data.parser.impl.FourDHTMLParser;
import sg.reddotdev.sharkfin.data.transaction.ResultDatabaseManager;
import sg.reddotdev.sharkfin.data.transaction.impl.FourDResultDatabaseManager;
import sg.reddotdev.sharkfin.manager.base.ResultRetrievalManager;
import sg.reddotdev.sharkfin.manager.impl.FourDRetrievalManager;
import sg.reddotdev.sharkfin.util.constants.AppLocale;
import sg.reddotdev.sharkfin.view.main.fragment.FourDFragmentView;
import sg.reddotdev.sharkfin.view.main.fragment.MainFragmentViewMVP;

import static android.content.Context.MODE_PRIVATE;

public class FourDMainFragment extends Fragment
        implements ResultRetrievalManager.ResultRetrievalManagerListener, ResultDatabaseManager.ResultDataManagerListener, MainFragmentViewMVP.MainFragmentViewMVPListener {

    private String LOGTAG = getClass().getSimpleName();

    private AppCompatActivity parentActivity;
    private SharedPreferences globalSharedPreferences;

    private FourDFragmentView viewController;

    private ResultRetrievalManager fourDRetrievalManager;
    private ResultDatabaseManager fourDDatabaseManager;

    private TreeMap<String, List<FourDLotteryResult>> fourDLotteryMap;
    private int newDrawNo;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentActivity = (AppCompatActivity) getActivity();
        globalSharedPreferences = parentActivity.getSharedPreferences("GlobalPreferences", MODE_PRIVATE);

        fourDRetrievalManager = new FourDRetrievalManager();
        fourDRetrievalManager.registerListener(this);

        fourDDatabaseManager = new FourDResultDatabaseManager();
        fourDDatabaseManager.registerListener(this);

        viewController = new FourDFragmentView(LayoutInflater.from(context), parentActivity);
        viewController.registerListener(this);

        fourDLotteryMap = new TreeMap<>(new TreeMapInversedComparator());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(needToRetrieveFromDB()) {
            fourDDatabaseManager.retrieve();
        }

        if(needToRetrieveResultOnline()) {
            fourDRetrievalManager.createRequest(newDrawNo);
            fourDRetrievalManager.retrieve();
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
        fourDRetrievalManager.unregisterListener();
        fourDDatabaseManager.unregisterListener();
        viewController.onDestroyAdapterListener();
        viewController.unregisterListener();
        super.onDetach();
    }


    /*Below are listeners*/
    /*For Result retrieval*/
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

    }

    /*For Result's database management*/
    @Override
    public void onSuccessSave() {
        SharedPreferences.Editor editor = globalSharedPreferences.edit();
        editor.putInt("LastDrawNo_4D", newDrawNo);
        editor.putBoolean("FirstStart_4D", false);
        editor.apply();
    }

    @Override
    public void onFailureSave() {

    }

    @Override
    public void onSuccessRetrieve(List<? extends LotteryResult> lotteryResultList) {
        for(LotteryResult result: lotteryResultList) {
            addTo4DMap((FourDLotteryResult) result);
        }
        mergedList(fourDLotteryMap);
    }

    @Override
    public void onFailureRetrieve() {

    }

    private boolean needToRetrieveFromDB() {
        /*If it's first start of app, straight jump to retrieve results online*/
        if(globalSharedPreferences.getBoolean("FirstStart_4D", true)) {
            return false;
        }
        /*If the existing map is still in memory, do not waste I/O resources to retrieve again*/
        if(!fourDLotteryMap.isEmpty()) {
            return false;
        }
        return true;
    }

    private boolean needToRetrieveResultOnline() {
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
        int oldDrawNo = globalSharedPreferences.getInt("LastDrawNo_4D", 0);

        if(newDrawNo > oldDrawNo) {
            Log.d(LOGTAG, oldDrawNo + " vs " + newDrawNo);
            /* Final check if its correct time to retrieve */
            /* Eg. it could be Wed but its still before 6.30*/
            if(currentDayOfWeek == 3 || currentDayOfWeek == 6 || currentDayOfWeek == 7) {
                if(currentClock.getHour() >= 18 && currentClock.getMinute() >= 30) {
                    return true;
                } else if(newDrawNo - oldDrawNo > 1) {
                    return true;
                }
            } else {
                return true;
            }
        }
        return false;
    }

    /*Creates a global list to submit to RecyclerView's adapter*/
    private void mergedList(Map<String, List<FourDLotteryResult>> map) {
        List<Object> returnedList = new ArrayList<>();
        for(String key: map.keySet()) {
            returnedList.add(key);
            List<FourDLotteryResult> clonedList = new ArrayList<>(map.get(key));
            Collections.reverse(clonedList);
            returnedList.addAll(clonedList);
        }
        viewController.getLotteryResults().clear();
        viewController.getLotteryResults().addAll(returnedList);
        Log.d(LOGTAG, viewController.getLotteryResults().size() +"");
        viewController.updateRecyclerViewAdapter();
    }


    /*Add to activity's global TreeMap*/
    private void addTo4DMap(FourDLotteryResult result) {
        ZonedDateTime date = result.getDate();
        String key = date.getMonth().getValue() + " " + date.getYear();
        List<FourDLotteryResult> list;
        if(fourDLotteryMap.containsKey(key)) {
            list = fourDLotteryMap.get(key);
        } else {
            list = new ArrayList<>();
        }
        list.add(result);
        fourDLotteryMap.put(key, list);
    }

    @Override
    public void onItemClick(LotteryResult lotteryResult) {
        Intent resultActivityIntent = new Intent(getActivity(), FourDResultActivity.class);
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
