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
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import sg.reddotdev.sharkfin.R;
import sg.reddotdev.sharkfin.data.adapter.ResultMultipleNumberRecyclerAdapter;

public class BigSweepResultView extends ResultViewImpl {
    private String LOGTAG = getClass().getSimpleName();

    private DecimalFormat df;

    private AppCompatActivity activity;

    private RecyclerView jackpotNosRecyclerView;
    private RecyclerView luckyNosRecyclerView;
    private RecyclerView giftNosRecyclerView;
    private RecyclerView consolationNosRecyclerView;
    private RecyclerView participationNosRecyclerView;
    private RecyclerView delight2DNosRecyclerView;
    private RecyclerView delight3DNosRecyclerView;

    public BigSweepResultView(LayoutInflater inflater, ViewGroup container, AppCompatActivity appCompatActivity) {
        super(inflater.inflate(R.layout.result_bigsweep_layout, container), appCompatActivity);

        df = new DecimalFormat("000000");

        activity = appCompatActivity;

        setupTheme();
        setupRecyclerViews();
    }

    protected void setupTheme() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int bigSweepDarkColor = ContextCompat.getColor(activity.getApplicationContext(), R.color.bigSweepTheme_PrimaryDark);
            Window window = activity.getWindow();
            window.setStatusBarColor(bigSweepDarkColor);
            window.setNavigationBarColor(bigSweepDarkColor);
        }
    }

    protected void setupRecyclerViews() {
        int bigSweepPrimaryColour = ContextCompat.getColor(activity.getApplicationContext(), R.color.bigSweepTheme_Primary);

        CardView jackpotNosCardView = (CardView) getRootView().findViewById(R.id.result_bigsweep_jackpot);
        TextView jackpotNosTitle = (TextView) jackpotNosCardView.findViewById(R.id.multipleNos_title);
        jackpotNosTitle.setText(R.string.bigSweep_result_jackpotPrize);
        jackpotNosTitle.setBackgroundColor(bigSweepPrimaryColour);
        jackpotNosRecyclerView = (RecyclerView) jackpotNosCardView.findViewById(R.id.multipleNos_numberList);
        jackpotNosRecyclerView.setLayoutManager(new GridLayoutManager(getRootView().getContext(), 3, GridLayoutManager.VERTICAL, false));

        CardView luckyNosCardView = (CardView) getRootView().findViewById(R.id.result_bigsweep_lucky);
        TextView luckyNosTitle = (TextView) luckyNosCardView.findViewById(R.id.multipleNos_title);
        luckyNosTitle.setBackgroundColor(bigSweepPrimaryColour);
        luckyNosTitle.setText(R.string.bigSweep_result_luckyPrize);
        luckyNosRecyclerView = (RecyclerView) luckyNosCardView.findViewById(R.id.multipleNos_numberList);
        luckyNosRecyclerView.setLayoutManager(new GridLayoutManager(getRootView().getContext(), 3, GridLayoutManager.VERTICAL, false));

        CardView giftNosCardView = (CardView) getRootView().findViewById(R.id.result_bigsweep_gift);
        TextView giftNosTitle = (TextView) giftNosCardView.findViewById(R.id.multipleNos_title);
        giftNosTitle.setBackgroundColor(bigSweepPrimaryColour);
        giftNosTitle.setText(R.string.bigSweep_result_giftPrize);
        giftNosRecyclerView = (RecyclerView) giftNosCardView.findViewById(R.id.multipleNos_numberList);
        giftNosRecyclerView.setLayoutManager(new GridLayoutManager(getRootView().getContext(), 3, GridLayoutManager.VERTICAL, false));

        CardView consolationNosCardView = (CardView) getRootView().findViewById(R.id.result_bigsweep_consolation);
        TextView consolationNosTitle = (TextView) consolationNosCardView.findViewById(R.id.multipleNos_title);
        consolationNosTitle.setBackgroundColor(bigSweepPrimaryColour);
        consolationNosTitle.setText(R.string.bigSweep_result_consolationPrize);
        consolationNosRecyclerView = (RecyclerView) consolationNosCardView.findViewById(R.id.multipleNos_numberList);
        consolationNosRecyclerView.setLayoutManager(new GridLayoutManager(getRootView().getContext(), 3, GridLayoutManager.VERTICAL, false));

        CardView participationNosCardView = (CardView) getRootView().findViewById(R.id.result_bigsweep_participation);
        TextView participationNosTitle = (TextView) participationNosCardView.findViewById(R.id.multipleNos_title);
        participationNosTitle.setBackgroundColor(bigSweepPrimaryColour);
        participationNosTitle.setText(R.string.bigSweep_result_participationPrize);
        participationNosRecyclerView = (RecyclerView) participationNosCardView.findViewById(R.id.multipleNos_numberList);
        participationNosRecyclerView.setLayoutManager(new GridLayoutManager(getRootView().getContext(), 3, GridLayoutManager.VERTICAL, false));

        CardView delight2DNosCardView = (CardView) getRootView().findViewById(R.id.result_bigsweep_delight_2d);
        TextView delight2DNosTitle = (TextView) delight2DNosCardView.findViewById(R.id.multipleNos_title);
        delight2DNosTitle.setBackgroundColor(bigSweepPrimaryColour);
        delight2DNosTitle.setText(R.string.bigSweep_result_delight2DPrize);
        delight2DNosRecyclerView = (RecyclerView) delight2DNosCardView.findViewById(R.id.multipleNos_numberList);
        delight2DNosRecyclerView.setLayoutManager(new GridLayoutManager(getRootView().getContext(), 5, GridLayoutManager.VERTICAL, false));

        CardView delight3DNosCardView = (CardView) getRootView().findViewById(R.id.result_bigsweep_delight_3d);
        TextView delight3DNosTitle = (TextView) delight3DNosCardView.findViewById(R.id.multipleNos_title);
        delight3DNosTitle.setBackgroundColor(bigSweepPrimaryColour);
        delight3DNosTitle.setText(R.string.bigSweep_result_delight3DPrize);
        delight3DNosRecyclerView = (RecyclerView) delight3DNosCardView.findViewById(R.id.multipleNos_numberList);
        delight3DNosRecyclerView.setLayoutManager(new GridLayoutManager(getRootView().getContext(), 5, GridLayoutManager.VERTICAL, false));
    }

    public void setupToolbar(String date) {
        getToolbar().setTitle(date);
        initToolbar();
    }

    public void initDrawDetails(int drawNo) {
        TextView drawNoTextView = (TextView) getRootView().findViewById(R.id.result_layout_draw_no);
        int month = drawNo / 10000;
        int year = drawNo % 10000;

        String formattedLotteryId = month + "/" + year;

        drawNoTextView.setText(month < 10 ? "0" + formattedLotteryId : formattedLotteryId);
    }

    public void initStandaloneNumbers(int firstPrize, int secondPrize, int thirdPrize) {
        CardView card = (CardView) getRootView().findViewById(R.id.result_bigsweep_standaloneNos);

        RelativeLayout firstPrizeRow = (RelativeLayout) card.findViewById(R.id.standalonenos_firstPrize);
        TextView firstPrizeTitle = (TextView) firstPrizeRow.findViewById(R.id.standaloneNo_row_title);
        firstPrizeTitle.setText(R.string.bigSweep_result_firstPrize);
        TextView firstPrizeNo = (TextView) firstPrizeRow.findViewById(R.id.standaloneNo_row_no);
        firstPrizeNo.setText(df.format(firstPrize));

        RelativeLayout secondPrizeRow = (RelativeLayout) card.findViewById(R.id.standalonenos_secondPrize);
        TextView secondPrizeTitle = (TextView) secondPrizeRow.findViewById(R.id.standaloneNo_row_title);
        secondPrizeTitle.setText(R.string.bigSweep_result_secondPrize);
        TextView secondPrizeNo = (TextView) secondPrizeRow.findViewById(R.id.standaloneNo_row_no);
        secondPrizeNo.setText(df.format(secondPrize));

        RelativeLayout thirdPrizeRow = (RelativeLayout) card.findViewById(R.id.standalonenos_thirdPrize);
        TextView thirdPrizeTitle = (TextView) thirdPrizeRow.findViewById(R.id.standaloneNo_row_title);
        thirdPrizeTitle.setText(R.string.bigSweep_result_thirdPrize);
        TextView thirdPrizeNo = (TextView) thirdPrizeRow.findViewById(R.id.standaloneNo_row_no);
        thirdPrizeNo.setText(df.format(thirdPrize));
    }

    public void initSuperSweep(String superSweepPrize) {
        CardView superSweepCard = (CardView) getRootView().findViewById(R.id.result_bigsweep_supersweep);

        if(superSweepPrize.isEmpty()) {
            superSweepCard.setVisibility(View.GONE);
        } else {
            RelativeLayout superSweepLayout = (RelativeLayout) superSweepCard.findViewById(R.id.result_standalone_single);
            TextView superSweepTitleTextView = (TextView) superSweepLayout.findViewById(R.id.standaloneNo_row_title);
            superSweepTitleTextView.setText(R.string.bigSweep_result_superSweepPrize);

            TextView superSweepPrizeTextView = (TextView) superSweepLayout.findViewById(R.id.standaloneNo_row_no);
            superSweepPrizeTextView.setText(superSweepPrize);
        }
    }

    public void initCascade(int cascadePrize) {
        CardView cascadeCard = (CardView) getRootView().findViewById(R.id.result_bigsweep_cascade);

        if(cascadePrize == 0) {
            cascadeCard.setVisibility(View.GONE);
        } else {
            RelativeLayout cascadeLayout = (RelativeLayout) cascadeCard.findViewById(R.id.result_standalone_single);
            TextView cascadeTitleTextView = (TextView) cascadeLayout.findViewById(R.id.standaloneNo_row_title);
            cascadeTitleTextView.setText(R.string.bigSweep_result_cascadePrize);

            TextView cascadePrizeTextView = (TextView) cascadeLayout.findViewById(R.id.standaloneNo_row_no);
            cascadePrizeTextView.setText(Integer.toString(cascadePrize));
        }
    }

    public void initJackpotNumbers(List<Integer> jackpotNumbers) {
        ResultMultipleNumberRecyclerAdapter jackpotNosAdapter = new ResultMultipleNumberRecyclerAdapter(jackpotNumbers);
        jackpotNosRecyclerView.setAdapter(jackpotNosAdapter);
    }

    public void initLuckyNumbers(List<Integer> luckyNumbers) {
        ResultMultipleNumberRecyclerAdapter luckyNosAdapter = new ResultMultipleNumberRecyclerAdapter(luckyNumbers);
        luckyNosRecyclerView.setAdapter(luckyNosAdapter);
    }

    public void initGiftNumbers(List<Integer> giftNumbers) {
        ResultMultipleNumberRecyclerAdapter giftNosAdapter = new ResultMultipleNumberRecyclerAdapter(giftNumbers);
        giftNosRecyclerView.setAdapter(giftNosAdapter);
    }

    public void initConsolationNumbers(List<Integer> consolationNumbers) {
        ResultMultipleNumberRecyclerAdapter consolationNosAdapter = new ResultMultipleNumberRecyclerAdapter(consolationNumbers);
        consolationNosRecyclerView.setAdapter(consolationNosAdapter);
    }

    public void initParticipationNumbers(List<Integer> participationNumbers) {
        if(participationNumbers.isEmpty()) {
            getRootView().findViewById(R.id.result_bigsweep_participation).setVisibility(View.GONE);
        } else {
            ResultMultipleNumberRecyclerAdapter participationNosAdapter = new ResultMultipleNumberRecyclerAdapter(participationNumbers);
            participationNosRecyclerView.setAdapter(participationNosAdapter);
        }
    }

    public void initDelight2DNumbers(List<Integer> delight2DNumbers) {
        if(delight2DNumbers.isEmpty()) {
            getRootView().findViewById(R.id.result_bigsweep_delight_2d).setVisibility(View.GONE);
        } else {
            ResultMultipleNumberRecyclerAdapter delight2DNosAdapter = new ResultMultipleNumberRecyclerAdapter(delight2DNumbers);
            delight2DNosRecyclerView.setAdapter(delight2DNosAdapter);
        }
    }

    public void initDelight3DNumbers(List<Integer> delight3DNumbers) {
        if(delight3DNumbers.isEmpty()) {
            getRootView().findViewById(R.id.result_bigsweep_delight_3d).setVisibility(View.GONE);
        } else {
            ResultMultipleNumberRecyclerAdapter delight3DNosAdapter = new ResultMultipleNumberRecyclerAdapter(delight3DNumbers);
            delight3DNosRecyclerView.setAdapter(delight3DNosAdapter);
        }
    }
}
