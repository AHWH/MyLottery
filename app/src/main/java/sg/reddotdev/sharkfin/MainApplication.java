/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;
import com.facebook.stetho.Stetho;
import com.raizlabs.android.dbflow.config.FlowManager;

import sg.reddotdev.sharkfin.util.dagger.AppComponent;
import sg.reddotdev.sharkfin.util.dagger.DaggerAppComponent;
import sg.reddotdev.sharkfin.util.dagger.module.AppModule;

public class MainApplication extends Application {
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidNetworking.initialize(this);
        Stetho.initializeWithDefaults(this);
        FlowManager.init(this);

        appComponent = initDagger(this);
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        AndroidNetworking.cancelAll();
        AndroidNetworking.shutDown();
        FlowManager.destroy();
    }

    protected AppComponent initDagger(MainApplication application) {
        return DaggerAppComponent.builder().appModule(new AppModule(application)).build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
