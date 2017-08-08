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

import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import sg.reddotdev.sharkfin.R;
import sg.reddotdev.sharkfin.data.adapter.TotoMainRecyclerAdapter;
import sg.reddotdev.sharkfin.data.model.LotteryResult;
import sg.reddotdev.sharkfin.util.constants.AppLocale;
import sg.reddotdev.sharkfin.view.viewholder.RecyclerViewHolder;

public class TotoFragmentView extends MainFragmentViewImpl implements RecyclerViewHolder.OnClickListener {
    private TotoMainRecyclerAdapter adapter;
    private final int totoPriColor;
    private final int totoDarkColor;
    private final int totoTextDarkShade;

    public TotoFragmentView(LayoutInflater inflater, AppCompatActivity activity) {
        super(inflater.inflate(R.layout.mainfragment_results_recycler_layout, null), activity);
        totoPriColor = ContextCompat.getColor(getActivity().getApplicationContext(), R.color.totoTheme_Primary);
        totoDarkColor = ContextCompat.getColor(getActivity().getApplicationContext(), R.color.totoTheme_PrimaryDark);
        totoTextDarkShade = ContextCompat.getColor(getActivity().getApplicationContext(), R.color.totoTheme_textDarkShade);
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
            window.setStatusBarColor(totoDarkColor);
            window.setNavigationBarColor(totoDarkColor);
        }
        getAppBarLayout().setBackgroundColor(totoPriColor);
    }

    @Override
    protected void setupToolbar() {
        getActivityToolbar().setTitle(R.string.totoMain);
        getActivityToolbar().setBackgroundColor(totoPriColor);
        getActivityNextDraw_textView().setTextColor(totoTextDarkShade);
        getActivityNextDrawDay_textView().setTextColor(totoTextDarkShade);
    }

    @Override
    protected void setupBottomBar() {
        getActivityBottomNavigationView().setBackgroundColor(totoPriColor);
    }

    @Override
    protected void setupNextDrawDate() {
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
        getActivityNextDrawDate_textView().setText(dateTimeArr[0]);
        getActivityNextDrawDay_textView().setText(dateTimeArr[1] + ", " + dateTimeArr[2]);
    }

    protected void setupRecyclerViewAdapter() {
        adapter = new TotoMainRecyclerAdapter(getLotteryResults());
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
