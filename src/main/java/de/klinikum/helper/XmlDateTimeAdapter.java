package de.klinikum.helper;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public class XmlDateTimeAdapter extends XmlAdapter<String, DateTime> {   
     
    public DateTime unmarshal(String v) throws Exception {
        return new DateTime(v);
    }
 
    public String marshal(DateTime v) throws Exception {
        String dateFormat = "yyyy-MM-dd HH:mm:ss";
        DateTimeFormatter dtf = ISODateTimeFormat.dateTime();
        LocalDateTime parsedDate = dtf.parseLocalDateTime(v.toString());
        String dateWithCustomFormat = parsedDate.toString(DateTimeFormat.forPattern(dateFormat));
        return dateWithCustomFormat;
    }
}
