/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.data.parser.impl;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import sg.reddotdev.sharkfin.data.model.Branch;
import sg.reddotdev.sharkfin.data.model.TotoWinner;
import sg.reddotdev.sharkfin.data.model.impl.TotoLotteryNumber;
import sg.reddotdev.sharkfin.data.model.impl.TotoLotteryResult;
import sg.reddotdev.sharkfin.data.model.impl.TotoWinningBoard;
import sg.reddotdev.sharkfin.data.parser.ResultParser;
import sg.reddotdev.sharkfin.util.LottoConst;
import sg.reddotdev.sharkfin.util.MonthConverter;


public class TotoHTMLParser implements ResultParser {
    private String response;

    private Element headerDiv;
    private Element winNumsDiv;
    private Element addWinNumDiv;
    private Element grp1PrizeAmtDiv;
    private Element winPropDiv;

    private Element winningOutletsParentDiv;

    public TotoHTMLParser(String response) {
        this.response = response;
    }

    @Override
    public void trimString() {
        Document doc = Jsoup.parse(response);
        Elements resultsParentDiv = doc.select("div.toto-result").first().select("div.tables-wrap");
        headerDiv = resultsParentDiv.select("table.orange-header").first();
        winNumsDiv = resultsParentDiv.select("table:not(.orange-header)").first();
        addWinNumDiv = resultsParentDiv.select("table:not(.orange-header)").eq(1).first();
        /*NOTE THIS will return null for some results earlier than a specific date (eg. RHJhd051bWJlcj0yMjg5) */
        /*TODO: CHECKs*/
        grp1PrizeAmtDiv = resultsParentDiv.select("table.jackpotPrizeTable").first();
        winPropDiv = resultsParentDiv.select("table.tableWinningShares").first();

        winningOutletsParentDiv= doc.select("div.divWinningOutlets").first();
    }

    @Override
    public TotoLotteryResult parse() {
        trimString();

        int lotteryID = parseID();
        Calendar date = parseDate();

        List<TotoLotteryNumber> winningNums = parseNo(lotteryID, date, LottoConst.SGPOOLS_TOTO_NUM);
        int addWinningNum = filterAddWinningNum();

        double grp1Prize = filterFirstPrizeAmt();

        TotoWinningBoard totoWinningBoard = parseWinningBoard(lotteryID, date);

        return new TotoLotteryResult(lotteryID, date, winningNums, addWinningNum, grp1Prize, totoWinningBoard);
    }


    private int parseID() {
        String lotteryIDStr = headerDiv.select("th.drawNumber").first().text();
        return Integer.parseInt(lotteryIDStr.substring(9));
    }

    private Calendar parseDate() {
        String lotteryDateStr = headerDiv.select("th.drawDate").first().text();
        lotteryDateStr = lotteryDateStr.substring(5);
        int day = Integer.parseInt(lotteryDateStr.substring(0,2));
        int month = MonthConverter.convert(lotteryDateStr.substring(3,6));
        int year = Integer.parseInt(lotteryDateStr.substring(7));
        return new GregorianCalendar(year, month, day);
    }

    private List<TotoLotteryNumber> parseNo(int lotteryID, Calendar date, int type) {
        List<String> winningNumsStr = filterWinningNums();
        List<TotoLotteryNumber> winningNums = new ArrayList<>();

        for(String winNumStr: winningNumsStr) {
            int winNum = Integer.parseInt(winNumStr);
            winningNums.add(new TotoLotteryNumber(winNum, lotteryID, date, type));
        }

        return winningNums;
    }

