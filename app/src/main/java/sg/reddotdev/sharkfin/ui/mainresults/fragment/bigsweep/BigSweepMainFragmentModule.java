/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.ui.mainresults.fragment.bigsweep;

import dagger.Binds;
import dagger.Module;
import sg.reddotdev.sharkfin.data.transaction.ResultDatabaseManager;
import sg.reddotdev.sharkfin.data.transaction.impl.BigSweepResultDatabaseManager;
import sg.reddotdev.sharkfin.ui.mainresults.fragment.MainFragmentContract;
import sg.reddotdev.sharkfin.util.dagger.scope.PerFragment;

@Module
public abstract class BigSweepMainFragmentModule {
    @Binds
    @PerFragment
    abstract MainFragmentContract.Presenter providesBigSweepMainFragmentPresenter(BigSweepMainFragmentPresenter presenter);

    @Binds
    @PerFragment
    abstract ResultDatabaseManager providesBigSweepResultsDatabaseManager(BigSweepResultDatabaseManager databaseManager);
}
