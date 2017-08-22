/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin;

import com.androidnetworking.AndroidNetworking;
import com.facebook.stetho.Stetho;
import com.raizlabs.android.dbflow.config.FlowManager;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import sg.reddotdev.sharkfin.util.dagger.AppComponent;

public class MainApplication extends DaggerApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidNetworking.initialize(this);
        Stetho.initializeWithDefaults(this);
        FlowManager.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        AndroidNetworking.cancelAll();
        AndroidNetworking.shutDown();
        FlowManager.destroy();
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        AppComponent appComponent = DaggerAppComponent.builder().application(this).build();
        appComponent.inject(this);
        return appComponent;
    }
}
