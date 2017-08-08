/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.data.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.DecimalFormat;
import java.util.List;

import sg.reddotdev.sharkfin.R;
import sg.reddotdev.sharkfin.view.viewholder.ResultMultipleNumberViewHolder;

public class ResultMultipleNumberRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Integer> lotteryNumbers;

    public ResultMultipleNumberRecyclerAdapter(List<Integer> lotteryNumbers) {
        this.lotteryNumbers = lotteryNumbers;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_multiplenos_row_recycler, parent, false);
        return new ResultMultipleNumberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ResultMultipleNumberViewHolder viewHolder = (ResultMultipleNumberViewHolder) holder;
        int num = lotteryNumbers.get(position);
        DecimalFormat df;
        if(num - 99 < 0) {
            df = new DecimalFormat("00");
        } else if(num - 9999 < 0) {
            df = new DecimalFormat("0000");
        } else {
            df = new DecimalFormat("000000");
        }

        viewHolder.getNum().setText(df.format(num));
    }

    @Override
    public int getItemCount() {
        return lotteryNumbers.size();
    }
}
