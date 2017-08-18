/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.network.impl;

import sg.reddotdev.sharkfin.network.base.QueryParamBuilder;

public class FourDTotoQueryParamBuilder extends QueryParamBuilder{

    public FourDTotoQueryParamBuilder(String queryParam) {
        super(queryParam);
    }


    public void buildParam(int drawNo) {
        addGrp1ParamModifier(drawNo/1000);
        addGrp2ParamModifier(drawNo%1000/100);
        addGrp3ParamModifier(drawNo%100/10);
        addGrp4ParamModifier(drawNo%10);
    }
}
