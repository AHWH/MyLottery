/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin;

import android.app.Activity;
import android.app.Application;

import com.androidnetworking.AndroidNetworking;
import com.facebook.stetho.Stetho;
import com.raizlabs.android.dbflow.config.FlowManager;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import sg.reddotdev.sharkfin.util.dagger.DaggerAppComponent;

public class MainApplication extends Application implements HasActivityInjector {
    @Inject
    DispatchingAndroidInjector<Activity> dispatchingActivityInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidNetworking.initialize(this);
        Stetho.initializeWithDefaults(this);
        FlowManager.init(this);

        DaggerAppComponent.builder().application(this).build().inject(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        AndroidNetworking.cancelAll();
        AndroidNetworking.shutDown();
        FlowManager.destroy();
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingActivityInjector;
    }
}
