/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.view.root;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import sg.reddotdev.sharkfin.R;
import sg.reddotdev.sharkfin.data.model.LotteryResult;
import sg.reddotdev.sharkfin.util.constants.AppErrorCode;
import sg.reddotdev.sharkfin.view.viewholder.RecyclerViewHolder;

public abstract class RootViewImpl implements RootViewMVP, RecyclerViewHolder.OnClickListener {
    private View rootView;
    private AppCompatActivity mActivity;

    private Toolbar toolbar;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private RecyclerView resultsRecyclerView;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;

    private List<Object> lotteryResults;

    private RootViewMVPListener listener;

    public RootViewImpl(View rootView, AppCompatActivity mActivity) {
        this.rootView = rootView;
        this.mActivity = mActivity;
        lotteryResults = new ArrayList<>();

        initialiseToolbar();
        initialiseDrawer();
        initialiseNavigationView();
        initialiseBottomNavigationView();
        initialiseRecyclerView();
    }

    protected abstract void setupToolbar();
    protected abstract void setupNextDrawDate();
    protected abstract void setupRecyclerViewAdapter();
    //Unfortunately, I can't generalise the adapter listener here
    public abstract void onDestroyAdapterListener();


    @Override
    public View getRootView() {
        return rootView;
    }

    @Override
    public Bundle getViewState() {
        return null;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public RecyclerView getResultsRecyclerView() {
        return resultsRecyclerView;
    }

    public NavigationView getNavigationView() {
        return navigationView;
    }

    public BottomNavigationView getBottomNavigationView() {
        return bottomNavigationView;
    }

    public List<Object> getLotteryResults() {
        return lotteryResults;
    }

    public void createSnackBar(int errorCode) {
        Snackbar snackbar = Snackbar.make(rootView, "Default Message", Snackbar.LENGTH_SHORT);
        switch (errorCode) {
            case AppErrorCode.RESULT_ERROR:
                snackbar.setText("Failed to retrieve result");
                snackbar.setDuration(Snackbar.LENGTH_LONG);
                snackbar.setAction(R.string.snackbar_retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onRetryRetrievingResult();
                    }
                });
                snackbar.show();
                break;
        }
    }


    @Override
    public void registerListener(RootViewMVPListener listener) {
        this.listener = listener;
    }

    @Override
    public void unregisterListener() {
        listener = null;
    }

    public RootViewMVPListener getListener() {
        return listener;
    }

    /*Internal Methods*/
    private void initialiseToolbar() {
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        if(toolbar != null) {
            mActivity.setSupportActionBar(toolbar);
            mActivity.getSupportActionBar().setTitle(R.string.fourDActivity);
        }
    }

    private void initialiseDrawer() {
        mDrawer = (DrawerLayout) rootView.findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(mActivity, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void initialiseNavigationView() {
        navigationView = (NavigationView) rootView.findViewById(R.id.drawerView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                listener.onNavigationSelected(item.getItemId());
                return true;
            }
        });
    }

    private void initialiseBottomNavigationView() {
        bottomNavigationView = (BottomNavigationView) rootView.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                listener.onBottomNavigationSelected(item.getItemId());
                return true;
            }
        });
    }


    private void initialiseRecyclerView() {
        resultsRecyclerView = (RecyclerView) rootView.findViewById(R.id.results_list);
        resultsRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity.getApplicationContext()));
        resultsRecyclerView.addItemDecoration(new
                DividerItemDecoration(mActivity.getApplicationContext(), DividerItemDecoration.VERTICAL));
    }
}



