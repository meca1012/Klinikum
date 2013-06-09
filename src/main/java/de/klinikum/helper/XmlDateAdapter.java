package de.klinikum.helper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * 
 * XmlDateAdapter.java
 * Purpose: Converting Dateformat to XML parsable Format via annotation
 *          
 * @author  Spironto Team 1
 * @version 1.0 08/06/13
 */

public class XmlDateAdapter extends XmlAdapter<String, Date> {
	private final DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()); 
	
	@Override
	public Date unmarshal(String date) throws ParseException {
		return formatter.parse(date);
	}
	
	@Override
	public String marshal(Date date)  {
		return formatter.format(date);
	}
}
