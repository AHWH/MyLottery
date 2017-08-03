/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.data.adapter;

import org.threeten.bp.ZonedDateTime;

import java.util.List;

import sg.reddotdev.sharkfin.R;
import sg.reddotdev.sharkfin.data.model.impl.BigSweepLotteryResult;
import sg.reddotdev.sharkfin.util.CalendarConverter;
import sg.reddotdev.sharkfin.util.constants.AppLocale;
import sg.reddotdev.sharkfin.util.constants.BigSweepPrizeAmt;
import sg.reddotdev.sharkfin.view.viewholder.NewLayoutViewHolder;
import sg.reddotdev.sharkfin.view.viewholder.NormalLayoutViewHolder;

public class BigSweepRecyclerAdapter extends BaseMainRecyclerAdapter {
    private String LOGTAG = getClass().getSimpleName();

    public BigSweepRecyclerAdapter(List<Object> lotteryResultList) {
        super(lotteryResultList);
    }

    @Override
    protected void configureNormalResultViewHolder(NormalLayoutViewHolder holder, int position, List<Object> lotteryResultList) {
        BigSweepLotteryResult bigSweepLotteryResult = (BigSweepLotteryResult) lotteryResultList.get(position);
        ZonedDateTime lotteryDate = bigSweepLotteryResult.getDate();
        holder.getDate().setText(Integer.toString(lotteryDate.getDayOfMonth()));
        holder.getDay().setText(CalendarConverter.dayNoTo3CharConvert(lotteryDate.getDayOfWeek().getValue()));

        String lotteryID = getContext().getText(R.string.main_drawNo) + " " + formatLotteryId(bigSweepLotteryResult.getLotteryID());
        holder.getDrawNo().setText(lotteryID);
        String firstPrize = getContext().getText(R.string.bigSweep_main_firstPrizeAmt) + " $" + getFirstPrizeAmt(lotteryDate);
        holder.getFirstTextView().setText(firstPrize);
    }

    @Override
    protected void configureNewResultViewHolder(NewLayoutViewHolder holder, int position, List<Object> lotteryResultList) {
        BigSweepLotteryResult bigSweepLotteryResult = (BigSweepLotteryResult) lotteryResultList.get(position);
        ZonedDateTime lotteryDate = bigSweepLotteryResult.getDate();
        holder.getDate().setText(Integer.toString(lotteryDate.getDayOfMonth()));
        holder.getDay().setText(CalendarConverter.dayNoTo3CharConvert(lotteryDate.getDayOfWeek().getValue()));

        String lotteryIDStr = getContext().getText(R.string.main_drawNo) + " " + formatLotteryId(bigSweepLotteryResult.getLotteryID());
        holder.getDrawNo().setText(lotteryIDStr);
        String firstPrize = getContext().getText(R.string.bigSweep_main_firstPrizeAmt) + " $" + getFirstPrizeAmt(lotteryDate);
        holder.getFirstTextView().setText(firstPrize);
        String firstWinningNo = getContext().getText(R.string.bigSweep_main_firstPrize) + " " + Integer.toString(bigSweepLotteryResult.getFirstNumber());
        holder.getSecondTextView().setText(firstWinningNo);
        String secondWinningNo = getContext().getText(R.string.bigSweep_main_secondPrize) + " " + Integer.toString(bigSweepLotteryResult.getSecondNumber());
        holder.getThirdTextView().setText(secondWinningNo);
    }

    private String formatLotteryId(int lotteryID) {
        int month = lotteryID / 10000;
        int year = lotteryID % 10000;

        String formattedLotteryId = month + "/" + year;
        return month < 10 ? "0" + formattedLotteryId : formattedLotteryId;
    }

    private String getFirstPrizeAmt(ZonedDateTime lotteryDate) {
        int lotteryMonth = lotteryDate.getMonth().getValue();
        int lotteryYear = lotteryDate.getYear();

        if(lotteryYear <= 2004 & lotteryMonth <= 1) {
            return AppLocale.decimalFormat.format(BigSweepPrizeAmt.firstGenPrizes.firstPrizeAmt);
        } else if(lotteryYear <= 2007 & lotteryMonth <= 7) {
            return AppLocale.decimalFormat.format(BigSweepPrizeAmt.secondGenPrizes.firstPrizeAmt);
        } else if(lotteryYear <= 2013 & lotteryMonth <= 4) {
            return AppLocale.decimalFormat.format(BigSweepPrizeAmt.thirdGenPrizes.firstPrizeAmt);
        } else {
            return AppLocale.decimalFormat.format(BigSweepPrizeAmt.fourthGenPrizes.firstPrizeAmt);
        }
    }
}
