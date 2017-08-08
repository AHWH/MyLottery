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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.TextStyle;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import sg.reddotdev.sharkfin.R;
import sg.reddotdev.sharkfin.data.adapter.ResultMultipleNumberRecyclerAdapter;

/**
 * Created by weihong on 4/8/17.
 */

public class FourDResultView extends ResultViewImpl {
    private AppCompatActivity activity;
    private DecimalFormat df;

    private RecyclerView starterNosRecyclerView;
    private RecyclerView consolationNosRecyclerView;

    public FourDResultView(LayoutInflater inflater, ViewGroup container, AppCompatActivity appCompatActivity) {
        super(inflater.inflate(R.layout.result_fourd_layout, container), appCompatActivity);
        activity = appCompatActivity;
        df = new DecimalFormat("0000");

        setupTheme();
        setupRecyclerViews();
    }

    protected void setupTheme() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int fourDDarkColor = ContextCompat.getColor(activity.getApplicationContext(), R.color.fourDTheme_PrimaryDark);
            Window window = activity.getWindow();
            window.setStatusBarColor(fourDDarkColor);
            window.setNavigationBarColor(fourDDarkColor);
        }
    }

    protected void setupRecyclerViews() {
        CardView starterNosCardView = (CardView) getRootView().findViewById(R.id.result_fourd_starterNos);
        TextView starterNosTitle = (TextView) starterNosCardView.findViewById(R.id.multipleNos_title);
        starterNosTitle.setText(R.string.fourd_result_starter);
        starterNosRecyclerView = (RecyclerView) starterNosCardView.findViewById(R.id.multipleNos_numberList);
        starterNosRecyclerView.setLayoutManager(new GridLayoutManager(getRootView().getContext(), 4, GridLayoutManager.VERTICAL, false));

        CardView consolationNosCardView = (CardView) getRootView().findViewById(R.id.result_fourd_consolationNos);
        TextView consolationNosTitle = (TextView) consolationNosCardView.findViewById(R.id.multipleNos_title);
        consolationNosTitle.setText(R.string.fourd_result_consolation);
        consolationNosRecyclerView = (RecyclerView) consolationNosCardView.findViewById(R.id.multipleNos_numberList);
        consolationNosRecyclerView.setLayoutManager(new GridLayoutManager(getRootView().getContext(), 4, GridLayoutManager.VERTICAL, false));
    }


    public void setupToolbar(String date) {
        getToolbar().setTitle(date);
        initToolbar();
    }


    public void initDrawDetails(int drawNo) {
        TextView drawNoTextView = (TextView) getRootView().findViewById(R.id.result_layout_draw_no);
        drawNoTextView.setText(Integer.toString(drawNo));
    }

    public void initStandaloneNumbers(int firstPrize, int secondPrize, int thirdPrize) {
        CardView card = (CardView) getRootView().findViewById(R.id.result_fourd_standaloneNos);

        RelativeLayout firstPrizeRow = (RelativeLayout) card.findViewById(R.id.standalonenos_firstPrize);
        TextView firstPrizeTitle = (TextView) firstPrizeRow.findViewById(R.id.standaloneNo_row_title);
        firstPrizeTitle.setText(R.string.fourd_result_firstPrize);
        TextView firstPrizeNo = (TextView) firstPrizeRow.findViewById(R.id.standaloneNo_row_no);
        firstPrizeNo.setText(df.format(firstPrize));

        RelativeLayout secondPrizeRow = (RelativeLayout) card.findViewById(R.id.standalonenos_secondPrize);
        TextView secondPrizeTitle = (TextView) secondPrizeRow.findViewById(R.id.standaloneNo_row_title);
        secondPrizeTitle.setText(R.string.fourd_result_secondPrize);
        TextView secondPrizeNo = (TextView) secondPrizeRow.findViewById(R.id.standaloneNo_row_no);
        secondPrizeNo.setText(df.format(secondPrize));

        RelativeLayout thirdPrizeRow = (RelativeLayout) card.findViewById(R.id.standalonenos_thirdPrize);
        TextView thirdPrizeTitle = (TextView) thirdPrizeRow.findViewById(R.id.standaloneNo_row_title);
        thirdPrizeTitle.setText(R.string.fourd_result_thirdPrize);
        TextView thirdPrizeNo = (TextView) thirdPrizeRow.findViewById(R.id.standaloneNo_row_no);
        thirdPrizeNo.setText(df.format(thirdPrize));
    }

    public void initStarterNumbers(List<Integer> starterNumbers) {
        ResultMultipleNumberRecyclerAdapter starterNosAdapter = new ResultMultipleNumberRecyclerAdapter(starterNumbers);
        starterNosRecyclerView.setAdapter(starterNosAdapter);
    }

    public void initConsolationNumbers(List<Integer> consolationNumbers) {
        ResultMultipleNumberRecyclerAdapter consolationNosAdapter = new ResultMultipleNumberRecyclerAdapter(consolationNumbers);
        consolationNosRecyclerView.setAdapter(consolationNosAdapter);
    }
}
