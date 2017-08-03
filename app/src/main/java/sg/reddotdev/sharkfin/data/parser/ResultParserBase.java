/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.data.parser;

import org.jsoup.nodes.Element;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;

import java.util.List;

import sg.reddotdev.sharkfin.data.model.LotteryResult;
import sg.reddotdev.sharkfin.util.CalendarConverter;
import sg.reddotdev.sharkfin.util.constants.AppLocale;

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
    protected abstract ZonedDateTime parseDate();

    protected ZonedDateTime parseDate(String dateStr) {
        dateStr = dateStr.substring(5);
        int day = Integer.parseInt(dateStr.substring(0,2));
        int month = CalendarConverter.month3CharToNoConvert(dateStr.substring(3,6));
        int year = Integer.parseInt(dateStr.substring(7));
        return ZonedDateTime.of(year, month, day, 18, 30, 0, 0, AppLocale.gmt8Zone);
    }

    protected List<String> filterNos(Element el) {
        Element tableNode = el.select("tbody").first();
        return tableNode.select("tr > td").eachText();
    }
}
