package de.klinikum.domain;

public enum NameSpaces {
	
	
	
	// RDF Type for Patient
	// ------------------------RDF PATIENT CORE DATA------------------------------
	PATIENT_TYPE("http://spironto.de/spironto#patient"),
	
	// RDF Predicates for Patient
	

	// RDF Type for PATEINT CORE DATA
	// Includes Street vNAME / nName  / Nr / Zip / City / Country
	COREDATA_TYPE("http://spironto.de/spironto#patientCoredataType"),
	COREDATA("http://spironto.de/spironto#patientCoredata"),
	COREDATA_VNAME("http://spironto.de/spironto#has-vname"),
	COREDATA_NNAME("http://spironto.de/spironto#has-nname"),
	COREDATA_DAYOFBIRTH("http://spironto.de/spironto#is-born-on"),
	COREDATA_ADDRESS("http://spironto.de/spironto#lives-in"),
	COREDATA_ADRESS_STREET("http://spironto.de/spironto#address-street"),
	COREDATA_ADRESS_ZIP("http://spironto.de/spironto#address-zip"),
	COREDATA_ADRESS_CITY("http://spironto.de/spironto#address-city"),
	COREDATA_ADRESS_Country("http://spironto.de/spironto#laddress-country"),	
	COREDATA_PHONE("http://spironto.de/spironto#has-phonenumber"),
	
	// ------------------------END RDF PATIENT CORE DATA------------------------------
	
	
	// ------------------------RDF THEMA CORE DATA------------------------------
	// RDF Type for TextBlock 
	// Includes Text , Date , Themes
	TEXTBLOCK_TYPE("http://spironto.de/spironto#textBlock"),
	TEXTBLOCK_TEXT("http://spironto.de/spironto#recorded-text"),
	TEXTBLOCK_DATE("http://spironto.de/spironto#recorded-date"),
	TEXTBLOCK_HEADTOPIC("http://spironto.de/spironto#head-topic"),
	TEXTBLOCK_SUBTOPIC("http://spironto.de/spironto#head-topic"),
		
	// RDF Type for HeadTopic
	// Used for SearchQuery like get everything linked to religion
	HEADTOPIC("http://spironto.de/spironto#mainTopic"),

	// RDF Type for SubTopyc 
	// Used as Subtopic 
	SUBTOPIC("http://spironto.de/spironto#subTopic"),

	
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
