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

import sg.reddotdev.sharkfin.data.model.Branch;
import sg.reddotdev.sharkfin.data.model.TotoWinner;
import sg.reddotdev.sharkfin.data.model.impl.TotoLotteryNumber;
import sg.reddotdev.sharkfin.data.model.impl.TotoLotteryResult;
import sg.reddotdev.sharkfin.data.model.impl.TotoWinningBoard;
import sg.reddotdev.sharkfin.data.parser.ResultParserBase;
import sg.reddotdev.sharkfin.util.constants.LottoConst;
import sg.reddotdev.sharkfin.util.MonthConverter;


public class TotoHTMLParser extends ResultParserBase {
    private Element headerDiv;
    private Element winNumsDiv;
    private Element addWinNumDiv;
    private Element grp1PrizeAmtDiv;
    private Element winPropDiv;

    private Element winningOutletsParentDiv;

    /*Surface to global variable for efficiency*/
    private boolean noGrp1Winner;
    private List<Double> grp1To4Prizes;
    private List<TotoWinner> grp1Winners;

    public TotoHTMLParser(String response) {
        super(response);
    }

    public TotoLotteryResult parse() {
        trimString();

        int lotteryID = parseID();
        Calendar date = parseDate();

        List<TotoLotteryNumber> winningNums = parseNo(lotteryID, date, LottoConst.SGPOOLS_TOTO_NUM);
        int addWinningNum = parseAddWinningNum();

        TotoWinningBoard totoWinningBoard = parseWinningBoard(lotteryID, date);

        double grp1Prize = parseFirstPrizeAmt();

        return new TotoLotteryResult(lotteryID, date, winningNums, addWinningNum, grp1Prize, totoWinningBoard);
    }


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


    protected int parseID() {
        String lotteryIDStr = headerDiv.select("th.drawNumber").first().text();
        return Integer.parseInt(lotteryIDStr.substring(9));
    }

    protected Calendar parseDate() {
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
        grp1To4Prizes = new ArrayList<>();
        List<Integer> grp3To7Winners = new ArrayList<>();
        noGrp1Winner = false;
        boolean noGrp2Winner = false;


        for(int i = 1; i < winningDist.size(); i++) {
            String winDistStr = winningDist.get(i);
            boolean noWinner = winDistStr.equals("-");
            int mathResult = i%3;

            /*For i%3 == 1 (prize share/person), == 2 (no of winners)*/
            if(mathResult > 0) {
                /*Update boolean*/
                if(i == 1) {
                    noGrp1Winner = noWinner;
                } else if (i == 4) {
                    noGrp2Winner = noWinner;
                }

                /*Fill the relevant list*/
                /*Adds 0 if no winner*/
                if (mathResult == 1 && i < 11) {
                    winDistStr = winDistStr.replaceAll("\\$|,", "");
                    grp1To4Prizes.add(noWinner ? 0 : Double.parseDouble(winDistStr));
                } else if (mathResult == 2 && i >= 8) {
                    winDistStr = winDistStr.replaceAll(",", "");
                    grp3To7Winners.add(noWinner ? 0 : Integer.parseInt(winDistStr));
                }

            }

        }

        grp1Winners = new ArrayList<>();
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


    private int parseAddWinningNum() {
        Element additionalWinningNumberNode = addWinNumDiv.select("tbody").first();
        return Integer.parseInt(additionalWinningNumberNode.select("tr > td").eachText().get(0));
    }

    private double parseFirstPrizeAmt() {
        if(grp1PrizeAmtDiv != null) {
            Element firstPrizeAmtTableBody = grp1PrizeAmtDiv.select("tbody").first();
            String amtStr = firstPrizeAmtTableBody.select("tr > td").eachText().get(0);
            amtStr = amtStr.replaceAll("\\$|,", "");
            return Double.parseDouble(amtStr);
        }

        if(!noGrp1Winner) {
            return grp1To4Prizes.get(0) / grp1Winners.size();
        } else {
            return filterSnowballedGrp1Prize();
        }
    }



    private List<String> filterWinningNums() {
        Element winningNumbersTableBody = winNumsDiv.select("tbody").first();
        return winningNumbersTableBody.select("tr > td").eachText();
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

    private double filterSnowballedGrp1Prize() {
        Elements snowballPrizeAmtPara = winningOutletsParentDiv.select("p").eq(1);
        String snowballPrizeAmtParaStr = snowballPrizeAmtPara.eachText().get(0);
        int dollarIndex = snowballPrizeAmtParaStr.indexOf("$");
        String snowballAmtStr = snowballPrizeAmtParaStr.substring(dollarIndex+1, snowballPrizeAmtParaStr.indexOf("\\s", dollarIndex));
        return Double.parseDouble(snowballAmtStr);
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
