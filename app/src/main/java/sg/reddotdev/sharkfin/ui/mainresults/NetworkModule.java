/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.ui.mainresults;

import dagger.Module;
import dagger.Provides;
import sg.reddotdev.sharkfin.network.impl.BigSweepRequestBuilder;
import sg.reddotdev.sharkfin.network.impl.FourDRequestBuilder;
import sg.reddotdev.sharkfin.network.impl.TotoRequestBuilder;
import sg.reddotdev.sharkfin.network.impl.UnifiedResultRetrievalManager;
import sg.reddotdev.sharkfin.util.dagger.scope.PerActivity;

@Module
public abstract class NetworkModule {
    @Provides
    @PerActivity
    public UnifiedResultRetrievalManager unifiedResultRetrievalManager() {
        return new UnifiedResultRetrievalManager();
    }

    @Provides
    @PerActivity
    public FourDRequestBuilder fourDRequestBuilder() {
        return new FourDRequestBuilder();
    }

    @Provides
    @PerActivity
    public TotoRequestBuilder totoRequestBuilder() {
        return new TotoRequestBuilder();
    }

    @Provides
    @PerActivity
    public BigSweepRequestBuilder bigSweepRequestBuilder() {
        return new BigSweepRequestBuilder();
    }
}
