package de.klinikum.persistence;

/**
 * 
 * NameSpace.java Purpose: Sesame URI Namespaces for identifying relationships betwween concepts
 * 
 * @author Andreas Schillinger, Constantin Treiber, Carsten Meiser
 * @version 1.0 08/06/13
 */

public enum NameSpaces {

    /*
     * RDF-Types
     */
    PERSON_TYPE("http://spironto.de/spironto#person"),
    PATIENT_TYPE("http://spironto.de/spironto#patient"),
    ADDRESS_TYPE("http://spironto.de/spironto#address"),
    NOTE_TYPE("http://spironto.de/spironto#note"),
    CONCEPT_TYPE("http://spironto.de/spironto#concept"),
    GUI_TAB_TYPE("http://spironto.de/spironto#gui-tab-type"),

    /*
     * RDF-Predicates for patient
     */
    PATIENT_HAS_PATIENT_NUMBER("http://spironto.de/spironto#has-patient-number"),
    PATIENT_HAS_FIRST_NAME("http://spironto.de/spironto#has-first-name"),
    PATIENT_HAS_LAST_NAME("http://spironto.de/spironto#has-last-name"),
    PATIENT_HAS_DATE_OF_BIRTH("http://spironto.de/spironto#has-date-of-birth"),
    PATIENT_HAS_ADDRESS("http://spironto.de/spironto#has-address"),
    PATIENT_HAS_NOTE("http://spironto.de/spironto#has-note"),
    PATIENT_HAS_CONCEPT("http://spironto.de/spironto#patient-has-concept"),
    PATIENT_POINTS_TO_CONCEPT("http://spironto.de/spironto#patient-points-to-concept"),
    
    /*
     * RDF-Predicates for address
     */
    ADDRESS_HAS_ADDRESS_STREET("http://spironto.de/spironto#has-address-street"),
    ADDRESS_HAS_ADDRESS_STREET_NUMBER("http://spironto.de/spironto#has-address-street-number"),
    ADDRESS_HAS_ADDRESS_ZIP("http://spironto.de/spironto#has-address-zip"),
    ADDRESS_HAS_ADDRESS_CITY("http://spironto.de/spironto#has-address-city"),
    ADDRESS_IN_COUNTRY("http://spironto.de/spironto#has-address-country"),
    ADDRESS_HAS_PHONENUMBER("http://spironto.de/spironto#has-phonenumber"),
    
    /*
     * RDF-Predicates for note
     */
    NOTE_HAS_TEXT("http://spironto.de/spironto#note-has-text"),
    NOTE_HAS_DATE("http://spironto.de/spironto#note-has-date"),
    NOTE_HAS_TITLE("http://spironto.de/spironto#note-has-title"),
    NOTE_HAS_PRIORITY("http://spironto.de/spironto#note-has-priority"),
    NOTE_POINTS_TO_CONCEPT("http://spironto.de/spironto#note-points-to-concept"),

    /*
     * RDF-Predicates for concept
     * Used for SearchQuery like get everything linked to religion
     */
    CONCEPT_HAS_LABEL("http://spironto.de/spironto#concept-has-label"),
    CONCEPT_LINKED_TO("http://spironto.de/spironto#concept-linked-to"),
    CONCEPT_IS_EDITABLE("http://spironto.de/spironto#concept-is-editable"),

    /*
     * Technical
     */
    // ID for the generation of unique URIs
    LAST_ID("http://spironto.de/spironto#lastID"),
    TYPE_DATASTORE("http://spironto.de/spironto#Datastore"),
    DATE_ADDED("http://spironto.de/spironto#dateAdded"),
    DATE_LAST_MODIFIED("http://spironto.de/spironto#dateLastModified");

    private final String uri;

    NameSpaces(String uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return this.uri;
    }
    
}
