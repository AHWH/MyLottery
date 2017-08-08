/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.view.main.fragment;

import android.support.v7.widget.RecyclerView;

import sg.reddotdev.sharkfin.data.model.LotteryResult;
import sg.reddotdev.sharkfin.view.ViewMVP;

public interface MainFragmentViewMVP extends ViewMVP {
    interface MainFragmentViewMVPListener {
        void onItemClick(LotteryResult lotteryResult);
    }

    RecyclerView getResultsRecyclerView();

    void registerListener(MainFragmentViewMVPListener listener);
    void unregisterListener();
}
