/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.data.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import sg.reddotdev.sharkfin.R;
import sg.reddotdev.sharkfin.data.model.LotteryResult;
import sg.reddotdev.sharkfin.util.CalendarConverter;
import sg.reddotdev.sharkfin.view.viewholder.FourDNewLayoutViewHolder;
import sg.reddotdev.sharkfin.view.viewholder.FourDNormalLayoutViewHolder;
import sg.reddotdev.sharkfin.view.viewholder.MonthSectionViewHolder;
import sg.reddotdev.sharkfin.view.viewholder.NewLayoutViewHolder;
import sg.reddotdev.sharkfin.view.viewholder.NormalLayoutViewHolder;
import sg.reddotdev.sharkfin.view.viewholder.RecyclerViewHolder;

public abstract class BaseMainRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements RecyclerViewHolder {
    private RecyclerViewHolder.OnClickListener listener;

    private String LOGTAG = getClass().getSimpleName();
    private List<Object> lotteryResultList;
    private Context context;

    private final int HEADER = 0;
    private final int RESULT = 1;
    private final int FIRSTRESULT = 2;

    public BaseMainRecyclerAdapter(List<Object> lotteryResultList) {
        this.lotteryResultList = lotteryResultList;
        Log.d(LOGTAG, lotteryResultList.size() + "/ " + this.lotteryResultList.size());
    }

    /*Assigns the item in the list the respective types*/
    @Override
    public int getItemViewType(int position) {
        if(lotteryResultList.get(position) instanceof String) {
            return HEADER;
        } else if(lotteryResultList.get(position) instanceof LotteryResult) {
            if(position == 1) {
                return FIRSTRESULT;
            } else {
                return RESULT;
            }
        }
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        final RecyclerView.ViewHolder viewHolder;
        View view;
        switch (viewType) {
            case HEADER:
                view = inflater.inflate(R.layout.section_month_recycler, parent, false);
                viewHolder = new MonthSectionViewHolder(view);
                break;
            case FIRSTRESULT:
                view = inflater.inflate(R.layout.results_recylcer_layout_new, parent, false);
                viewHolder = new FourDNewLayoutViewHolder(view);
                break;
            case RESULT:
                view = inflater.inflate(R.layout.results_recylcer_layout_normal, parent, false);
                viewHolder = new FourDNormalLayoutViewHolder(view);
                break;
            default:
                view = inflater.inflate(R.layout.results_recylcer_layout_normal, parent, false);
                viewHolder = new FourDNormalLayoutViewHolder(view);
        }
        if(viewType != HEADER) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*Pass to RootView*/
                    listener.onResultClick((LotteryResult) lotteryResultList.get(viewHolder.getAdapterPosition()));
                }
            });
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case HEADER:
                configureHeaderViewHolder((MonthSectionViewHolder) holder, position);
                break;
            case FIRSTRESULT:
                configureNewResultViewHolder((FourDNewLayoutViewHolder) holder, position, lotteryResultList);
                break;
            case RESULT:
                configureNormalResultViewHolder((FourDNormalLayoutViewHolder) holder, position, lotteryResultList);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return lotteryResultList.size();
    }

    private void configureHeaderViewHolder(MonthSectionViewHolder holder, int position) {
        String monthYearStr = (String) lotteryResultList.get(position);
        String[] monthYearStrArr = monthYearStr.split("\\s");
        holder.getSectionMonth().setText(CalendarConverter.monthNoToFullConvert(Integer.parseInt(monthYearStrArr[0])) + " " + monthYearStrArr[1]);
    }

    protected abstract void configureNormalResultViewHolder(NormalLayoutViewHolder holder, int position, List<Object> lotteryResultList);

    protected abstract void configureNewResultViewHolder(NewLayoutViewHolder holder, int position, List<Object> lotteryResultList);

    public Context getContext() {
        return context;
    }

    @Override
    public void registerListener(OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void unregisterListener() {
        listener = null;
    }
}
