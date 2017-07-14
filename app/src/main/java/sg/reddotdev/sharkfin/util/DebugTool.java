/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.util;

import android.util.Log;

public class DebugTool {
    public void logLargeString(String str) {
        if(str.length() > 3000) {
            Log.i("Process", str.substring(0, 3000));
            logLargeString(str.substring(3000));
        } else {
            Log.i("Process", str); // continuation
        }
    }
}

