package de.klinikum.domain;

public enum NameSpaces {
	
	PATIENT_TYPE("http://spironto.de/spironto#patient"),
		
	//Stammdaten
	MASTER_DATA("http://spironto.de/spironto#is-masterData"),
	PATIENT_NAME("http://spironto.de/spironto#has-name"),
	//Hauptthema
	MAIN_TOPIC("http://spironto.de/spironto#is-mainTopic"),
	
	//vordefinierte Themen
	
	//Technisch
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
