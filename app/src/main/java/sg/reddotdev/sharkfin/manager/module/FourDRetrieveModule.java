/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.manager.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import sg.reddotdev.sharkfin.manager.impl.FourDRetrievalManager;

@Module
public class FourDRetrieveModule {
    @Provides
    @Singleton
    public FourDRetrievalManager createInstance() {
        return new FourDRetrievalManager();
    }
}
