/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.ui.mainresults;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import sg.reddotdev.sharkfin.ui.mainresults.fragment.MainFragmentContract;
import sg.reddotdev.sharkfin.ui.mainresults.fragment.fourd.FourDMainFragmentPresenter;
import sg.reddotdev.sharkfin.util.dagger.scope.PerActivity;

@Module
public abstract class MainPresenterModule {

    @Provides
    @PerActivity
    static Context activityContext(Context context) {
        return context;
    }

    @Provides
    @PerActivity
    static MainResultsContract.Presenter mainResultsPresenter() {
        return new MainResultsPresenter();
    }

    /*@Provides
    @PerActivity
    public MainFragmentContract.Presenter providesBigSweepMainFragmentPresenter() {
        return new BigSweepMainFragmentPresenter(context);
    }*/

    @Provides
    @PerActivity
    static MainFragmentContract.Presenter providesFourDMainFragmentPresenter(Context context) {
        return new FourDMainFragmentPresenter(context);
    }

    /*@Provides
    @PerActivity
    public MainFragmentContract.Presenter providesTotoMainFragmentPresenter() {
        return new TotoMainFragmentPresenter(context);
    }*/
}
