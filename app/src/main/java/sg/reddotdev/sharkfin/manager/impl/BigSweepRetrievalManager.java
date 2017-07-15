/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.manager.impl;


import sg.reddotdev.sharkfin.data.network.impl.BigSweepRequestBuilder;
import sg.reddotdev.sharkfin.manager.base.ResultRetrievalManagerBase;

public class BigSweepRetrievalManager extends ResultRetrievalManagerBase {
    public BigSweepRetrievalManager() {
        super();
    }

    public void createRequest() {
        setRequest(new BigSweepRequestBuilder().createRequest());
    }
}

