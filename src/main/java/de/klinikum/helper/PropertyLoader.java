package de.klinikum.helper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

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
    private final String sesameConfigFile = "sesame.properties";
    private final String conceptConfigFile = "Concept.properties";
    private final String english = "en";
    private final String german = "de";

    public PropertyLoader() {
        this.props = new Properties();
    }

    public Properties load(String propsName) throws Exception {
        this.props = new Properties();
        this.in = getClass().getResourceAsStream(propPackage + propsName);
        this.props.load(in);
        return props;
    }

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

        if (languange == "en") {
            return english + conceptConfigFile;
        }
        if (languange == "de") {
            return german + conceptConfigFile;
        }
        return english + conceptConfigFile;

    }
}
