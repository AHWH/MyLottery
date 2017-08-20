/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.util.dagger.module;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import sg.reddotdev.sharkfin.ui.mainresults.BaseMainActivity;
import sg.reddotdev.sharkfin.ui.mainresults.MainPresenterModule;
import sg.reddotdev.sharkfin.ui.mainresults.NetworkModule;
import sg.reddotdev.sharkfin.util.dagger.scope.PerActivity;

@Module
public abstract class ActivityBuilderModule {
    @PerActivity
    @ContributesAndroidInjector(modules = {MainPresenterModule.class, NetworkModule.class})
    abstract BaseMainActivity bindBaseMainActivity();

}
