/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.network.impl;

import sg.reddotdev.sharkfin.network.base.QueryParamBuilder;

public class BigSweepQueryParamBuilder extends QueryParamBuilder {

    public BigSweepQueryParamBuilder(String queryParam) {
        super(queryParam);
    }

    public void buildParam(int drawNo) {
        addGrp1ParamModifier(drawNo/100000);
        addGrp2ParamModifier(drawNo%100000/10000);
        addGrp3ParamModifier(drawNo%10000/1000);
        addGrp4ParamModifier(drawNo%1000/100);
        addGrp2ParamModifier(drawNo%100/10);
        addGrp3ParamModifier(drawNo%10);
        setQueryParam(getQueryParam() + "%3d");
    }
}
