/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.ui.mainresults;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasFragmentInjector;
import dagger.android.support.HasSupportFragmentInjector;
import sg.reddotdev.sharkfin.MainApplication;
import sg.reddotdev.sharkfin.R;
import sg.reddotdev.sharkfin.ui.mainresults.fragment.bigsweep.BigSweepMainFragment;
import sg.reddotdev.sharkfin.ui.mainresults.fragment.fourd.FourDMainFragment;
import sg.reddotdev.sharkfin.ui.mainresults.fragment.toto.TotoMainFragment;
import sg.reddotdev.sharkfin.util.constants.LottoConst;
import sg.reddotdev.sharkfin.util.ui.BaseActivityView;


public class BaseMainActivity extends BaseActivityView
        implements MainResultsContract.View, HasSupportFragmentInjector {

    private String LOGTAG = getClass().getSimpleName();
    private String fragmentInViewTag;

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentInjector;


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.appbar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.drawerView)
    NavigationView navigationView;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;

    @BindView(R.id.nextDraw_Date_textView)
    TextView nextDrawDateTextView;
    @BindView(R.id.nextDraw_DayTime_textView)
    TextView nextDrawDayTextView;

    @Inject
    MainResultsContract.Presenter presenter;

    private Fragment fourDMainFragment;
    private Fragment totoMainFragment;
    private Fragment bigSweepMainFragment;


    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basemain_activity);
        ButterKnife.bind(this);

        AndroidInjection.inject(this);

        if(toolbar != null) {
            setSupportActionBar(toolbar);
        }

        fragmentManager = getSupportFragmentManager();
        initDrawer();
        setListeners();

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
        presenter.setView(this);
    }

    @Override
    protected void onPause() {
        presenter.resetView();
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
        super.onDestroy();
    }

    @Override
    public void updateTheme(int lotteryType) {
        int bgColour;
        int textShade;
        switch (lotteryType) {
            case LottoConst.SGPOOLS_4D:
                bgColour = ContextCompat.getColor(this, R.color.fourDTheme_Primary);
                textShade = ContextCompat.getColor(this, R.color.fourDTheme_textDarkShade);

                toolbar.setTitle(R.string.fourDMain);
                break;
            case LottoConst.SGPOOLS_TOTO:
                bgColour = ContextCompat.getColor(this, R.color.totoTheme_Primary);
                textShade = ContextCompat.getColor(this, R.color.totoTheme_textDarkShade);

                toolbar.setTitle(R.string.totoMain);
                break;
            case LottoConst.SGPOOLS_SWEEP:
                bgColour = ContextCompat.getColor(this, R.color.bigSweepTheme_Primary);
                textShade = ContextCompat.getColor(this, R.color.bigSweepTheme_textDarkShade);

                toolbar.setTitle(R.string.bigSweepMain);
                break;
            default:
                bgColour = Color.BLACK;
                textShade = Color.WHITE;
        }
        changeAppToolbarLayoutTheme(bgColour);
        changeToolbarTheme(bgColour, textShade);
        changeBottomNavViewTheme(bgColour);
    }

    @Override
    public void showNextDraw(String date, String dayTime) {
        nextDrawDateTextView.setText(date);
        nextDrawDayTextView.setText(dayTime);
    }

    private void initDrawer() {
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }


    private void setListeners() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.drawer_settings:
                        showToastMessage("Redirects you to SettingsActivity");
                        return true;
                    default:
                        return false;
                }
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                switch (item.getItemId()) {
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
                    return true;
                }
                return false;
            }
        });
    }

    public final AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentInjector;
    }


    private void changeAppToolbarLayoutTheme(int bgColour) {
        appBarLayout.setBackgroundColor(bgColour);
    }

    private void changeToolbarTheme(int bgColour, int textShade) {
        toolbar.setBackgroundColor(bgColour);

        nextDrawDateTextView.setTextColor(textShade);
        nextDrawDayTextView.setTextColor(textShade);
    }

    private void changeBottomNavViewTheme(int bgColour) {
        bottomNavigationView.setBackgroundColor(bgColour);
    }

}
