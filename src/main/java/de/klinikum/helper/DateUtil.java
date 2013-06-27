package de.klinikum.helper;

import org.joda.time.DateTime;

/**
 * 
 * DateUtil.java
 * Purpose: Reading and Formatting Dateobject 
 * For Sesamestorage and UI delivery
 *          
 * @author  Constantin Treiber, Andreas Schillinger
 * @version 1.0 08/06/13
 */

public class DateUtil {

    /**
     * Parses a string to yoda DateTime.
     * @param sDate
     * @return
     */
    public static DateTime getDateTimeFromString(String sDate) {
        
        return new DateTime(sDate);
    }
}