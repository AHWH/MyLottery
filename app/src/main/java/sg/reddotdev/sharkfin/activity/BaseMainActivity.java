/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import sg.reddotdev.sharkfin.R;
import sg.reddotdev.sharkfin.fragment.BigSweepMainFragment;
import sg.reddotdev.sharkfin.fragment.FourDMainFragment;
import sg.reddotdev.sharkfin.fragment.TotoMainFragment;
import sg.reddotdev.sharkfin.view.main.BaseMainView;
import sg.reddotdev.sharkfin.view.main.BaseMainViewMVP;


public class BaseMainActivity extends AppCompatActivity implements BaseMainViewMVP.BaseMainViewMVPListener {
    private String LOGTAG = getClass().getSimpleName();
    private String fragmentInViewTag;

    private SharedPreferences globalSharedPreferences;

    private BaseMainView baseMainView;

    private Fragment fourDMainFragment;
    private Fragment totoMainFragment;
    private Fragment bigSweepMainFragment;

    /*Related to app's start*/
    private boolean firstStart;
    private boolean coldStart;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseMainView = new BaseMainView(LayoutInflater.from(this), this);
        baseMainView.registerListener(this);

        setContentView(baseMainView.getRootView());

        fragmentManager = getSupportFragmentManager();

        if(savedInstanceState == null) {
            fourDMainFragment = new FourDMainFragment();
            fragmentManager.beginTransaction().replace(R.id.results_list_placeholder, fourDMainFragment, "4DFragment").commit();
            fragmentInViewTag = "4DFragment";
        } else {
            fragmentInViewTag = savedInstanceState.getString("LastFragmentViewed");
            Fragment lastKnownFragment = fragmentManager.findFragmentByTag(fragmentInViewTag);
            fragmentManager.beginTransaction().replace(R.id.results_list_placeholder, lastKnownFragment, fragmentInViewTag).commit();
        }

    }



    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("LastFragmentViewed", fragmentInViewTag);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        baseMainView.unregisterListener();
        super.onDestroy();
    }



    /*Getters and Setter for app's start*/
    private void initFirstStart() {
        globalSharedPreferences.getBoolean("FirstStart", true);
    }

    private void initColdStart() {
        globalSharedPreferences.getBoolean("ColdStart", true);
    }

    public boolean isFirstStart() {
        return firstStart;
    }

    public boolean isColdStart() {
        return coldStart;
    }

    /*Drawer's listener*/
    @Override
    public void onNavigationSelected(int itemID) {
        switch (itemID) {
            case R.id.drawer_settings:
                Toast.makeText(this, "Redirects you to SettingsActivity", Toast.LENGTH_SHORT).show();
        }
    }

    /*Bottom Bar Listener*/
    @Override
    public void onBottomNavigationSelected(int itemID) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (itemID) {
            case R.id.bottomNav_4D:
                if(fourDMainFragment == null) {
                    fourDMainFragment = new FourDMainFragment();
                }

                if(!fourDMainFragment.isAdded()) {
                    fragmentTransaction.replace(R.id.results_list_placeholder, fourDMainFragment, "4DFragment");
                    fragmentInViewTag = "4DFragment";
                }
                break;
            case R.id.bottomNav_Toto:
                if(totoMainFragment == null) {
                    totoMainFragment = new TotoMainFragment();
                }

                if(!totoMainFragment.isAdded()) {
                    fragmentTransaction.replace(R.id.results_list_placeholder, totoMainFragment, "TotoFragment");
                    fragmentInViewTag = "TotoFragment";
                }
                break;
            case R.id.bottomNav_BigSweep:
                if(bigSweepMainFragment == null) {
                    bigSweepMainFragment = new BigSweepMainFragment();
                }

                if(!bigSweepMainFragment.isAdded()) {
                    fragmentTransaction.replace(R.id.results_list_placeholder, bigSweepMainFragment, "BigSweepFragment");
                    fragmentInViewTag = "BigSweepFragment";
                }
        }

        if(!fragmentTransaction.isEmpty()) {
            fragmentTransaction.commit();
        }
    }
}
