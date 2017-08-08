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

import javax.annotation.Nullable;

import sg.reddotdev.sharkfin.data.model.LotteryResult;
import sg.reddotdev.sharkfin.data.model.impl.BigSweepLottery2DNumber;
import sg.reddotdev.sharkfin.data.model.impl.BigSweepLottery3DNumber;
import sg.reddotdev.sharkfin.data.model.impl.BigSweepLottery7DNumber;
import sg.reddotdev.sharkfin.data.model.impl.BigSweepLotteryResult;
import sg.reddotdev.sharkfin.data.parser.ResultParserBase;
import sg.reddotdev.sharkfin.util.constants.LottoConst;

public class BigSweepHTMLParser extends ResultParserBase {
    private Element topHeader;
    private Element standalonePrizesTable;
    @Nullable
    private Element superSweepTable;
    @Nullable
    private Element cascadeTable;
    private Element jackpotPrizesTable;
    private Element luckyPrizesTable;
    private Element giftPrizesTable;
    private Element consolationPrizesTable;
    @Nullable
    private Element participationPrizesTable;
    private Element delight2DPrizesTable;
    @Nullable
    private Element delight3DPrizesTable;

    public BigSweepHTMLParser(String response) {
        super(response);
    }

    public LotteryResult parse() {
        trimString();

        int lotteryID = parseID();
        ZonedDateTime date = parseDate();

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
        @Nullable
        Elements expandedTables = outerTables.select("table:not(.expandable-container)");
        int count = 0;
        if(expandedTables != null) {
            for(Element e: expandedTables) {
                count++;
            }
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

    protected ZonedDateTime parseDate() {
        String lotteryDateStr = topHeader.select("th.drawDate").first().text();
        return parseDate(lotteryDateStr);
    }

    private List<Integer> parseStandalone() {
        List<String> allNos = filterNos(standalonePrizesTable);
        List<Integer> allNosInt = new ArrayList<>();
        for(String no: allNos) {
            allNosInt.add(Integer.parseInt(no));
        }
        return allNosInt;
    }

    private String parseSuperSweep() {
        String superSweepStr = filterNos(superSweepTable).get(0);
        return superSweepStr.substring(0, 8);
    }

    private int parseCascadePrize() {
        String cascadePrizeStr = filterNos(cascadeTable).get(0);
        return Integer.parseInt(cascadePrizeStr.substring(0, 5));
    }

    private List<BigSweepLottery7DNumber> parse7DNo(int lotteryID, ZonedDateTime date, int type) {
        List<BigSweepLottery7DNumber> sweepNos = new ArrayList<>();
        List<String> sweepNosStr = new ArrayList<>();
        switch (type) {
            case LottoConst.SGPOOLS_SWEEP_JACKPOT:
                sweepNosStr = filterNos(jackpotPrizesTable);
                break;
            case LottoConst.SGPOOLS_SWEEP_LUCKY:
                sweepNosStr = filterNos(luckyPrizesTable);
                break;
            case LottoConst.SGPOOLS_SWEEP_GIFT:
                sweepNosStr = filterNos(giftPrizesTable);
                break;
            case LottoConst.SGPOOLS_SWEEP_CONSOLATION:
                sweepNosStr = filterNos(consolationPrizesTable);
                break;
            case LottoConst.SGPOOLS_SWEEP_PARTICIPATION:
                sweepNosStr = filterNos(participationPrizesTable);
        }

        for(int i = 0; i < sweepNosStr.size(); i++) {
            int sweepNo = Integer.parseInt(sweepNosStr.get(i));
            sweepNos.add(new BigSweepLottery7DNumber(sweepNo, lotteryID, date, type));
        }

        return sweepNos;
    }

    private List<BigSweepLottery2DNumber> parse2DNo(int lotteryID, ZonedDateTime date) {
        List<BigSweepLottery2DNumber> sweepNos = new ArrayList<>();
        List<String> sweepNosStr = filterNos(delight2DPrizesTable);
        for(int i = 0; i < sweepNosStr.size(); i++) {
            int sweepNo = Integer.parseInt(sweepNosStr.get(i));
            sweepNos.add(new BigSweepLottery2DNumber(sweepNo, lotteryID, date, LottoConst.SGPOOLS_SWEEP_2D));
        }

        return sweepNos;
    }

    private List<BigSweepLottery3DNumber> parse3DNo(int lotteryID, ZonedDateTime date) {
        List<BigSweepLottery3DNumber> sweepNos = new ArrayList<>();
        List<String> sweepNosStr = filterNos(delight3DPrizesTable);
        for(int i = 0; i < sweepNosStr.size(); i++) {
            int sweepNo = Integer.parseInt(sweepNosStr.get(i));
            sweepNos.add(new BigSweepLottery3DNumber(sweepNo, lotteryID, date, LottoConst.SGPOOLS_SWEEP_3D));
        }

        return sweepNos;
    }
}
