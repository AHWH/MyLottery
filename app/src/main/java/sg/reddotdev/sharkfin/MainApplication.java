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
import com.raizlabs.android.dbflow.config.FlowLog;
import com.raizlabs.android.dbflow.config.FlowManager;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidNetworking.initialize(this);
        Stetho.initializeWithDefaults(this);
        FlowManager.init(this);
        FlowLog.setMinimumLoggingLevel(FlowLog.Level.V);
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        AndroidNetworking.cancelAll();
        AndroidNetworking.shutDown();
    }
}
