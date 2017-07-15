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

import sg.reddotdev.sharkfin.data.model.impl.BigSweepLottery2DNumber;
import sg.reddotdev.sharkfin.data.model.impl.BigSweepLottery3DNumber;
import sg.reddotdev.sharkfin.data.model.impl.BigSweepLottery7DNumber;
import sg.reddotdev.sharkfin.data.model.impl.BigSweepLotteryResult;
import sg.reddotdev.sharkfin.data.parser.ResultParserBase;
import sg.reddotdev.sharkfin.util.constants.LottoConst;
import sg.reddotdev.sharkfin.util.MonthConverter;

public class BigSweepHTMLParser extends ResultParserBase {
    private Element topHeader;
    private Element standalonePrizesTable;
    private Element superSweepTable;
    private Element cascadeTable;
    private Element jackpotPrizesTable;
    private Element luckyPrizesTable;
    private Element giftPrizesTable;
    private Element consolationPrizesTable;
    private Element participationPrizesTable;
    private Element delight2DPrizesTable;
    private Element delight3DPrizesTable;

    public BigSweepHTMLParser(String response) {
        super(response);
    }

    public BigSweepLotteryResult parse() {
        trimString();

        int lotteryID = parseID();
        Calendar date = parseDate();

        List<Integer> standalonePrizes = parseStandalone();

        String superSweepPrize = superSweepTable == null ? "" : parseSuperSweep();
        int cascadePrize = cascadeTable == null ? 0 : parseCascadePrize();

        List<BigSweepLottery7DNumber> jackpotPrizes = parse7DNo(lotteryID, date, LottoConst.SGPOOLS_SWEEP_JACKPOT);
        List<BigSweepLottery7DNumber> luckyPrizes = parse7DNo(lotteryID, date, LottoConst.SGPOOLS_SWEEP_LUCKY);
        List<BigSweepLottery7DNumber> giftPrizes = parse7DNo(lotteryID, date, LottoConst.SGPOOLS_SWEEP_GIFT);
        List<BigSweepLottery7DNumber> consolationPrizes = parse7DNo(lotteryID, date, LottoConst.SGPOOLS_SWEEP_CONSOLATION);
        List<BigSweepLottery7DNumber> participationPrizes = participationPrizesTable == null ? new ArrayList<BigSweepLottery7DNumber>() : parse7DNo(lotteryID, date, LottoConst.SGPOOLS_SWEEP_PARTICIPATION);

        List<BigSweepLottery2DNumber> delight2DPrizes = delight2DPrizesTable == null ? new ArrayList<BigSweepLottery2DNumber>() : parse2DNo(lotteryID, date);
        List<BigSweepLottery3DNumber> delight3DPrizes = delight3DPrizesTable == null ? new ArrayList<BigSweepLottery3DNumber>() : parse3DNo(lotteryID, date);

        return new BigSweepLotteryResult(lotteryID, date, standalonePrizes, superSweepPrize, cascadePrize, jackpotPrizes, luckyPrizes, giftPrizes, consolationPrizes, participationPrizes, delight2DPrizes, delight3DPrizes);
    }

    public void trimString() {
        Document doc = Jsoup.parse(response);
        Element parentDiv = doc.select("div.swpr").first().select("div.tables-wrap").first();
        topHeader = parentDiv.select("table.orange-header").first();
        standalonePrizesTable = parentDiv.select("table:not(.orange-header)").first();


        Elements outerTables = parentDiv.select("div.table-responsive");
        /*CAN BE NULL!!!!*/
        Elements expandedTables = outerTables.select("table:not(.expandable-container)");
        int count = 0;
        for(Element e: expandedTables) {
            count++;
        }

        if(count == 3) {
            superSweepTable = expandedTables.eq(0).first();
            cascadeTable = null;
        } else if(count == 4) {
            superSweepTable = expandedTables.eq(0).first();
            cascadeTable = expandedTables.eq(1).first();
        } else {
            superSweepTable = null;
            cascadeTable = null;
        }

        jackpotPrizesTable = expandedTables.eq(count-2).first();
        luckyPrizesTable = expandedTables.eq(count-1).last();


        giftPrizesTable = outerTables.select("table[prizecode=06]").first();
        consolationPrizesTable = outerTables.select("table[prizecode=07]").first();
        participationPrizesTable = outerTables.select("table[prizecode=08]").first();
        delight2DPrizesTable = outerTables.select("table[prizecode=60]").first();
        delight3DPrizesTable = outerTables.select("table[prizecode=23]").first();
    }

    protected int parseID() {
        String lotteryIDStr = topHeader.select("th.drawNumber").first().text().substring(9);
        lotteryIDStr = lotteryIDStr.replaceAll("/", "");
        return Integer.parseInt(lotteryIDStr);
    }

