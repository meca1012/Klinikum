package de.klinikum.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {

    public static Date getBirthDateFromString(String sDate) {
        String dateFormat = "EEE MMM dd HH:mm:ss z yyyy";

        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, new Locale("en_US"));
        sdf.setTimeZone(TimeZone.getTimeZone("CET"));
        Date newDate = null;
        try {
            newDate = sdf.parse(sDate);
        }
        catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return newDate;
    }
}
