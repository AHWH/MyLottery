/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.ui.mainresults.fragment;

import java.util.List;

import sg.reddotdev.sharkfin.util.ui.BasePresenter;
import sg.reddotdev.sharkfin.util.ui.BaseView;

public interface MainFragmentContract {
    interface View extends BaseView {
        void onSuccessRetrieveResult();
        void onFailureRetrieveResult();

        void onFailureSaveDB();
        void onSuccessRetrieveDB();
        void onFailureRetrieveDB();

        void onMergedList();
    }

    interface Presenter extends BasePresenter<View> {
        void onViewCreated(View view);
        void onViewDetach();

        void mergedList(List<Object> lotteryResults);
    }
}
