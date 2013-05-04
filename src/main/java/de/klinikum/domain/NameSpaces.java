package de.klinikum.domain;

public enum NameSpaces {
	
	PATIENT_NS("http://spironto.de/spironto/patient#"),
	
	PATIENT_NAME("http://spironto.de/spironto#has-name"),
	
	DATE_ADDED("http://spironto.de/spironto/#dateAdded"),
	
	DATE_LAST_MODIFIED("http://spironto.de/spironto/#dateLastModified");

	private final String uri;
	
	NameSpaces(String uri) {
		this.uri = uri;
	}
	
	public String toString() {
		return uri;
	}
}
