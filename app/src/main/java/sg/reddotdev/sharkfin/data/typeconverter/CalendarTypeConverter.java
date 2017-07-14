/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.data.typeconverter;

import com.raizlabs.android.dbflow.converter.TypeConverter;

import java.util.Calendar;
import java.util.GregorianCalendar;

@com.raizlabs.android.dbflow.annotation.TypeConverter
public class CalendarTypeConverter extends TypeConverter<String, Calendar> {
    @Override
    public String getDBValue(Calendar model) {
        return model.DAY_OF_MONTH + " " + model.MONTH + " " + model.YEAR;
    }

    @Override
    public Calendar getModelValue(String data) {
        String[] dateArr = data.split("\\s");
        Calendar returnCalendar = new GregorianCalendar(Integer.parseInt(dateArr[0]), Integer.parseInt(dateArr[1]), Integer.parseInt(dateArr[2]));
        return returnCalendar;
    }
}

