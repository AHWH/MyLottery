/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.ui.mainresults.fragment.fourd;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.temporal.ChronoUnit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;

import sg.reddotdev.sharkfin.data.comparator.TreeMapInversedComparator;
import sg.reddotdev.sharkfin.data.model.LotteryResult;
import sg.reddotdev.sharkfin.data.parser.ResultParser;
import sg.reddotdev.sharkfin.data.parser.impl.FourDHTMLParser;
import sg.reddotdev.sharkfin.data.transaction.ResultDatabaseManager;
import sg.reddotdev.sharkfin.data.transaction.impl.FourDResultDatabaseManager;
import sg.reddotdev.sharkfin.network.impl.UnifiedResultRetrievalManager;
import sg.reddotdev.sharkfin.ui.mainresults.MainResultsContract;
import sg.reddotdev.sharkfin.ui.mainresults.fragment.MainFragmentContract;
import sg.reddotdev.sharkfin.util.constants.AppLocale;
import sg.reddotdev.sharkfin.util.constants.LottoConst;

import static android.content.Context.MODE_PRIVATE;

public class FourDMainFragmentPresenter
        implements MainFragmentContract.Presenter, UnifiedResultRetrievalManager.ResultRetrievalManagerListener, FourDResultDatabaseManager.ResultDataManagerListener {

    private String LOGTAG = getClass().getSimpleName();

    private MainFragmentContract.View view;

    @Inject
    MainResultsContract.Presenter mainPresenter;

    private SharedPreferences globalSharedPreferences;

    private ResultDatabaseManager databaseManager;
    @Inject
    UnifiedResultRetrievalManager resultRetrievalManager;

    private Map<String, List<LotteryResult>> lotteryMap;

    private int newDrawNo;

    public FourDMainFragmentPresenter(Context context) {
        /*TODO: inject global shared preferences*/
        globalSharedPreferences = context.getSharedPreferences("GlobalPreferences", MODE_PRIVATE);

        databaseManager = new FourDResultDatabaseManager();

        lotteryMap = new TreeMap<>(new TreeMapInversedComparator());
    }


    @Override
    public void onViewCreated(MainFragmentContract.View view) {
        this.view = view;
        mainPresenter.updateTheme(LottoConst.SGPOOLS_4D);

        databaseManager.registerListener(this);
        resultRetrievalManager.registerListener(this);
        if(needToRetrieveFromDB()) {
            databaseManager.retrieve();
        }

        if(needToRetrieveResultOnline()) {
            resultRetrievalManager.create4DRequest(newDrawNo);
            resultRetrievalManager.retrieve();
        }
    }

    public void onViewDetach() {
        view = null;
        databaseManager.unregisterListener();
        resultRetrievalManager.unregisterListener();
    }

    /*Database Manager Listeners*/
    @Override
    public void onSuccessSave() {
        SharedPreferences.Editor editor = globalSharedPreferences.edit();
        editor.putInt("LastDrawNo_4D", newDrawNo);
        editor.putBoolean("FirstStart_4D", false);
        editor.apply();
    }

    @Override
    public void onFailureSave() {
        view.onFailureSaveDB();
    }

    @Override
    public void onSuccessRetrieve(List<? extends LotteryResult> lotteryResultList) {
        for(LotteryResult result: lotteryResultList) {
            addTo4DMap(result);
        }
        view.onSuccessRetrieveDB();
    }

    @Override
    public void onFailureRetrieve() {
        view.onFailureRetrieveDB();
    }

    /*Result Retrieval Listeners*/
    @Override
    public void onSuccessfulRetrievedResult(String response) {
        /*Parse the result into 4D Result Objects*/
        /*To be saved into DB and view to show*/
        ResultParser fourDHTMLParcer = new FourDHTMLParser(response);
        final LotteryResult fourDLotteryResult = fourDHTMLParcer.parse();
        databaseManager.save(fourDLotteryResult);

        /*Pass to Fragment to update*/
        addTo4DMap(fourDLotteryResult);
        view.onSuccessRetrieveResult();
    }

    @Override
    public void onFailureRetrievedResult() {
        view.onFailureRetrieveResult();
    }

    /*Creates a global list to submit to RecyclerView's adapter*/
    public void mergedList(List<Object> lotteryResults) {
        lotteryResults.clear();
        for(String key: lotteryMap.keySet()) {
            lotteryResults.add(key);
            List<LotteryResult> clonedList = new ArrayList<>(lotteryMap.get(key));
            Collections.reverse(clonedList);
            lotteryResults.addAll(clonedList);
        }
        view.onMergedList();
    }



    /*Internal Methods*/
    private boolean needToRetrieveFromDB() {
        /*If it's first start of app, straight jump to retrieve results online*/
        if(globalSharedPreferences.getBoolean("FirstStart_4D", true)) {
            return false;
        }
        /*If the existing map is still in memory, do not waste I/O resources to retrieve again*/
        if(!lotteryMap.isEmpty()) {
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
        setNextDraw(currentClock);

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
        Log.d(LOGTAG, oldDrawNo + " vs " + newDrawNo);
        if(newDrawNo > oldDrawNo) {
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


    /*Add to activity's global TreeMap*/
    private void addTo4DMap(LotteryResult result) {
        ZonedDateTime date = result.getDate();
        String key = date.getMonth().getValue() + " " + date.getYear();
        List<LotteryResult> list;
        if(lotteryMap.containsKey(key)) {
            list = lotteryMap.get(key);
        } else {
            list = new ArrayList<>();
        }
        list.add(result);
        lotteryMap.put(key, list);
    }

    private void setNextDraw(ZonedDateTime currentDateTime) {
        ZonedDateTime newDateTime = currentDateTime.withHour(18).withMinute(30);
        DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("d LLLL yyyy, EEEE, HH:mm");
        int currentDay = currentDateTime.getDayOfWeek().getValue();

        switch (currentDay) {
            case 1:case 4:
                newDateTime = newDateTime.plusDays(2);
                break;
            case 2:case 5:
                newDateTime = newDateTime.plusDays(1);
                break;
            case 3:case 7:
                if(newDateTime.getHour() >= 18 && newDateTime.getMinute() >= 30) {
                    newDateTime = newDateTime.plusDays(3);
                }
                break;
            case 6:
                if(newDateTime.getHour() >= 18 & newDateTime.getMinute() >= 30) {
                    newDateTime = newDateTime.plusDays(1);
                }
                break;

        }

        String[] dateTimeArr = newDateTime.format(dateTimeFormat).split(",\\s");
        mainPresenter.updateNextDraw(dateTimeArr);
    }
}
