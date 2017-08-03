/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.data.parser.impl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.threeten.bp.ZonedDateTime;

import java.util.ArrayList;
import java.util.List;

import sg.reddotdev.sharkfin.data.model.impl.FourDLotteryNumber;
import sg.reddotdev.sharkfin.data.model.impl.FourDLotteryResult;
import sg.reddotdev.sharkfin.data.parser.ResultParserBase;
import sg.reddotdev.sharkfin.util.constants.LottoConst;


public class FourDHTMLParser extends ResultParserBase {
    private Element topTable;
    private Element midTable;
    private Element btmTable;

    public FourDHTMLParser(String response) {
        super(response);
    }

    public FourDLotteryResult parse() {
        trimString();

        int lotteryID = parseID();
        ZonedDateTime lotteryDate = parseDate();

        List<Integer> standAloneNos = parseStandaloneNo();

        List<FourDLotteryNumber> starterNos = parseNo(lotteryID, lotteryDate, LottoConst.SGPOOLS_4D_STARTER);
        List<FourDLotteryNumber> consolationNos = parseNo(lotteryID, lotteryDate, LottoConst.SGPOOLS_4D_CONSOLATION);

        return new FourDLotteryResult(lotteryID, lotteryDate, standAloneNos, starterNos, consolationNos);
    }

    public void trimString() {
        Document doc = Jsoup.parse(response);
        Elements parentDiv = doc.select("div.four-d-results").first().select("div.tables-wrap");
        topTable = parentDiv.select("table.orange-header").first();
        midTable = parentDiv.select("table:not(.orange-header)").first();
        btmTable = parentDiv.select("table:not(.orange-header)").last();
    }


    protected int parseID() {
        String lotteryIDStr = topTable.select("th.drawNumber").first().text();
        return Integer.parseInt(lotteryIDStr.substring(9));
    }

    protected ZonedDateTime parseDate() {
        String lotteryDateStr = topTable.select("th.drawDate").first().text();
        return parseDate(lotteryDateStr);
    }


    private List<FourDLotteryNumber> parseNo(int lotteryID, ZonedDateTime lotteryDate, int type) {
        List<FourDLotteryNumber> standaloneLotteryNos = new ArrayList<>();
        List<String> standaloneNos = new ArrayList<>();
        switch (type) {
            case LottoConst.SGPOOLS_4D_STARTER:
                standaloneNos = filterNos(midTable);
                break;
            case LottoConst.SGPOOLS_4D_CONSOLATION:
                standaloneNos = filterNos(btmTable);
        }

        for(int i = 0; i < standaloneNos.size(); i++) {
            int standaloneNo = Integer.parseInt(standaloneNos.get(i));
            standaloneLotteryNos.add(new FourDLotteryNumber(standaloneNo, lotteryID, lotteryDate, type));
        }

        return standaloneLotteryNos;
    }

    private List<Integer> parseStandaloneNo() {
        List<String> allNos = filterNos(topTable);
        List<Integer> allNosInt = new ArrayList<>();
        for(String no: allNos) {
            allNosInt.add(Integer.parseInt(no));
        }
        return allNosInt;
    }
}
