package de.klinikum.helper;
import java.net.URL;
import java.util.Properties;

 
public class PropertyLoader {
	
	private static final String propPackage = "/de/klinikum/properties/";
    private PropertyLoader() {  }
    /**
     * Load a properties file from the classpath
     * @param propsName
     * @return Properties
     * @throws Exception
     */
    public static Properties load(String propsName) throws Exception {
        Properties props = new Properties();
        URL url = Properties.class.getResource(propPackage + propsName);
        props.load(url.openStream());
        return props;

    }
}
