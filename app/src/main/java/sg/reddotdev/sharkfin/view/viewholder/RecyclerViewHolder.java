/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.view.viewholder;

import sg.reddotdev.sharkfin.data.model.LotteryResult;

public interface RecyclerViewHolder {
    interface OnClickListener {
        void onResultClick(LotteryResult lotteryResult);
    }

    void registerListener(OnClickListener listener);
    void unregisterListener();
}
