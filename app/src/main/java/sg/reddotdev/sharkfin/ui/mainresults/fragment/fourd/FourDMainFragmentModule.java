/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.ui.mainresults.fragment.fourd;

import dagger.Binds;
import dagger.Module;
import sg.reddotdev.sharkfin.data.transaction.ResultDatabaseManager;
import sg.reddotdev.sharkfin.data.transaction.impl.FourDResultDatabaseManager;
import sg.reddotdev.sharkfin.ui.mainresults.fragment.MainFragmentContract;
import sg.reddotdev.sharkfin.util.dagger.scope.PerFragment;

@Module
public abstract class FourDMainFragmentModule {
    @Binds
    @PerFragment
    abstract MainFragmentContract.Presenter providesFourDMainFragmentPresenter(FourDMainFragmentPresenter presenter);

    @Binds
    @PerFragment
    abstract ResultDatabaseManager providesFourDResultsDatabaseManager(FourDResultDatabaseManager databaseManager);
}
