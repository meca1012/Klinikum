package de.klinikum.domain.enums;

import java.util.Properties;

import de.klinikum.helper.PropertyLoader;

public enum TabConcepts {
	
    COURSEOFDISEASE("Course Of Disease"),
	FAMILY ("Family"),
	SPIRITUALITY_RELIGION ("Spirituality / Religion"),
	ORIGIN ("Origin"),
	SPIRITUAL_CARE_INTERVENTION ("Spiritual care intervention");
	
    private final String value;

    TabConcepts(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        try {
            return this.getEnumValue();
        }
        catch (Exception e) {
            return this.value;
        }

    }

    private String getEnumValue() throws Exception {
        PropertyLoader propertyLoader = new PropertyLoader();
        Properties propFile = propertyLoader.load("concept.properties");
        return propFile.getProperty(this.name().toString());
    }

}
