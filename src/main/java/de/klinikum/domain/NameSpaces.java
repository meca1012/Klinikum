package de.klinikum.domain;

/**
 * 
 * NameSpace.java Purpose: Sesame URI Namespaces for identifying relationships betwween concepts
 * 
 * @author Spironto Team 1
 * @version 1.0 08/06/13
 */

public enum NameSpaces {

    // RDF Type for Patient
    // ------------------------RDF PATIENT + ADRESS_TYPE------------------------------
    PERSON_TYPE("http://spironto.de/spironto#person"),
    PATIENT_TYPE("http://spironto.de/spironto#patient"),
    ADDRESS_TYPE("http://spironto.de/spironto#address"),

    // RDF Predicates for Patient

    // RDF Type for PATEINT CORE DATA
    // Includes Street vNAME / nName / Nr / Zip / City / Country
    PATIENT_HAS_PATIENT_NUMBER("http://spironto.de/spironto#has-patient-number"),
    PATIENT_HAS_FIRST_NAME("http://spironto.de/spironto#has-first-name"),
    PATIENT_HAS_LAST_NAME("http://spironto.de/spironto#has-last-name"),
    PATIENT_HAS_DATE_OF_BIRTH("http://spironto.de/spironto#has-date-of-birth"),
    PATIENT_HAS_ADDRESS("http://spironto.de/spironto#has-address"),
    PATIENT_HAS_NOTE("http://spironto.de/spironto#has-note"),
    PATIENT_POINTS_TO_CONCEPT("http://spironto.de/spironto#patient-points-to-concept"),
    ADDRESS_HAS_ADDRESS_STREET("http://spironto.de/spironto#has-address-street"),
    ADDRESS_HAS_ADDRESS_STREET_NUMBER("http://spironto.de/spironto#has-address-street-number"),
    ADDRESS_HAS_ADDRESS_ZIP("http://spironto.de/spironto#has-address-zip"),
    ADDRESS_HAS_ADDRESS_CITY("http://spironto.de/spironto#has-address-city"),
    ADDRESS_IN_COUNTRY("http://spironto.de/spironto#has-laddress-country"),
    ADDRESS_HAS_PHONENUMBER("http://spironto.de/spironto#has-phonenumber"),

    // ------------------------END RDF PATIENT CORE DATA------------------------------

    // ------------------------RDF THEMA CORE DATA------------------------------
    // RDF Type for Note
    // Includes Text , Date , Themes
    NOTE_TYPE("http://spironto.de/spironto#note"),
    NOTE_HAS_TEXT("http://spironto.de/spironto#note-has-text"),
    NOTE_HAS_DATE("http://spironto.de/spironto#note-has-date"),
    NOTE_HAS_TITLE("http://spironto.de/spironto#note-has-title"),
    NOTE_HAS_PRIORITY("http://spironto.de/spironto#note-has-priority"),
    NOTE_POINTS_TO_CONCEPT("http://spironto.de/spironto#note-points-to-concept"),

    // RDF Type for HeadTopic
    // Used for SearchQuery like get everything linked to religion
    ONTOLOGIE_CONCEPT_TYPE("http://spironto.de/spironto#concept"),
    ONTOLOGIE_CONCEPT_HAS_LABEL("http://spironto.de/spironto#concept-has-label"),
    ONTOLOGIE_CONCEPT_LINKED_TO("http://spironto.de/spironto#concept-linked-to"),
    LINK_HAS_LABEL("http://spironto.de/spironto#link-has-label"),

    // RDF Type for Ontology
    PATIENT_HAS_CONCEPT("http://spironto.de/spironto#patient-has-concept"),

    // Defined a Tap-Element in the GUI
    // Predefined Topic
    GUI_TAB_TYPE("http://spironto.de/spironto#gui-tab-type"),
    GUI_TAB_TYPE_HAS_VALUE("http://spironto.de/spironto#gui-tab-type-has-value"),

    // GEDANKEN MACHEN
    // Predefined Topic
    RELIGION_TYPE("http://spironto.de/spironto#head-topic"),

    // GEDANKEN MACHEN
    // PalliativeTeam
    // Doctors, Nurses, Social worker
    PTEAM_TYPE("http://sprironto.de//spironto#pTeam"),

    // /Technical
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
