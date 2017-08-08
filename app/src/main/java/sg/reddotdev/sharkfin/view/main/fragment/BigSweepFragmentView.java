/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.view.main.fragment;

import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Window;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.temporal.TemporalAdjusters;

import sg.reddotdev.sharkfin.R;
import sg.reddotdev.sharkfin.data.adapter.BigSweepRecyclerAdapter;
import sg.reddotdev.sharkfin.data.model.LotteryResult;
import sg.reddotdev.sharkfin.util.constants.AppLocale;
import sg.reddotdev.sharkfin.view.viewholder.RecyclerViewHolder;


public class BigSweepFragmentView extends MainFragmentViewImpl implements RecyclerViewHolder.OnClickListener {
    private BigSweepRecyclerAdapter adapter;

    private final int bigSweepPriColor;
    private final int bigSweepDarkColor;
    private final int bigSweepTextDarkShade;

    public BigSweepFragmentView(LayoutInflater inflater, AppCompatActivity activity) {
        super(inflater.inflate(R.layout.mainfragment_results_recycler_layout, null), activity);
        bigSweepPriColor = ContextCompat.getColor(getActivity().getApplicationContext(), R.color.bigSweepTheme_Primary);
        bigSweepDarkColor = ContextCompat.getColor(getActivity().getApplicationContext(), R.color.bigSweepTheme_PrimaryDark);
        bigSweepTextDarkShade = ContextCompat.getColor(getActivity().getApplicationContext(), R.color.bigSweepTheme_textDarkShade);
    }

    public void setup() {
        init();

        setupTheme();
        setupToolbar();
        setupBottomBar();
        setupNextDrawDate();

        setupRecyclerViewAdapter();
    }

    @Override
    protected void setupTheme() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.setStatusBarColor(bigSweepDarkColor);
            window.setNavigationBarColor(bigSweepDarkColor);
        }
        getAppBarLayout().setBackgroundColor(bigSweepPriColor);
    }

    @Override
    protected void setupToolbar() {
        getActivityToolbar().setTitle(R.string.bigSweepMain);
        getActivityToolbar().setBackgroundColor(bigSweepPriColor);
        getActivityNextDraw_textView().setTextColor(bigSweepTextDarkShade);
        getActivityNextDrawDay_textView().setTextColor(bigSweepTextDarkShade);
    }

    @Override
    protected void setupBottomBar() {
        getActivityBottomNavigationView().setBackgroundColor(bigSweepPriColor);
    }

    @Override
    protected void setupNextDrawDate() {
        ZonedDateTime currentDateTime = ZonedDateTime.now(AppLocale.gmt8Zone);
        ZonedDateTime newDateTime = currentDateTime.with(TemporalAdjusters.firstInMonth(DayOfWeek.WEDNESDAY)).withHour(18).withMinute(30);
        DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("d LLLL yyyy, EEEE, HH:mm");

        //if current Date/Time is after the current month's draw date & time, get next month one
        if(!newDateTime.isAfter(currentDateTime)) {
            newDateTime = newDateTime.with(TemporalAdjusters.firstDayOfNextMonth()).with(TemporalAdjusters.firstInMonth(DayOfWeek.WEDNESDAY));
        }

        String[] dateTimeArr = newDateTime.format(dateTimeFormat).split(",\\s");
        getActivityNextDrawDate_textView().setText(dateTimeArr[0]);
        getActivityNextDrawDay_textView().setText(dateTimeArr[1] + ", " + dateTimeArr[2]);
    }

    protected void setupRecyclerViewAdapter() {
        adapter = new BigSweepRecyclerAdapter(getLotteryResults());
        getResultsRecyclerView().setAdapter(adapter);
        adapter.registerListener(this);
    }

    public void updateRecyclerViewAdapter() {
        adapter.notifyDataSetChanged();
    }

    /*From Adapter to View*/
    @Override
    public void onResultClick(LotteryResult lotteryResult) {
        onItemClick(lotteryResult);
    }

    /*From View to Fragment*/
    public void onItemClick(LotteryResult lotteryResult) {
        getListener().onItemClick(lotteryResult);
    }

    /*Destroy adapter to view listener*/
    public void onDestroyAdapterListener() {
        adapter.unregisterListener();
    }
}
