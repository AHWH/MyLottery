/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.util.dagger;

import javax.inject.Singleton;

import dagger.Component;
import sg.reddotdev.sharkfin.util.dagger.module.AppModule;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

}
