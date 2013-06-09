package de.klinikum.helper;

import java.io.InputStream;
import java.util.Properties;

/**
 * 
 * PropertyLoader.java
 * Purpose: Reads Configuration -File for getting
 * Sesamestorage Configuration
 *          
 * @author  Spironto Team 1
 * @version 1.0 08/06/13
 */
 
public class PropertyLoader {
	
	private static final String propPackage = "/de/klinikum/properties/";
	private  Properties props;
	private InputStream in;
    
	public PropertyLoader() { 
    	this.props = new Properties();
    }
   
    public Properties load(String propsName) throws Exception {
        this.props = new Properties();
        this.in = getClass().getResourceAsStream(propPackage + propsName);
        this.props.load(in);
        return props;
    }
}
