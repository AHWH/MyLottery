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

public class ResultMultipleNumberViewHolder extends RecyclerView.ViewHolder {
    private TextView num;

    public ResultMultipleNumberViewHolder(View itemView) {
        super(itemView);
        num = (TextView) itemView.findViewById(R.id.multipleNos_row_no);
    }

    public TextView getNum() {
        return num;
    }

    public void setNum(TextView num) {
        this.num = num;
    }
}
