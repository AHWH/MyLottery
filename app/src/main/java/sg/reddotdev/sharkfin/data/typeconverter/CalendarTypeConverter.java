/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.data.typeconverter;

import android.util.Log;

import com.raizlabs.android.dbflow.converter.TypeConverter;

import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;

import sg.reddotdev.sharkfin.util.CalendarConverter;
import sg.reddotdev.sharkfin.util.constants.AppLocale;

@com.raizlabs.android.dbflow.annotation.TypeConverter
public class CalendarTypeConverter extends TypeConverter<String, ZonedDateTime> {
    @Override
    public String getDBValue(ZonedDateTime model) {
        return model.getDayOfMonth() + " "
                + CalendarConverter.monthNoToFullConvert(model.getMonth().getValue()) + " "
                + model.getYear() + ", "
                + CalendarConverter.dayNoToFullConvert(model.getDayOfWeek().getValue()) + " ("
                + model.getZone().getId() + ")";
    }

    @Override
    public ZonedDateTime getModelValue(String data) {
        String[] dateArr = data.split("\\s|,|\\(|\\)");
        return ZonedDateTime.of(Integer.parseInt(dateArr[2]), CalendarConverter.monthFullToNoConvert(dateArr[1]), Integer.parseInt(dateArr[0]), 18, 30, 0, 0, AppLocale.gmt8Zone);
    }
}

