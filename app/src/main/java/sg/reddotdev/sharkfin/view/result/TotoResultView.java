/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.view.result;

import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import sg.reddotdev.sharkfin.R;
import sg.reddotdev.sharkfin.data.adapter.ResultMultipleNumberRecyclerAdapter;

public class TotoResultView extends ResultViewImpl {
    private AppCompatActivity activity;

    private RecyclerView winningNosRecyclerView;

    public TotoResultView(LayoutInflater layoutInflater, ViewGroup container, AppCompatActivity activity) {
        super(layoutInflater.inflate(R.layout.result_toto_layout, container), activity) ;
        this.activity = activity;

        setupTheme();
        setupRecyclerViews();
    }

    protected void setupTheme() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int totoDarkColor = ContextCompat.getColor(activity.getApplicationContext(), R.color.totoTheme_PrimaryDark);
            Window window = activity.getWindow();
            window.setStatusBarColor(totoDarkColor);
            window.setNavigationBarColor(totoDarkColor);
        }
    }

    protected void setupRecyclerViews() {
        CardView winningNosCardView = (CardView) getRootView().findViewById(R.id.result_toto_winningNos);
        TextView winningNosTitleTextView = (TextView) winningNosCardView.findViewById(R.id.multipleNos_title);
        winningNosTitleTextView.setText(R.string.toto_result_winningNos);
        winningNosRecyclerView = (RecyclerView) winningNosCardView.findViewById(R.id.multipleNos_numberList);
        winningNosRecyclerView.setLayoutManager(new GridLayoutManager(activity.getApplicationContext(), 6, GridLayoutManager.VERTICAL, false));
    }


    public void setupToolbar(String date) {
        getToolbar().setTitle(date);
        initToolbar();
    }

    public void initDrawDetails(int drawNo) {
        TextView drawNoTextView = (TextView) getRootView().findViewById(R.id.result_layout_draw_no);
        drawNoTextView.setText(Integer.toString(drawNo));
    }

    public void initWinningNumbers(List<Integer> winningNumbers) {
        ResultMultipleNumberRecyclerAdapter winningNosAdapter = new ResultMultipleNumberRecyclerAdapter(winningNumbers);
        winningNosRecyclerView.setAdapter(winningNosAdapter);
    }

    public void initAdditionalWinningNumbers(int additionalWinningNumber) {
        CardView additionalWinningNumberCardView = (CardView) getRootView().findViewById(R.id.result_toto_additionalWinningNo);
        RelativeLayout additionalWinningNumberRowLayout = (RelativeLayout) additionalWinningNumberCardView.findViewById(R.id.result_standalone_single);
        TextView additionalWinningNumberTitleTextView = (TextView) additionalWinningNumberRowLayout.findViewById(R.id.standaloneNo_row_title);
        additionalWinningNumberTitleTextView.setText(R.string.toto_result_addWinningNos);

        TextView additionalWinningNumberTextView = (TextView) additionalWinningNumberRowLayout.findViewById(R.id.standaloneNo_row_no);
        DecimalFormat df = new DecimalFormat("00");
        additionalWinningNumberTextView.setText(df.format(additionalWinningNumber));
    }
}
