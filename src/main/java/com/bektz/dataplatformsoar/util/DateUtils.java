package com.bektz.dataplatformsoar.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.bektz.dataplatformsoar.constants.Common.ILLEGAL_INSTANCE_MESSAGE;

public class DateUtils {
    private DateUtils() {
        throw new RuntimeException(ILLEGAL_INSTANCE_MESSAGE);
    }

    private static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public static String toString(Date date) {
        DateFormat df = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
        return df.format(date);
    }
}
