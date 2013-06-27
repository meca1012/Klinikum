package de.klinikum.helper;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/** Date Adapter for created date of concept to marshal/unmarshal XML date.
 *  Uses yoda DateTime.
 *  Returned format dd-MM-yyyy HH:mm:ss
 * 
 * @author Constantin Treiber, Andreas Schillinger
 *
 */
public class XmlDateTimeAdapter extends XmlAdapter<String, DateTime> {
     
    public DateTime unmarshal(String v) throws Exception {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss");
        DateTime dt = formatter.parseDateTime(v);
        return dt;
    }
 
    public String marshal(DateTime v) throws Exception {
        String dateFormat = "dd-MM-yyyy HH:mm:ss";
        DateTimeFormatter dtf = ISODateTimeFormat.dateTime();
        LocalDateTime parsedDate = dtf.parseLocalDateTime(v.toString());
        String dateWithCustomFormat = parsedDate.toString(DateTimeFormat.forPattern(dateFormat));
        return dateWithCustomFormat;
    }
}
