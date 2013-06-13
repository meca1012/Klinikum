package de.klinikum.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 
 * DateUtil.java
 * Purpose: Reading and Formatting Dateobject 
 * For Sesamestorage and UI delivery
 *          
 * @author  Spironto Team 1
 * @version 1.0 08/06/13
 */

public class DateUtil {

    public static Date getBirthDateFromString(String sDate) {
        
        String subString = sDate.substring(sDate.length() - 3, sDate.length());
        subString = subString.replaceFirst(":", "");
        sDate = sDate.substring(0, sDate.length() - 3);
        sDate = sDate.concat(subString);
       
        String dateFormatOld = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
        SimpleDateFormat sdfOld = new SimpleDateFormat(dateFormatOld, new Locale("en_US"));
        sdfOld.setTimeZone(TimeZone.getTimeZone("CET"));
        Date oldDate = null;
        try {
            oldDate = sdfOld.parse(sDate);
        }
        catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       
        String dateFormat = "EEE MMM dd HH:mm:ss z yyyy";

        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, new Locale("en_US"));
        sdf.setTimeZone(TimeZone.getTimeZone("CET"));
        Date newDate = null;
        try {
            newDate = sdf.parse(oldDate.toString());
        }
        catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return newDate;
    }
    
    public static Date getCurrentSysTime() {
        String dateFormat = "EEE MMM dd HH:mm:ss z yyyy";
        
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, new Locale("en_US"));
        sdf.setTimeZone(TimeZone.getTimeZone("CET"));
        
        Date newDate = new Date(System.currentTimeMillis());
        try {
            newDate = sdf.parse(newDate.toString());
        }
        catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return newDate;
    }   
}
