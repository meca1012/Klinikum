package de.klinikum.domain;

public enum NameSpaces {
	
	
	
	// RDF Type for Patient
	// ------------------------RDF PATIENT + ADRESS_TYPE------------------------------
	PERSON_TYPE("http://spironto.de/spironto#person"),
	PATIENT_TYPE("http://spironto.de/spironto#patient"),
	ADDRESS_TYPE("http://spironto.de/spironto#address"),
	
	// RDF Predicates for Patient
	

	// RDF Type for PATEINT CORE DATA
	// Includes Street vNAME / nName  / Nr / Zip / City / Country
	PATIENT_HAS_PATIENT_NUMBER("http://spironto.de/spironto#has-patient-number"),
	PATIENT_HAS_FIRST_NAME("http://spironto.de/spironto#has-first-name"),
	PATIENT_HAS_LAST_NAME("http://spironto.de/spironto#has-last-name"),
	PATIENT_HAS_DATE_OF_BIRTH("http://spironto.de/spironto#has-date-of-birth"),
	PATIENT_HAS_ADDRESS("http://spironto.de/spironto#has-address"),
	PATIENT_HAS_TEXTBLOCK("http://spironto.de/spironto#has-textblock"),
	PATIENT_POINTS_TO_CONCEPT("http://spironto.de/spironto#patient-points-to-concept"),
	ADDRESS_HAS_ADDRESS_STREET("http://spironto.de/spironto#has-address-street"),
	ADDRESS_HAS_ADDRESS_ZIP("http://spironto.de/spironto#has-address-zip"),
	ADDRESS_HAS_ADDRESS_CITY("http://spironto.de/spironto#has-address-city"),
	ADDRESS_IN_COUNTRY("http://spironto.de/spironto#has-laddress-country"),	
	ADDRESS_HAS_PHONENUMBER("http://spironto.de/spironto#has-phonenumber"),
	
	// ------------------------END RDF PATIENT CORE DATA------------------------------
	
	
	// ------------------------RDF THEMA CORE DATA------------------------------
	// RDF Type for TextBlock 
	// Includes Text , Date , Themes
	TEXTBLOCK_TYPE("http://spironto.de/spironto#textBlock"),
	TEXTBLOCK_HAS_TEXT("http://spironto.de/spironto#textblock-has-text"),
	TEXTBLOCK_HAS_DATE("http://spironto.de/spironto#textblock-has-date"),
	TEXTBLOCK_POINTS_TO_CONCEPT("http://spironto.de/spironto#textblock-points-to-concept"),
		
	// RDF Type for HeadTopic
	// Used for SearchQuery like get everything linked to religion
	ONTOLOGIE_CONCEPT_TYPE("http://spironto.de/spironto#concept"),
	ONTOLOGIE_CONCEPT_HAS_LABEL("http://spironto.de/spironto#concept-has-label"),

	// Defined a Tap-Element in the GUI
	// Predefined Topic
	GUI_TAB_TYPE("http://spironto.de/spironto#gui-tab-type"),
	GUI_TAB_TYPE_HAS_VALUE("http://spironto.de/spironto#gui-tab-type-has-value"),

	
    //GEDANKEN MACHEN
	// Predefined Topic
	RELIGION_TYPE("http://spironto.de/spironto#head-topic"),

	//GEDANKEN MACHEN
	// PalliativeTeam 
	// Doctors, Nurses, Social worker
	PTEAM_TYPE("http://sprironto.de//spironto#pTeam"),
	
	
	
	
	///Technical
	//ID for the generation of unique URIs
	LAST_ID("http://spironto.de/spironto#lastID"),
	TYPE_DATASTORE("http://spironto.de/spironto#Datastore"),
	DATE_ADDED("http://spironto.de/spironto#dateAdded"),	
	DATE_LAST_MODIFIED("http://spironto.de/spironto#dateLastModified");
	

	private final String uri;
	
	NameSpaces(String uri) {
		this.uri = uri;
	}
	
	public String toString() {
		return uri;
	}
}
