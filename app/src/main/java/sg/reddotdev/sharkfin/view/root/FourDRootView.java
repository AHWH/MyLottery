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
import android.widget.TextView;;

import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import sg.reddotdev.sharkfin.R;
import sg.reddotdev.sharkfin.data.adapter.FourDMainRecyclerAdapter;
import sg.reddotdev.sharkfin.data.model.LotteryResult;
import sg.reddotdev.sharkfin.util.constants.AppLocale;
import sg.reddotdev.sharkfin.view.viewholder.RecyclerViewHolder;

/**
 * Created by weihong on 15/7/17.
 */

public class FourDRootView extends RootViewImpl implements RecyclerViewHolder.OnClickListener {
    private FourDMainRecyclerAdapter adapter;

    public FourDRootView(LayoutInflater inflater, ViewGroup container, AppCompatActivity activity) {
        super(inflater.inflate(R.layout.fourd_activity, container), activity);

        setupToolbar();
        setupNextDrawDate();
        setupRecyclerViewAdapter();
    }

    @Override
    public void updateRecyclerViewAdapter() {
        adapter.notifyDataSetChanged();
    }

    /*From Adapter to rootview*/
    @Override
    public void onResultClick(LotteryResult lotteryResult) {
        onItemClick(lotteryResult);
    }

    protected void setupToolbar() {
        getToolbar().setTitle(R.string.fourDActivity);
    }

    protected void setupNextDrawDate() {
        View rootView = getRootView();
        TextView nextDrawDateTV = (TextView) rootView.findViewById(R.id.nextDraw_Date_textView);
        TextView nextDrawDayTimeTV = (TextView) rootView.findViewById(R.id.nextDraw_DayTime_textView);

        ZonedDateTime currentDateTime = ZonedDateTime.now(AppLocale.gmt8Zone);
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
        nextDrawDateTV.setText(dateTimeArr[0]);
        nextDrawDayTimeTV.setText(dateTimeArr[1] + ", " + dateTimeArr[2]);
    }

    protected void setupRecyclerViewAdapter() {
        adapter = new FourDMainRecyclerAdapter(getLotteryResults());
        getResultsRecyclerView().setAdapter(adapter);
        adapter.registerListener(this);
    }

    @Override
    public void onDestroyAdapterListener() {
        adapter.unregisterListener();
    }

    public void onItemClick(LotteryResult lotteryResult) {
        getListener().onItemClick(lotteryResult);
    }

}
