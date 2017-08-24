/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.ui.mainresults.fragment.bigsweep;

import android.content.Context;
import android.content.SharedPreferences;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;
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
import sg.reddotdev.sharkfin.data.parser.impl.BigSweepHTMLParser;
import sg.reddotdev.sharkfin.data.transaction.ResultDatabaseManager;
import sg.reddotdev.sharkfin.data.transaction.impl.BigSweepResultDatabaseManager;
import sg.reddotdev.sharkfin.network.impl.UnifiedResultRetrievalManager;
import sg.reddotdev.sharkfin.ui.mainresults.MainResultsContract;
import sg.reddotdev.sharkfin.ui.mainresults.fragment.MainFragmentContract;
import sg.reddotdev.sharkfin.util.constants.AppLocale;
import sg.reddotdev.sharkfin.util.constants.LottoConst;
import sg.reddotdev.sharkfin.util.dagger.scope.PerFragment;

import static android.content.Context.MODE_PRIVATE;

@PerFragment
public class BigSweepMainFragmentPresenter
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

    @Inject
    public BigSweepMainFragmentPresenter(Context context) {
        /*TODO: inject global shared preferences*/
        globalSharedPreferences = context.getSharedPreferences("GlobalPreferences", MODE_PRIVATE);

        databaseManager = new BigSweepResultDatabaseManager();

        lotteryMap = new TreeMap<>(new TreeMapInversedComparator());
    }

    @Override
    public void onViewCreated(MainFragmentContract.View view) {
        this.view = view;
        mainPresenter.updateTheme(LottoConst.SGPOOLS_SWEEP);

        databaseManager.registerListener(this);
        resultRetrievalManager.registerListener(this);

        if(needToRetrieveFromDB()) {
            databaseManager.retrieve();
        }

        if(needToRetrieveResultOnline()) {
            resultRetrievalManager.createBigSweepRequest(newDrawNo);
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
        editor.putInt("LastDrawNo_BigSweep", newDrawNo);
        editor.putBoolean("FirstStart_BigSweep", false);
        editor.apply();
    }

    @Override
    public void onFailureSave() {

    }

    /*Result Retrieval Listeners*/
    @Override
    public void onSuccessRetrieve(List<? extends LotteryResult> lotteryResultList) {
        for(LotteryResult result: lotteryResultList) {
            addToBigSweepMap(result);
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
        ResultParser bigSweepHTMLParcer = new BigSweepHTMLParser(response);
        final LotteryResult bigSweepLotteryResult = bigSweepHTMLParcer.parse();
        databaseManager.save(bigSweepLotteryResult);

        /*Pass to Fragment to update*/
        addToBigSweepMap(bigSweepLotteryResult);
        view.onSuccessRetrieveResult();
    }

    @Override
    public void onFailureRetrievedResult() {

    }

    public void onViewInactive() {

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
        if(globalSharedPreferences.getBoolean("FirstStart_BigSweep", true)) {
            return false;
        }
        /*If the existing map is still in memory, do not waste I/O resources to retrieve again*/
        if(!lotteryMap.isEmpty()) {
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
        setupNextDrawDate(currentClock);
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

    /*Add to activity's global TreeMap*/
    private void addToBigSweepMap(LotteryResult result) {
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
        ZonedDateTime newDateTime = currentDateTime.with(TemporalAdjusters.firstInMonth(DayOfWeek.WEDNESDAY)).withHour(18).withMinute(30);
        DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("d LLLL yyyy, EEEE, HH:mm");

        //if current Date/Time is after the current month's draw date & time, get next month one
        if(!newDateTime.isAfter(currentDateTime)) {
            newDateTime = newDateTime.with(TemporalAdjusters.firstDayOfNextMonth()).with(TemporalAdjusters.firstInMonth(DayOfWeek.WEDNESDAY));
        }

        String[] dateTimeArr = newDateTime.format(dateTimeFormat).split(",\\s");
        mainPresenter.updateNextDraw(dateTimeArr);
    }
}
