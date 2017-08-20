/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.ui.mainresults;

import android.content.Context;
import android.util.Log;

import dagger.Module;
import dagger.Provides;
import sg.reddotdev.sharkfin.ui.mainresults.fragment.MainFragmentContract;
import sg.reddotdev.sharkfin.ui.mainresults.fragment.bigsweep.BigSweepMainFragmentPresenter;
import sg.reddotdev.sharkfin.ui.mainresults.fragment.fourd.FourDMainFragmentPresenter;
import sg.reddotdev.sharkfin.ui.mainresults.fragment.toto.TotoMainFragmentPresenter;
import sg.reddotdev.sharkfin.util.dagger.scope.PerActivity;

@Module
public class MainPresenterModule {
    private Context context;

    public MainPresenterModule(Context context) {
        this.context = context;
    }


    @Provides
    @PerActivity
    public Context providesActivityContext() {
        return context;
    }

    @Provides
    @PerActivity
    public MainResultsContract.Presenter providesMainResultsPresenter() {
        return new MainResultsPresenter();
    }

    @Provides
    @PerActivity
    public MainFragmentContract.Presenter providesBigSweepMainFragmentPresenter() {
        return new BigSweepMainFragmentPresenter(context);
    }

    @Provides
    @PerActivity
    public MainFragmentContract.Presenter providesFourDMainFragmentPresenter() {
        Log.d("TEst", "HERE?");
        return new FourDMainFragmentPresenter(context);
    }

    @Provides
    @PerActivity
    public MainFragmentContract.Presenter providesTotoMainFragmentPresenter() {
        return new TotoMainFragmentPresenter(context);
    }
}
