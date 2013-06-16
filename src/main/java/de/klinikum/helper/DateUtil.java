package de.klinikum.helper;

import org.joda.time.DateTime;

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

    public static DateTime getDateTimeFromString(String sDate) {
        
        return new DateTime(sDate);
    }
}