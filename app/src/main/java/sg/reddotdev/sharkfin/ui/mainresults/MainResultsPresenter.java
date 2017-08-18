/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.ui.mainresults;

public class MainResultsPresenter implements MainResultsContract.Presenter {
    private MainResultsContract.View view;

    public MainResultsPresenter() {
    }

    public void setView(MainResultsContract.View view) {
        this.view = view;
    }

    public void resetView() {
        view = null;
    }

    public void updateTheme(int lotteryType) {
        view.updateTheme(lotteryType);
    }

    public void updateNextDraw(String[] dateTimeArr) {
        String date = dateTimeArr[0];
        String dayTime = dateTimeArr[1] + ", " + dateTimeArr[2];
        view.showNextDraw(date, dayTime);
    }
}
