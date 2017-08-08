/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.view.viewholder;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import sg.reddotdev.sharkfin.R;
import sg.reddotdev.sharkfin.util.constants.LottoConst;

public class NewLayoutViewHolder extends RecyclerView.ViewHolder {
    private View view;

    private TextView date;
    private TextView day;

    private TextView drawNo;
    private TextView firstTextView;
    private TextView secondTextView;
    private TextView thirdTextView;

    private ImageView arrow;

    public NewLayoutViewHolder(View itemView) {
        super(itemView);
        view = itemView;

        date = (TextView) itemView.findViewById(R.id.results_layout_new_dateday_date);
        day = (TextView) itemView.findViewById(R.id.results_layout_new_dateday_day);

        drawNo = (TextView) itemView.findViewById(R.id.results_layout_new_results_drawNo);
        firstTextView = (TextView) itemView.findViewById(R.id.results_layout_new_results_first);
        secondTextView = (TextView) itemView.findViewById(R.id.results_layout_new_results_second);
        thirdTextView = (TextView) itemView.findViewById(R.id.results_layout_new_results_third);

        arrow = (ImageView) itemView.findViewById(R.id.results_layout_new_arrow_arrow);
    }


    /*Getters and Setters*/
    public View getView() {
        return view;
    }

    public TextView getDate() {
        return date;
    }

    public void setDate(TextView date) {
        this.date = date;
    }

    public TextView getDay() {
        return day;
    }

    public void setDay(TextView day) {
        this.day = day;
    }

    public TextView getDrawNo() {
        return drawNo;
    }

    public void setDrawNo(TextView drawNo) {
        this.drawNo = drawNo;
    }

    public TextView getFirstTextView() {
        return firstTextView;
    }

    public void setFirstTextView(TextView firstTextView) {
        this.firstTextView = firstTextView;
    }

    public TextView getSecondTextView() {
        return secondTextView;
    }

    public void setSecondTextView(TextView secondTextView) {
        this.secondTextView = secondTextView;
    }

    public TextView getThirdTextView() {
        return thirdTextView;
    }

    public void setThirdTextView(TextView thirdTextView) {
        this.thirdTextView = thirdTextView;
    }

    public ImageView getArrow() {
        return arrow;
    }

    public void setLotteryTheme(int lotteryType) {
        int colour;
        switch (lotteryType) {
            case LottoConst.SGPOOLS_4D:
                colour = ContextCompat.getColor(getView().getContext(), R.color.fourDTheme_Primary);
                break;
            case LottoConst.SGPOOLS_TOTO:
                colour = ContextCompat.getColor(getView().getContext(), R.color.totoTheme_Primary);
                break;
            case LottoConst.SGPOOLS_SWEEP:
                colour = ContextCompat.getColor(getView().getContext(), R.color.bigSweepTheme_Primary);
                break;
            default:
                colour = ContextCompat.getColor(getView().getContext(), R.color.fourDTheme_Primary);
        }

        getDate().setTextColor(colour);
        getDrawNo().setTextColor(colour);
        getArrow().setColorFilter(colour);
    }
}
