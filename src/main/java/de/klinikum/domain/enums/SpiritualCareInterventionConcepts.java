package de.klinikum.domain.enums;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.klinikum.helper.PropertyLoader;

public enum SpiritualCareInterventionConcepts {

    SILENCE("meaningful silence"), 
    SPIRITUAL_ATTENDANCE("spirituelle attendance"), 
    COUNSEL("Counsel");

    private final String value;
    private static final Logger LOGGER = LoggerFactory.getLogger(SpiritualityConcepts.class);

    SpiritualCareInterventionConcepts(String value) {
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
        Properties propFile = propertyLoader.load("concept.properties");
        return propFile.getProperty(this.name().toString());
    }

}
