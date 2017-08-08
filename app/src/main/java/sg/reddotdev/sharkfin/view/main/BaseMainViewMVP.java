/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.view.main;

import sg.reddotdev.sharkfin.view.ViewMVP;

public interface BaseMainViewMVP extends ViewMVP {
    interface BaseMainViewMVPListener {
        void onNavigationSelected(int itemID);
        void onBottomNavigationSelected(int itemID);
    }

    void registerListener(BaseMainViewMVPListener listener);
    void unregisterListener();
}
