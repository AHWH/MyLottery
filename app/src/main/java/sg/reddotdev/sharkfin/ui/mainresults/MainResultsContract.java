/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.ui.mainresults;

import sg.reddotdev.sharkfin.util.ui.BasePresenter;
import sg.reddotdev.sharkfin.util.ui.BaseView;

public interface MainResultsContract {
    interface View extends BaseView {
        void updateTheme(int lotteryType);

        void showNextDraw(String date, String dayTime);
    }

    interface Presenter extends BasePresenter<View> {
        void setView(View v);
        void resetView();

        void updateTheme(int lotteryType);
        void updateNextDraw(String[] dateTimeArr);
    }
}
