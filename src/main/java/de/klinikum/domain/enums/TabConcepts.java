package de.klinikum.domain.enums;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.klinikum.helper.PropertyLoader;

/**
 * 
 * @author Carsten Meiser
 *
 */
public enum TabConcepts {
	
    COURSEOFDISEASE("Course Of Disease"),
	FAMILY ("Family"),
	SPIRITUALITY_RELIGION ("Spirituality / Religion"),
	ORIGIN ("Origin"),
	SPIRITUAL_CARE_INTERVENTION ("Spiritual care intervention");
	
    private final String value;
    private String conceptFilePath;
    private static final Logger LOGGER = LoggerFactory.getLogger(SpiritualityConcepts.class);

    TabConcepts(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        try {
            return this.getEnumValue();
        }
        catch (Exception e) {
            LOGGER.warn("Enum property for: " + this.name() + "not found");
            return this.value;
        }

    }

    private String getEnumValue() throws Exception {
        PropertyLoader propertyLoader = new PropertyLoader();
        conceptFilePath = propertyLoader.getLanguageFileName();
        Properties propFile = propertyLoader.load(conceptFilePath);
        return propFile.getProperty(this.name().toString());
    }

}

