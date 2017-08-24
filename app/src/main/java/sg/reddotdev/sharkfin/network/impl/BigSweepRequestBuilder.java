/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.network.impl;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Priority;

import javax.inject.Inject;

import sg.reddotdev.sharkfin.network.base.ANRequestInt;
import sg.reddotdev.sharkfin.network.base.QueryParamBuilder;
import sg.reddotdev.sharkfin.util.constants.LottoConst;
import sg.reddotdev.sharkfin.util.constants.LottoURLEndpoint;
import sg.reddotdev.sharkfin.util.dagger.scope.PerActivity;
import sg.reddotdev.sharkfin.util.dagger.scope.PerFragment;

@PerActivity
public class BigSweepRequestBuilder implements ANRequestInt {
    private int drawNo;

    @Inject
    public BigSweepRequestBuilder() {
    }

    @Override
    public ANRequest createRequest() {
        return AndroidNetworking.get(LottoURLEndpoint.BIGSWEEP_URL)
                                .addQueryParameter(LottoConst.PARAM, buildQueryParam())
                                .setPriority(Priority.LOW)
                                .build();
    }

    private String buildQueryParam() {
        /*Check ReverseURL.xlsx for more info*/
        QueryParamBuilder queryParamBuilder = new BigSweepQueryParamBuilder(LottoConst.QUERY_INIITAL);
        queryParamBuilder.buildParam(drawNo);
        return queryParamBuilder.getQueryParam();
    }

    public void setDrawNo(int drawNo) {
        this.drawNo = drawNo;
    }
}
