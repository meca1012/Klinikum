package main.java.de.klinikum.appserver.domain;

public enum RDF {

	TYPE("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
	
	private final String uri;
	
	RDF(String uri) {
		this.uri = uri;
	}
	
	public String toString() {
		return uri;
	}
}
