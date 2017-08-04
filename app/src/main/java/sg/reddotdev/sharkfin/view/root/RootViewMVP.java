/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.view.root;

import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.List;

import sg.reddotdev.sharkfin.data.model.LotteryResult;
import sg.reddotdev.sharkfin.view.ViewMVP;


public interface RootViewMVP extends ViewMVP {
    interface RootViewMVPListener {
        void onBottomNavigationSelected(int itemID);
        void onNavigationSelected(int itemID);
        void onRetryRetrievingResult();
        void onItemClick(LotteryResult lotteryResult);
    }

    RecyclerView getResultsRecyclerView();
    Toolbar getToolbar();
    List<Object> getLotteryResults();
    BottomNavigationView getBottomNavigationView();

    void updateRecyclerViewAdapter();

    void createSnackBar(int errorCode);

    void registerListener(RootViewMVPListener listener);
    void unregisterListener();
}
