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

public class MonthSectionViewHolder extends RecyclerView.ViewHolder {
    private TextView sectionMonth;

    public MonthSectionViewHolder(View itemView) {
        super(itemView);
        sectionMonth = (TextView)itemView.findViewById(R.id.section_month_year);
    }

    public TextView getSectionMonth() {
        return sectionMonth;
    }

    public void setSectionMonth(TextView sectionMonth) {
        this.sectionMonth = sectionMonth;
    }
}
