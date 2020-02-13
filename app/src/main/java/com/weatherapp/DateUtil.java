package com.weatherapp;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    /*
     * Convert timestamp into date form
     * timeStamp: 1577212200000   timetodate :: 12/25/2019 00:00:00
     * */
    public static String timeStamptodate(long timeStampValue, String dateformat) {
        Timestamp ts = new Timestamp(timeStampValue*1000);
        Date date = ts;
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateformat);
        String str = dateFormat.format(date);

        return str;
    }


}
