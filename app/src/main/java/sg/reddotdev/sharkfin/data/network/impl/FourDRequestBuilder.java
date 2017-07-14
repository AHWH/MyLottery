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
import sg.reddotdev.sharkfin.util.LottoConst;
import sg.reddotdev.sharkfin.util.LottoURLEndpoint;
import sg.reddotdev.sharkfin.util.base.QueryParamBuilder;
import sg.reddotdev.sharkfin.util.impl.FourDTotoQueryParamBuilder;

public class FourDRequestBuilder implements ANRequestInt {
    @Override
    public ANRequest createRequest() {
        return AndroidNetworking.get(LottoURLEndpoint.FOURD_URL)
                                .addQueryParameter(LottoConst.PARAM, buildQueryParam())
                                .setPriority(Priority.LOW)
                                .build();
    }

    public String buildQueryParam() {
        /*Capped at Draw 1000*/
        QueryParamBuilder queryParamBuilder = new FourDTotoQueryParamBuilder(LottoConst.QUERY_INIITAL);
        queryParamBuilder.buildParam(4129);
        return queryParamBuilder.getQueryParam();
    }
}
