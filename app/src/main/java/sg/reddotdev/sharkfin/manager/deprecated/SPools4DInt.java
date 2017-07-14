/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.manager.deprecated;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SPools4DInt {
    @GET("4d_results.aspx")
    Call<String> getResult(@Query("sppl") String sppl);

}
