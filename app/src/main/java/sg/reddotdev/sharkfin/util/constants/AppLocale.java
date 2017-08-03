/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.util.constants;

import org.threeten.bp.ZoneId;

import java.text.DecimalFormat;

public class AppLocale {
    private AppLocale() {}

    public static final ZoneId gmt8Zone = ZoneId.of("GMT+8");
    public static final DecimalFormat decimalFormat = new DecimalFormat("#,##0");
}
