/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.view.root;

import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.temporal.TemporalAdjusters;

import sg.reddotdev.sharkfin.R;
import sg.reddotdev.sharkfin.data.adapter.BigSweepRecyclerAdapter;
import sg.reddotdev.sharkfin.data.model.LotteryResult;
import sg.reddotdev.sharkfin.util.constants.AppLocale;
import sg.reddotdev.sharkfin.view.viewholder.RecyclerViewHolder;

public class BigSweepRootView extends RootViewImpl implements RecyclerViewHolder.OnClickListener{
    private BigSweepRecyclerAdapter adapter;

    public BigSweepRootView(LayoutInflater inflater, ViewGroup container, AppCompatActivity activity) {
        super(inflater.inflate(R.layout.bigsweep_activity, container), activity);

        setupToolbar();
        setupNextDrawDate();
        setupRecyclerViewAdapter();
    }

    @Override
    public void updateRecyclerViewAdapter() {
        adapter.notifyDataSetChanged();
    }

    protected void setupToolbar() {
        getToolbar().setTitle(R.string.bigSweepActivity);
    }

    protected void setupNextDrawDate() {
        View rootView = getRootView();
        TextView nextDrawDateTV = (TextView) rootView.findViewById(R.id.nextDraw_Date_textView);
        TextView nextDrawDayTimeTV = (TextView) rootView.findViewById(R.id.nextDraw_DayTime_textView);

        ZonedDateTime currentDateTime = ZonedDateTime.now(AppLocale.gmt8Zone);
        ZonedDateTime newDateTime = currentDateTime.with(TemporalAdjusters.firstInMonth(DayOfWeek.WEDNESDAY)).withHour(18).withMinute(30);
        DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("d LLLL yyyy, EEEE, HH:mm");

        //if current Date/Time is after the current month's draw date & time, get next month one
        if(!newDateTime.isAfter(currentDateTime)) {
          newDateTime = newDateTime.with(TemporalAdjusters.firstDayOfNextMonth()).with(TemporalAdjusters.firstInMonth(DayOfWeek.WEDNESDAY));
        }

        String[] dateTimeArr = newDateTime.format(dateTimeFormat).split(",\\s");
        nextDrawDateTV.setText(dateTimeArr[0]);
        nextDrawDayTimeTV.setText(dateTimeArr[1] + ", " + dateTimeArr[2]);
    }

    @Override
    protected void setupRecyclerViewAdapter() {
        adapter = new BigSweepRecyclerAdapter(getLotteryResults());
        getResultsRecyclerView().setAdapter(adapter);
    }

    @Override
    public void onResultClick(LotteryResult lotteryResult) {
        onItemClick(lotteryResult);
    }

    public void onItemClick(LotteryResult lotteryResult) {
        getListener().onItemClick(lotteryResult);
    }

    @Override
    public void onDestroyAdapterListener() {
        adapter.unregisterListener();
    }
}
