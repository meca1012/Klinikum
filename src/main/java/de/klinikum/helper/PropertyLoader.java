package de.klinikum.helper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;

import javax.ws.rs.core.Response;

/**
 * 
 * PropertyLoader.java Purpose: Reads Configuration -File for getting Sesamestorage Configuration
 * 
 * @author Spironto Team 1
 * @version 1.0 08/06/13
 */

public class PropertyLoader {

    private static final String propPackage = "/de/klinikum/properties/";
    private Properties props;
    private InputStream in;
    private OutputStream out;
    private final String sesameConfigFile = "sesame.properties";
    private final String conceptConfigFile = "Concept.properties";
    private final String sesameLanguageSetting = "spironto.language";
    private final String english = "en";
    private final String german = "de";

    /**
     * Constructor and Properties() initialization
     */
    public PropertyLoader() {
        this.props = new Properties();
    }

    public Properties load(String propsName) throws Exception {
        this.props = new Properties();
        this.in = getClass().getResourceAsStream(propPackage + propsName);
        this.props.load(in);
        return props;
    }
    
    /**
     * Loads the Property-Filename depending on configuration
     * @return String with filename
     */
    public String getLanguageFileName() {
        String languange = "";

        try {
            this.in = getClass().getResourceAsStream(propPackage + sesameConfigFile);
            this.props.load(in);
            languange = this.props.getProperty("spironto.language");
        }
        catch (IOException e) {
            languange = "en";
        }

        if (languange.equals(english)) {
            return english + conceptConfigFile;
        }
        if (languange.equals(german)) {
            return german + conceptConfigFile;
        }
        return english + conceptConfigFile;
    }
    /**
     * Changes LanguageSettings via Rest
     * @param language Language Short String
     * @return Boolean returns true if language changed / false if not
     */
    public boolean changeLanguageSetting(String language)
    {
        if(language.equals(english) || language.equals(german))
        {
            try {
                this.in = getClass().getResourceAsStream(propPackage + sesameConfigFile);
                this.props.load(in);
                in.close();

                URL resourceUrl = getClass().getResource(propPackage + sesameConfigFile);
                File file = new File(resourceUrl.toURI());
                this.out = new FileOutputStream(file);
                
                props.setProperty(sesameLanguageSetting, language);
                props.store(out, null);
                out.close();
                return true;
             }
           catch(Exception e)
           {
               return false;
           }
        }
            
        return false;
        
    }
}
