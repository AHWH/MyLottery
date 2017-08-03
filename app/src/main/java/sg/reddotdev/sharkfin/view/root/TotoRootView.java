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

import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import sg.reddotdev.sharkfin.R;
import sg.reddotdev.sharkfin.data.adapter.TotoMainRecyclerAdapter;
import sg.reddotdev.sharkfin.data.model.LotteryResult;
import sg.reddotdev.sharkfin.util.constants.AppLocale;
import sg.reddotdev.sharkfin.view.viewholder.RecyclerViewHolder;

public class TotoRootView extends RootViewImpl implements RecyclerViewHolder.OnClickListener {
    private TotoMainRecyclerAdapter adapter;

    public TotoRootView(LayoutInflater inflater, ViewGroup container, AppCompatActivity activity) {
        super(inflater.inflate(R.layout.toto_activity, container), activity);

        setupToolbar();
        setupNextDrawDate();
        setupRecyclerViewAdapter();
    }

    @Override
    public void updateRecyclerViewAdapter() {
        adapter.notifyDataSetChanged();
    }

    protected void setupToolbar() {
        getToolbar().setTitle(R.string.totoActivity);
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
        nextDrawDateTV.setText(dateTimeArr[0]);
        nextDrawDayTimeTV.setText(dateTimeArr[1] + ", " + dateTimeArr[2]);
    }

    protected void setupRecyclerViewAdapter() {
        adapter = new TotoMainRecyclerAdapter(getLotteryResults());
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
