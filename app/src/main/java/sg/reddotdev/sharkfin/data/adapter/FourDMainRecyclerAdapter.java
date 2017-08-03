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
import sg.reddotdev.sharkfin.data.model.impl.FourDLotteryResult;
import sg.reddotdev.sharkfin.util.CalendarConverter;
import sg.reddotdev.sharkfin.view.viewholder.NewLayoutViewHolder;
import sg.reddotdev.sharkfin.view.viewholder.NormalLayoutViewHolder;

public class FourDMainRecyclerAdapter extends BaseMainRecyclerAdapter {
    private String LOGTAG = getClass().getSimpleName();

    public FourDMainRecyclerAdapter(List<Object> fourDLotteryResultList) {
        super(fourDLotteryResultList);
    }

    protected void configureNewResultViewHolder(NewLayoutViewHolder holder, int position, List<Object> fourDLotteryResultList) {
        FourDLotteryResult fourDLotteryResult = (FourDLotteryResult) fourDLotteryResultList.get(position);
        ZonedDateTime lotteryDate = fourDLotteryResult.getDate();
        holder.getDate().setText(Integer.toString(lotteryDate.getDayOfMonth()));
        holder.getDay().setText(CalendarConverter.dayNoTo3CharConvert(lotteryDate.getDayOfWeek().getValue()));

        String lotteryID = getContext().getText(R.string.main_drawNo) + " " + Integer.toString(fourDLotteryResult.getLotteryID());
        holder.getDrawNo().setText(lotteryID);
        String firstPrize = getContext().getText(R.string.fourd_main_firstPrize) + " " + Integer.toString(fourDLotteryResult.getFirstPrize());
        holder.getFirstTextView().setText(firstPrize);
        String secondPrize = getContext().getText(R.string.fourd_main_secondPrize) + " " + Integer.toString(fourDLotteryResult.getSecondPrize());
        holder.getSecondTextView().setText(secondPrize);
        String thirdPrize = getContext().getText(R.string.fourd_main_thirdPrize) + " " + Integer.toString(fourDLotteryResult.getThirdPrize());
        holder.getThirdTextView().setText(thirdPrize);
    }

    protected void configureNormalResultViewHolder(NormalLayoutViewHolder holder, int position, List<Object> fourDLotteryResultList) {
        FourDLotteryResult fourDLotteryResult = (FourDLotteryResult) fourDLotteryResultList.get(position);
        ZonedDateTime lotteryDate = fourDLotteryResult.getDate();
        holder.getDate().setText(Integer.toString(lotteryDate.getDayOfMonth()));
        holder.getDay().setText(CalendarConverter.dayNoTo3CharConvert(lotteryDate.getDayOfWeek().getValue()));

        String lotteryID = getContext().getText(R.string.main_drawNo) + " " + Integer.toString(fourDLotteryResult.getLotteryID());
        holder.getDrawNo().setText(lotteryID);
        String firstPrize = getContext().getText(R.string.fourd_main_firstPrize) + " " + Integer.toString(fourDLotteryResult.getFirstPrize());
        holder.getFirstTextView().setText(firstPrize);
    }
}
