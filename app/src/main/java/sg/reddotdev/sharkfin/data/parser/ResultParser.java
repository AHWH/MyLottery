/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.data.parser;

import sg.reddotdev.sharkfin.data.model.LotteryResult;

/*Blueprint for all ResultParser*/
public interface ResultParser {
    /*Parse the incoming String response into Model object*/
    LotteryResult parse();

    /*Need to trim String*/
    void trimString();

}
