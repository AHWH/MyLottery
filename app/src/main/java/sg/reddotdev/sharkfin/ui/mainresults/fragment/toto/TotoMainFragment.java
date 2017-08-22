/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.ui.mainresults.fragment.toto;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import org.greenrobot.eventbus.EventBus;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.TextStyle;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindColor;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.AndroidSupportInjection;
import sg.reddotdev.sharkfin.R;
import sg.reddotdev.sharkfin.activity.TotoResultActivity;
import sg.reddotdev.sharkfin.data.adapter.TotoMainRecyclerAdapter;
import sg.reddotdev.sharkfin.data.model.LotteryResult;
import sg.reddotdev.sharkfin.ui.mainresults.MainResultsPresenter;
import sg.reddotdev.sharkfin.ui.mainresults.fragment.MainFragmentContract;
import sg.reddotdev.sharkfin.ui.mainresults.fragment.bigsweep.BigSweepMainFragmentPresenter;
import sg.reddotdev.sharkfin.util.constants.LottoConst;
import sg.reddotdev.sharkfin.util.ui.BaseFragmentView;
import sg.reddotdev.sharkfin.view.viewholder.RecyclerViewHolder;


public class TotoMainFragment extends BaseFragmentView
    implements MainFragmentContract.View, RecyclerViewHolder.OnClickListener {

    private String LOGTAG = getClass().getSimpleName();

    private View view;

    private Unbinder unbinder;

    private RecyclerView resultsListRecyclerView;
    private TotoMainRecyclerAdapter adapter;

    @BindColor(R.color.totoTheme_PrimaryDark)
    int totoDarkColour;

    @Inject
    BigSweepMainFragmentPresenter presenter;

    private List<Object> lotteryResults;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        AndroidSupportInjection.inject(this);
        lotteryResults = new ArrayList<>();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mainfragment_results_recycler_layout, container, false);
        unbinder = ButterKnife.bind(this, view);

        setupRecyclerView();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupTheme();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onViewCreated(this);
        adapter.registerListener(this);
    }

    @Override
    public void onPause() {
        presenter.onViewDetach();
        adapter.unregisterListener();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }



    /*Below are listeners*/
    /*For Result retrieval*/
    @Override
    public void onSuccessRetrieveResult() {
        /*Presenter to merged the list of View*/
        /*Java pass-by-value! */
        presenter.mergedList(lotteryResults);
    }

    @Override
    public void onFailureRetrieveResult() {

    }
    /*For Result's database management*/
    @Override
    public void onFailureSaveDB() {

    }

    @Override
    public void onSuccessRetrieveDB() {
        presenter.mergedList(lotteryResults);
    }

    @Override
    public void onFailureRetrieveDB() {

    }

    @Override
    public void onMergedList() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResultClick(LotteryResult lotteryResult) {
        Intent resultActivityIntent = new Intent(getActivity(), TotoResultActivity.class);
        ZonedDateTime date = lotteryResult.getDate();
        String dateStr = date.getDayOfMonth() + " "
                + date.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()) + " "
                + date.getYear() + " ("
                + date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault()) + ")";
        resultActivityIntent.putExtra("Date", dateStr);
        startActivity(resultActivityIntent);
        EventBus.getDefault().postSticky(lotteryResult);
    }

    /*Private Methods*/
    private void setupRecyclerView() {
        resultsListRecyclerView = (RecyclerView) view.findViewById(R.id.results_list);
        resultsListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        resultsListRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        adapter = new TotoMainRecyclerAdapter(lotteryResults);
        resultsListRecyclerView.setAdapter(adapter);
    }

    private void setupTheme() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.setStatusBarColor(totoDarkColour);
            window.setNavigationBarColor(totoDarkColour);
        }
    }
}
