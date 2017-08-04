/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.view.result;

import android.support.v7.widget.Toolbar;
import android.view.View;

import sg.reddotdev.sharkfin.view.ViewMVP;

/**
 * Created by weihong on 4/8/17.
 */

public interface ResultViewMVP extends ViewMVP {
    interface ResultViewMVPListener {

    }

    Toolbar getToolbar();

    void registerListener(ResultViewMVPListener listener);
    void unregisterListener();
}
