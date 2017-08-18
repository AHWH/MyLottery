/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.ui.mainresults.fragment.toto;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.temporal.ChronoUnit;
import org.threeten.bp.temporal.TemporalAdjusters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;

import sg.reddotdev.sharkfin.data.comparator.TreeMapInversedComparator;
import sg.reddotdev.sharkfin.data.model.LotteryResult;
import sg.reddotdev.sharkfin.data.parser.ResultParser;
import sg.reddotdev.sharkfin.data.parser.impl.TotoHTMLParser;
import sg.reddotdev.sharkfin.data.transaction.ResultDatabaseManager;
import sg.reddotdev.sharkfin.data.transaction.impl.TotoResultDatabaseManager;
import sg.reddotdev.sharkfin.network.impl.UnifiedResultRetrievalManager;
import sg.reddotdev.sharkfin.ui.mainresults.MainResultsContract;
import sg.reddotdev.sharkfin.ui.mainresults.fragment.MainFragmentContract;
import sg.reddotdev.sharkfin.util.constants.AppLocale;
import sg.reddotdev.sharkfin.util.constants.LottoConst;

import static android.content.Context.MODE_PRIVATE;


public class TotoMainFragmentPresenter
        implements MainFragmentContract.Presenter, UnifiedResultRetrievalManager.ResultRetrievalManagerListener, ResultDatabaseManager.ResultDataManagerListener {
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

    public TotoMainFragmentPresenter(Context context) {
        /*TODO: inject global shared preferences*/
        globalSharedPreferences = context.getSharedPreferences("GlobalPreferences", MODE_PRIVATE);

        databaseManager = new TotoResultDatabaseManager();

        lotteryMap = new TreeMap<>(new TreeMapInversedComparator());
    }

    @Override
    public void onViewCreated(MainFragmentContract.View view) {
        this.view = view;
        mainPresenter.updateTheme(LottoConst.SGPOOLS_TOTO);

        databaseManager.registerListener(this);
        resultRetrievalManager.registerListener(this);

        if(needToRetrieveFromDB()) {
            databaseManager.retrieve();
        }

        if(needToRetrieveResultOnline()) {
            resultRetrievalManager.createTotoRequest(newDrawNo);
            resultRetrievalManager.retrieve();
        }
    }


    public void onViewDetach() {
        databaseManager.unregisterListener();
        resultRetrievalManager.unregisterListener();
        view = null;
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

    }

    /*Result Retrieval Listeners*/
    @Override
    public void onSuccessRetrieve(List<? extends LotteryResult> lotteryResultList) {
        for(LotteryResult result: lotteryResultList) {
            addToTotoMap(result);
        }
        view.onSuccessRetrieveDB();
    }

    @Override
    public void onFailureRetrieve() {

    }

    @Override
    public void onSuccessfulRetrievedResult(String response) {
        /*Parse the result into 4D Result Objects*/
        /*To be saved into DB and view to show*/
        ResultParser totoHTMLParcer = new TotoHTMLParser(response);
        final LotteryResult bigSweepLotteryResult = totoHTMLParcer.parse();
        databaseManager.save(bigSweepLotteryResult);

        /*Pass to Fragment to update*/
        addToTotoMap(bigSweepLotteryResult);
        view.onSuccessRetrieveResult();
    }

    @Override
    public void onFailureRetrievedResult() {

    }

    /*Creates a global list to submitted RecyclerView's adapter*/
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

    private boolean needToRetrieveFromDB() {
        /*If it's first start of app, straight jump to retrieve results online*/
        if(globalSharedPreferences.getBoolean("FirstStart_Toto", true)) {
            return false;
        }
        /*If the existing map is still in memory, do not waste I/O resources to retrieve again*/
        if(!lotteryMap.isEmpty()) {
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
        setupNextDrawDate(currentClock);

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

    /*Add to activity's global TreeMap*/
    private void addToTotoMap(LotteryResult result) {
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

    private void setupNextDrawDate(ZonedDateTime currentDateTime) {
        ZonedDateTime newDateTime = currentDateTime.withHour(18).withMinute(30);
        DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("d LLLL yyyy, EEEE, HH:mm");
        int currentDay = currentDateTime.getDayOfWeek().getValue();

        switch (currentDay) {
            case 1:
                if(newDateTime.getHour() >= 18 && newDateTime.getMinute() >= 30) {
                    newDateTime = newDateTime.plusDays(3);
                }
                break;
            case 2:case 6:
                newDateTime = newDateTime.plusDays(2);
                break;
            case 3:case 7:
                newDateTime = newDateTime.plusDays(1);
                break;
            case 4:
                if(newDateTime.getHour() >= 18 & newDateTime.getMinute() >= 30) {
                    newDateTime = newDateTime.plusDays(4);
                }
                break;
            case 5:
                newDateTime = newDateTime.plusDays(3);

        }
        String[] dateTimeArr = newDateTime.format(dateTimeFormat).split(",\\s");
        mainPresenter.updateNextDraw(dateTimeArr);
    }
}
