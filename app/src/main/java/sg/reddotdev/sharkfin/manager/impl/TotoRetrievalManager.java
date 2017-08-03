/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.manager.impl;


import sg.reddotdev.sharkfin.data.network.impl.TotoRequestBuilder;
import sg.reddotdev.sharkfin.manager.base.ResultRetrievalManagerBase;


public class TotoRetrievalManager extends ResultRetrievalManagerBase {
    public TotoRetrievalManager() {
        super();
    }

    public void createRequest(int drawNo) {
        setRequest(new TotoRequestBuilder().createRequest(drawNo));
    }
}