    private TotoWinningBoard parseWinningBoard(int lotteryID, Calendar date) {
        List<String> winningDist = filterWinningDistribution();
        List<Double> grp1To4Prizes = new ArrayList<>();
        List<Integer> grp3To7Winners = new ArrayList<>();
        boolean noGrp1Winner = false;
        boolean noGrp2Winner = false;


        for(int i = 1; i < winningDist.size(); i++) {
            String winDistStr = winningDist.get(i);
            boolean noWinner = winDistStr.equals("-");

            /*When i%3 = 1, it is the divided price amt*/
            if(i % 3 == 1) {
                /*if i = 1 or 4, update the boolean*/
                if(i == 1) {
                    noGrp1Winner = noWinner;
                } else if (i == 4) {
                    noGrp2Winner = noWinner;
                }

                /*Only update the price amt for Grp 1 to 4*/
                if(i < 11) {
                    winDistStr = winDistStr.replaceAll("\\$|,", "");
                    grp1To4Prizes.add(noWinner ? 0 : Double.parseDouble(winDistStr));
                }
            } else if(i % 3 == 2 && i >= 8) {
                /*When i%3 == 2, it is the no of ppl that wins in the group*/
                /*Only applicable for grp 3 onwards*/
                winDistStr = winDistStr.replaceAll(",", "");
                grp3To7Winners.add(noWinner ? 0 : Integer.parseInt(winDistStr));
            }

        }

        List<TotoWinner> grp1Winners = new ArrayList<>();
        List<TotoWinner> grp2Winners = new ArrayList<>();


        /*Check for Winners*/


        if(!noGrp1Winner) {
            List<String> filteredList = filterGrp1Wins();
            addTotoWinners(filteredList, grp1Winners, lotteryID, date, LottoConst.SGPOOLS_TOTO_WIN_G1);
        }

        if(!noGrp2Winner) {
            List<String> filteredList = filterGrp2Wins();
            addTotoWinners(filteredList, grp2Winners, lotteryID, date, LottoConst.SGPOOLS_TOTO_WIN_G2);
        }

        return new TotoWinningBoard(lotteryID, date, grp1To4Prizes, grp3To7Winners, grp1Winners, grp2Winners);
    }


    private List<String> filterWinningNums() {
        Element winningNumbersTableBody = winNumsDiv.select("tbody").first();
        return winningNumbersTableBody.select("tr > td").eachText();
    }

    private int filterAddWinningNum() {
        Element additionalWinningNumberNode = addWinNumDiv.select("tbody").first();
        return Integer.parseInt(additionalWinningNumberNode.select("tr > td").eachText().get(0));
    }

    private double filterFirstPrizeAmt() {
        Element firstPrizeAmtTableBody = grp1PrizeAmtDiv.select("tbody").first();
        String amtStr = firstPrizeAmtTableBody.select("tr > td").eachText().get(0);
        amtStr = amtStr.replaceAll("\\$|,", "");
        return Double.parseDouble(amtStr);
    }

    private List<String> filterWinningDistribution() {
        Element winningDistTableBody = winPropDiv.select("tbody").first();
        return winningDistTableBody.select("tr > td").eachText();
    }

    private List<String> filterGrp1Wins() {
        Element grp1WinningPlacesUl = winningOutletsParentDiv.select("ul").first();
        return grp1WinningPlacesUl.select("li").eachText();
    }

    private List<String> filterGrp2Wins() {
        Element grp2WinningPlacesUl = winningOutletsParentDiv.select("ul").last();
        return grp2WinningPlacesUl.select("li").eachText();
    }

    private void addTotoWinners(List<String> filteredList, List<TotoWinner> winners, int lotteryID, Calendar date, int winningGrp) {
        for(int i = 0; i < filteredList.size(); i++) {
            String winningPlaceFullStr = filteredList.get(i);
            String[] winningPlaceArr = winningPlaceFullStr.split("\\s+-\\s+|\\(|\\)");
            String branchName = winningPlaceArr[0];
            String branchAddress = winningPlaceArr[1];
            Branch branch = new Branch(branchName, branchAddress);

            String betType = winningPlaceArr[2];
            winners.add(new TotoWinner(lotteryID, date, i, branch, winningGrp, betType));
        }
    }
}
