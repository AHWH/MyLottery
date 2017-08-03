/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.data.network;

import com.androidnetworking.common.ANRequest;

public interface ANRequestInt {
    ANRequest createRequest(int drawNo);

    String buildQueryParam(int drawNo);
}
