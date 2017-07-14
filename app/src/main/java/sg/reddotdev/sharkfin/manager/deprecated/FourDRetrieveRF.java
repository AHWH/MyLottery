/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.manager.deprecated;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import sg.reddotdev.sharkfin.data.parser.impl.FourDHTMLParser;
import sg.reddotdev.sharkfin.util.impl.FourDTotoQueryParamBuilder;

public class FourDRetrieveRF {
    public void retrieve() {
        Retrofit retrofit = retroFitBuilder();
        SPools4DInt sPools4DInt = retrofit.create(SPools4DInt.class);
        Call<String> call = sPools4DInt.getResult(buildQueryParam());

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("Process", "Successfully retrieved " + call.request().url().toString());
                logLargeString(response.body());
                new FourDHTMLParser(response.body()).parse();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Process", "Failed to retrieve");
            }
        });

    }

    private Retrofit retroFitBuilder() {
        return new Retrofit.Builder().baseUrl("http://www.singaporepools.com.sg/en/product/Pages/").addConverterFactory(ScalarsConverterFactory.create()).build();
    }

    private void logLargeString(String str) {
        if(str.length() > 3000) {
            Log.i("Process", str.substring(0, 3000));
            logLargeString(str.substring(3000));
        } else {
            Log.i("Process", str); // continuation
        }
    }

    private String buildQueryParam() {
        FourDTotoQueryParamBuilder queryParamBuilder = new FourDTotoQueryParamBuilder("RHJhd051bWJlcj");
        queryParamBuilder.buildParam(4128);
        return queryParamBuilder.getQueryParam();
    }
}
