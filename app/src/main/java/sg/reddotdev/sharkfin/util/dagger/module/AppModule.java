/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.util.dagger.module;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.android.AndroidInjectionModule;
import sg.reddotdev.sharkfin.MainApplication;

@Module (includes = AndroidInjectionModule.class)
public class AppModule {

    @Provides
    @Singleton
    public Context providesContext(MainApplication application) {
        return application;
    }
}
