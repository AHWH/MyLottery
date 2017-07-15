/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.data.parser;

import java.util.Calendar;

import sg.reddotdev.sharkfin.data.model.LotteryResult;

/**
 * Created by weihong on 15/7/17.
 */

public abstract class ResultParserBase implements ResultParser {
    protected String response;

    public ResultParserBase(String response) {
        this.response = response;
    }

    /*Master Caller*/
    public abstract LotteryResult parse();

    /*Need to trim String*/
    public abstract void trimString();

    /*Used to retrieve the lotteryID - identifier*/
    protected abstract int parseID();

    /*Used to retrieve the date of lottery draw - candidate identifier*/
    protected abstract Calendar parseDate();
}