    protected Calendar parseDate() {
        String lotteryDateStr = topHeader.select("th.drawDate").first().text();
        lotteryDateStr = lotteryDateStr.substring(5);
        int day = Integer.parseInt(lotteryDateStr.substring(0,2));
        int month = MonthConverter.convert(lotteryDateStr.substring(3,6));
        int year = Integer.parseInt(lotteryDateStr.substring(7));
        return new GregorianCalendar(year, month, day);
    }

    private List<Integer> parseStandalone() {
        Element standaloneNosNode = standalonePrizesTable.select("tbody").first();
        List<String> allNos = standaloneNosNode.select("tr > td").eachText();
        List<Integer> allNosInt = new ArrayList<>();
        for(String no: allNos) {
            allNosInt.add(Integer.parseInt(no));
        }
        return allNosInt;
    }

    private String parseSuperSweep() {
        Element superSweepNode = superSweepTable.select("tbody").first();
        String superSweepStr = superSweepNode.select("tr > td").eachText().get(0);
        return superSweepStr.substring(0, 8);
    }

    private int parseCascadePrize() {
        Element cascadeNode = cascadeTable.select("tbody").first();
        String cascadePrizeStr = cascadeNode.select("tr > td").eachText().get(0);
        return Integer.parseInt(cascadePrizeStr.substring(0, 5));
    }

    private List<BigSweepLottery7DNumber> parse7DNo(int lotteryID, Calendar date, int type) {
        List<BigSweepLottery7DNumber> sweepNos = new ArrayList<>();
        List<String> sweepNosStr = new ArrayList<>();
        switch (type) {
            case LottoConst.SGPOOLS_SWEEP_JACKPOT:
                sweepNosStr = filterJackpotNo();
                break;
            case LottoConst.SGPOOLS_SWEEP_LUCKY:
                sweepNosStr = filterLuckyNo();
                break;
            case LottoConst.SGPOOLS_SWEEP_GIFT:
                sweepNosStr = filterGiftNo();
                break;
            case LottoConst.SGPOOLS_SWEEP_CONSOLATION:
                sweepNosStr = filterConsolationNo();
                break;
            case LottoConst.SGPOOLS_SWEEP_PARTICIPATION:
                sweepNosStr = filterParticipationNo();
        }

        for(int i = 0; i < sweepNosStr.size(); i++) {
            int sweepNo = Integer.parseInt(sweepNosStr.get(i));
            sweepNos.add(new BigSweepLottery7DNumber(sweepNo, lotteryID, date, type));
        }

        return sweepNos;
    }

    private List<BigSweepLottery2DNumber> parse2DNo(int lotteryID, Calendar date) {
        List<BigSweepLottery2DNumber> sweepNos = new ArrayList<>();
        List<String> sweepNosStr = filterDelight2DNo();
        for(int i = 0; i < sweepNosStr.size(); i++) {
            int sweepNo = Integer.parseInt(sweepNosStr.get(i));
            sweepNos.add(new BigSweepLottery2DNumber(sweepNo, lotteryID, date, LottoConst.SGPOOLS_SWEEP_2D));
        }

        return sweepNos;
    }

    private List<BigSweepLottery3DNumber> parse3DNo(int lotteryID, Calendar date) {
        List<BigSweepLottery3DNumber> sweepNos = new ArrayList<>();
        List<String> sweepNosStr = filterDelight3DNo();
        for(int i = 0; i < sweepNosStr.size(); i++) {
            int sweepNo = Integer.parseInt(sweepNosStr.get(i));
            sweepNos.add(new BigSweepLottery3DNumber(sweepNo, lotteryID, date, LottoConst.SGPOOLS_SWEEP_3D));
        }

        return sweepNos;
    }


    private List<String> filterJackpotNo() {
        Element jackpotNosNode = jackpotPrizesTable.select("tbody").first();
        return jackpotNosNode.select("tr > td").eachText();
    }

    private List<String> filterLuckyNo() {
        Element luckyNosNode = luckyPrizesTable.select("tbody").first();
        return luckyNosNode.select("tr > td").eachText();
    }

    private List<String> filterGiftNo() {
        Element giftNosNode = giftPrizesTable.select("tbody").first();
        return giftNosNode.select("tr > td").eachText();
    }

    private List<String> filterConsolationNo() {
        Element consolationNosNode = consolationPrizesTable.select("tbody").first();
        return consolationNosNode.select("tr > td").eachText();
    }

    private List<String> filterParticipationNo() {
        Element participationNosNode = participationPrizesTable.select("tbody").first();
        return participationNosNode.select("tr > td").eachText();
    }

    private List<String> filterDelight2DNo() {
        Element delight2DNode = delight2DPrizesTable.select("tbody").first();
        return delight2DNode.select("tr > td").eachText();
    }

    private List<String> filterDelight3DNo() {
        Element delight3DNode = delight3DPrizesTable.select("tbody").first();
        return delight3DNode.select("tr > td").eachText();
    }
}
