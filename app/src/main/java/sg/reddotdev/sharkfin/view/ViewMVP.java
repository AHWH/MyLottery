/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.view;

import android.os.Bundle;
import android.view.View;

/**
 * Created by weihong on 15/7/17.
 */
/*Master blueprint of all View in MVP*/
/*Sub-interfaces of other Views should extend this interface as base*/
public interface ViewMVP {
    /*Gets the rootView of the current context*/
    public View getRootView();
    /*Gets the all the current UI elements in the current context*/
    public Bundle getViewState();
}
