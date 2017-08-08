/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.view.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import sg.reddotdev.sharkfin.R;


public class BaseMainView implements BaseMainViewMVP {
    private AppCompatActivity mActivity;
    private View rootView;

    private BaseMainViewMVPListener listener;

    private Toolbar toolbar;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;

    public BaseMainView(LayoutInflater inflater, AppCompatActivity mActivity) {
        rootView = inflater.inflate(R.layout.basemain_activity, null);
        this.mActivity = mActivity;

        initToolbar();
        initDrawer();
        initNavigationView();
        initBottomNavigationView();
    }


    @Override
    public View getRootView() {
        return rootView;
    }

    @Override
    public Bundle getViewState() {
        return null;
    }

    @Override
    public void registerListener(BaseMainViewMVPListener listener) {
        this.listener = listener;
    }

    @Override
    public void unregisterListener() {
        listener = null;
    }

    /*Internal Methods*/
    private void initToolbar() {
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        if(toolbar != null) {
            mActivity.setSupportActionBar(toolbar);
            mActivity.getSupportActionBar().setTitle(R.string.fourDMain);
        }
    }

    private void initDrawer() {
        mDrawer = (DrawerLayout) rootView.findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(mActivity, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void initNavigationView() {
        navigationView = (NavigationView) rootView.findViewById(R.id.drawerView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                listener.onNavigationSelected(item.getItemId());
                return true;
            }
        });
    }

    private void initBottomNavigationView() {
        bottomNavigationView = (BottomNavigationView) rootView.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                listener.onBottomNavigationSelected(item.getItemId());
                return true;
            }
        });
    }

}
