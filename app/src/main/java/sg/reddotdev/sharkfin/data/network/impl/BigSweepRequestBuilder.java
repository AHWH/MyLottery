/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.data.network.impl;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Priority;

import sg.reddotdev.sharkfin.data.network.ANRequestInt;
import sg.reddotdev.sharkfin.util.base.QueryParamBuilder;
import sg.reddotdev.sharkfin.util.impl.BigSweepQueryParamBuilder;
import sg.reddotdev.sharkfin.util.constants.LottoConst;
import sg.reddotdev.sharkfin.util.constants.LottoURLEndpoint;

public class BigSweepRequestBuilder implements ANRequestInt {
    @Override
    public ANRequest createRequest() {
        return AndroidNetworking.get(LottoURLEndpoint.BIGSWEEP_URL)
                                .addQueryParameter(LottoConst.PARAM, buildQueryParam())
                                .setPriority(Priority.LOW)
                                .build();
    }

    @Override
    public String buildQueryParam() {
        /*Check ReverseURL.xlsx for more info*/
        QueryParamBuilder queryParamBuilder = new BigSweepQueryParamBuilder(LottoConst.QUERY_INIITAL);
        queryParamBuilder.buildParam(22008);
        return queryParamBuilder.getQueryParam();
    }
}
