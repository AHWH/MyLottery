/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.ui.mainresults;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import sg.reddotdev.sharkfin.ui.mainresults.fragment.bigsweep.BigSweepMainFragment;
import sg.reddotdev.sharkfin.ui.mainresults.fragment.bigsweep.BigSweepMainFragmentModule;
import sg.reddotdev.sharkfin.ui.mainresults.fragment.fourd.FourDMainFragment;
import sg.reddotdev.sharkfin.ui.mainresults.fragment.fourd.FourDMainFragmentModule;
import sg.reddotdev.sharkfin.ui.mainresults.fragment.toto.TotoMainFragment;
import sg.reddotdev.sharkfin.ui.mainresults.fragment.toto.TotoMainFragmentModule;
import sg.reddotdev.sharkfin.util.dagger.scope.PerActivity;
import sg.reddotdev.sharkfin.util.dagger.scope.PerFragment;

@Module
public abstract class MainActivityModule {

    @Binds
    @PerActivity
    abstract MainResultsContract.Presenter mainResultsPresenter(MainResultsPresenter presenter);

    @PerFragment
    @ContributesAndroidInjector(modules = BigSweepMainFragmentModule.class)
    abstract BigSweepMainFragment providesBigSweepMainFragment();

    @PerFragment
    @ContributesAndroidInjector(modules = FourDMainFragmentModule.class)
    abstract FourDMainFragment providesFourDMainFragment();

    @PerFragment
    @ContributesAndroidInjector(modules = TotoMainFragmentModule.class)
    abstract TotoMainFragment providesTotoMainFragment();
}
