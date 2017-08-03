/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.view.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import sg.reddotdev.sharkfin.R;
import sg.reddotdev.sharkfin.data.model.LotteryResult;

public class NormalLayoutViewHolder extends RecyclerView.ViewHolder {
    private TextView date;
    private TextView day;

    private TextView drawNo;
    private TextView firstTextView;

    private RecyclerViewHolder.OnClickListener listener;

    public NormalLayoutViewHolder(View itemView) {
        super(itemView);
        date = (TextView) itemView.findViewById(R.id.results_layout_normal_dateday_date);
        day = (TextView) itemView.findViewById(R.id.results_layout_normal_dateday_day);

        drawNo = (TextView) itemView.findViewById(R.id.results_layout_normal_results_drawNo);
        firstTextView = (TextView) itemView.findViewById(R.id.results_layout_normal_results_first);
    }


    /*Getters and Setters*/
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
        this.firstTextView = this.firstTextView;
    }
}
