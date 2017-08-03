/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.data.adapter;

import android.util.Log;

import org.threeten.bp.ZonedDateTime;

import java.util.List;

import sg.reddotdev.sharkfin.R;
import sg.reddotdev.sharkfin.data.model.impl.TotoLotteryNumber;
import sg.reddotdev.sharkfin.data.model.impl.TotoLotteryResult;
import sg.reddotdev.sharkfin.util.CalendarConverter;
import sg.reddotdev.sharkfin.util.constants.AppLocale;
import sg.reddotdev.sharkfin.view.viewholder.NewLayoutViewHolder;
import sg.reddotdev.sharkfin.view.viewholder.NormalLayoutViewHolder;

public class TotoMainRecyclerAdapter extends BaseMainRecyclerAdapter {
    private String LOGTAG = getClass().getSimpleName();

    public TotoMainRecyclerAdapter(List<Object> lotteryResultList) {
        super(lotteryResultList);
    }

    @Override
    protected void configureNormalResultViewHolder(NormalLayoutViewHolder holder, int position, List<Object> totoLotteryResultList) {
        Log.d(LOGTAG, "Yoohoo!");
        TotoLotteryResult totoLotteryResult = (TotoLotteryResult) totoLotteryResultList.get(position);
        ZonedDateTime lotteryDate = totoLotteryResult.getDate();
        holder.getDate().setText(Integer.toString(lotteryDate.getDayOfMonth()));
        holder.getDay().setText(CalendarConverter.dayNoTo3CharConvert(lotteryDate.getDayOfWeek().getValue()));

        String lotteryID = getContext().getText(R.string.main_drawNo) + " " + Integer.toString(totoLotteryResult.getLotteryID());
        holder.getDrawNo().setText(lotteryID);
        String firstPrize = getContext().getText(R.string.toto_main_firstPrizeAmt) + " $" + AppLocale.decimalFormat.format(totoLotteryResult.getFirstPrizeAmt());
        holder.getFirstTextView().setText(firstPrize);
    }

    @Override
    protected void configureNewResultViewHolder(NewLayoutViewHolder holder, int position, List<Object> totoLotteryResultList) {
        TotoLotteryResult totoLotteryResult = (TotoLotteryResult) totoLotteryResultList.get(position);
        ZonedDateTime lotteryDate = totoLotteryResult.getDate();
        holder.getDate().setText(Integer.toString(lotteryDate.getDayOfMonth()));
        holder.getDay().setText(CalendarConverter.dayNoTo3CharConvert(lotteryDate.getDayOfWeek().getValue()));

        String lotteryID = getContext().getText(R.string.main_drawNo) + " " + Integer.toString(totoLotteryResult.getLotteryID());
        holder.getDrawNo().setText(lotteryID);
        String firstPrize = getContext().getText(R.string.toto_main_firstPrizeAmt) + " $" + AppLocale.decimalFormat.format(totoLotteryResult.getFirstPrizeAmt());
        holder.getFirstTextView().setText(firstPrize);
        String winningNos = getContext().getText(R.string.toto_main_winningNos) + " " + getWinningNos(totoLotteryResult.getWinningNumbers());
        holder.getSecondTextView().setText(winningNos);
        String addWinningNo = getContext().getText(R.string.toto_main_addWinningNos) + " " + Integer.toString(totoLotteryResult.getAdditionalNumber());
        holder.getThirdTextView().setText(addWinningNo);
    }

    private String getWinningNos(List<TotoLotteryNumber> winningNos) {
        String returnedList = "";
        for (int i = 0; i < 3; i++) {
            returnedList += winningNos.get(i).getNum();
            if(i < 2) {
                returnedList += " | ";
            }
        }
        return returnedList + "...";
    }
}
