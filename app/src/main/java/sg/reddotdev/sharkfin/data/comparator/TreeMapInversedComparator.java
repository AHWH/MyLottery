/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.data.comparator;

import java.util.Comparator;


public class TreeMapInversedComparator implements Comparator<String> {
    @Override
    public int compare(String o1, String o2) {
        if(o1.compareTo(o2) > 0) {
            return -1;
        } else if(o1.compareTo(o2) < 0) {
            return 1;
        }
        return 0;
    }
}
