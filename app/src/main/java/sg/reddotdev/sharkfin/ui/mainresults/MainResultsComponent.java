/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.ui.mainresults;

import dagger.Component;
import sg.reddotdev.sharkfin.util.dagger.scope.ActivityScope;

@ActivityScope
@Component(modules = {MainPresenterModule.class, NetworkModule.class})
public interface MainResultsComponent {
    void inject(BaseMainActivity activity);
}

