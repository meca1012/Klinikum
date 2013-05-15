package de.klinikum.domain;

public enum NameSpaces {
	
	
	
	// RDF Type for Patient
	// ------------------------RDF PATIENT + ADRESS_TYPE------------------------------
	PERSON_TYPE("http://spironto.de/spironto#patient"),
	ADRESS_TYPE("http://spironto.de/spironto#address"),
	
	// RDF Predicates for Patient
	

	// RDF Type for PATEINT CORE DATA
	// Includes Street vNAME / nName  / Nr / Zip / City / Country
	PERSON_HAS_VNAME("http://spironto.de/spironto#has-vname"),
	PERSON_HAS_NNAME("http://spironto.de/spironto#has-nname"),
	PERSON_HAS_DAY_OF_BIRTH("http://spironto.de/spironto#is-born-on"),
	PERSON_HAS_ADDRESS("http://spironto.de/spironto#has-address"),
	PERSON_HAS_TEXTBLOCK("http://spironto.de/spironto#has-textblock"),
	PERSON_POINTS_TO_CONCEPT("http://spironto.de/spironto#person-points-to-concept"),
	ADRESS_HAS_ADDRESS_STREET("http://spironto.de/spironto#has-address-street"),
	ADRESS_HAS_ADDRESS_ZIP("http://spironto.de/spironto#has-address-zip"),
	ADRESS_HAS_ADDRESS_CITY("http://spironto.de/spironto#has-address-city"),
	ADDRESS_IN_COUNTRY("http://spironto.de/spironto#has-laddress-country"),	
	ADRESS_HAS_PHONENUMBER("http://spironto.de/spironto#has-phonenumber"),
	
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



	// Predefined Topic
	RELIGION_TYPE("http://spironto.de/spironto#head-topic"),
	
	// PalliativeTeam 
	// Doctors, Nurses, Social worker
	PTEAM_TYPE("http://sprironto.de//spironto#pTeam"),
	
	
	
	
	///Technisch
	//ID zur generierung von einzigartigen URI's
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
