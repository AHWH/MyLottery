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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import sg.reddotdev.sharkfin.data.model.impl.FourDLotteryNumber;
import sg.reddotdev.sharkfin.data.model.impl.FourDLotteryResult;
import sg.reddotdev.sharkfin.data.parser.ResultParser;
import sg.reddotdev.sharkfin.util.LottoConst;
import sg.reddotdev.sharkfin.util.MonthConverter;


public class FourDHTMLParser implements ResultParser {
    private String response;
    private Element topTable;
    private Element midTable;
    private Element btmTable;

    public FourDHTMLParser(String response) {
        this.response = response;
    }

    @Override
    public void trimString() {
        Document doc = Jsoup.parse(response);
        Elements parentDiv = doc.select("div.four-d-results").first().select("div.tables-wrap");
        topTable = parentDiv.select("table.orange-header").first();
        midTable = parentDiv.select("table:not(.orange-header)").first();
        btmTable = parentDiv.select("table:not(.orange-header)").last();
    }

    public FourDLotteryResult parse() {
        trimString();

        int lotteryID = parseID();
        Calendar lotteryDate = parseDate();

        List<Integer> standAloneNos = filterStandaloneNo();

        List<FourDLotteryNumber> starterNos = parseNo(lotteryID, lotteryDate, LottoConst.SGPOOLS_4D_STARTER);
        List<FourDLotteryNumber> consolationNos = parseNo(lotteryID, lotteryDate, LottoConst.SGPOOLS_4D_CONSOLATION);

        return new FourDLotteryResult(lotteryID, lotteryDate, standAloneNos, starterNos, consolationNos);
    }

    private int parseID() {
        String lotteryIDStr = topTable.select("th.drawNumber").first().text();
        return Integer.parseInt(lotteryIDStr.substring(9));
    }

    private Calendar parseDate() {
        String lotteryDateStr = topTable.select("th.drawDate").first().text();
        lotteryDateStr = lotteryDateStr.substring(5);
        int day = Integer.parseInt(lotteryDateStr.substring(0,2));
        int month = MonthConverter.convert(lotteryDateStr.substring(3,6));
        int year = Integer.parseInt(lotteryDateStr.substring(7));
        return new GregorianCalendar(year, month, day);
    }


    private List<FourDLotteryNumber> parseNo(int lotteryID, Calendar lotteryDate, int type) {
        List<FourDLotteryNumber> standaloneLotteryNos = new ArrayList<>();
        List<String> standaloneNos = new ArrayList<>();
        switch (type) {
            case LottoConst.SGPOOLS_4D_STARTER:
                standaloneNos = filterStarterNo();
                break;
            case LottoConst.SGPOOLS_4D_CONSOLATION:
                standaloneNos = filterConsolationNo();
        }

        for(int i = 0; i < standaloneNos.size(); i++) {
            int standaloneNo = Integer.parseInt(standaloneNos.get(i));
            standaloneLotteryNos.add(new FourDLotteryNumber(standaloneNo, lotteryID, lotteryDate, type));
        }

        return standaloneLotteryNos;
    }

    private List<Integer> filterStandaloneNo() {
        Element standaloneNosNode = topTable.select("tbody").first();
        List<String> allNos = standaloneNosNode.select("tr > td").eachText();
        List<Integer> allNosInt = new ArrayList<>();
        for(String no: allNos) {
            allNosInt.add(Integer.parseInt(no));
        }
        return allNosInt;
    }

    private List<String> filterStarterNo() {
        Element starterNosNode = midTable.select("tbody").first();
        return starterNosNode.select("tr > td").eachText();
    }

    private List<String> filterConsolationNo() {
        Element consolationNosNode = btmTable.select("tbody").first();
        return consolationNosNode.select("tr > td").eachText();
    }

}
