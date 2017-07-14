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


public class TotoRequestBuilder implements ANRequestInt {
    @Override
    public ANRequest createRequest() {
        return AndroidNetworking.get(LottoURLEndpoint.TOTO_URL)
                                .addQueryParameter(LottoConst.PARAM, buildQueryParam())
                                .setPriority(Priority.LOW)
                                .build();
    }

    @Override
    public String buildQueryParam() {
        /*Capped at Draw 1001*/
        /*Draw 1282 (RHJhd051bWJlcj0xMjgy, Mon, 11 May 1998) is the first draw to show prize amount fully*/
        /*Draw 1281 returns error*/
        /*Draw 1194 (RHJhd051bWJlcj0xMTk0, Thu, 03 Jul 1997) is the first draw to show correct dates. Earlier default to 1 Jan 0001*/

        QueryParamBuilder queryParamBuilder = new FourDTotoQueryParamBuilder(LottoConst.QUERY_INIITAL);
        queryParamBuilder.buildParam(3281);
        return queryParamBuilder.getQueryParam();
    }
}
