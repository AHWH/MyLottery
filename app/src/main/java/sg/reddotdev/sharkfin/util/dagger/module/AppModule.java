/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.util.dagger.module;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import sg.reddotdev.sharkfin.MainApplication;

@Module
public abstract class AppModule {
    @Binds
    @Singleton
    abstract Application providesContext(MainApplication application);

    @Provides
    @Singleton
    static Context providesApplicationContext(MainApplication application) {
        return application.getApplicationContext();
    }
}
