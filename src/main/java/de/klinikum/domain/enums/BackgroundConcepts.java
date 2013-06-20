package de.klinikum.domain.enums;

import java.util.Properties;

import de.klinikum.helper.PropertyLoader;

public enum BackgroundConcepts {

    ATHOME("at home"), MIGRATIONBACKGROUND("migration background");

    private final String value;

    BackgroundConcepts(String value) {
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
