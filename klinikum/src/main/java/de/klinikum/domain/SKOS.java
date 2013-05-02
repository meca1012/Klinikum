package de.klinikum.domain;


public enum SKOS {

	RELATED("http://www.w3.org/2004/02/skos/core#related"),
	HAS_BROADER("http://www.w3.org/2004/02/skos/core#broader"), //this has broader concept x
	HAS_NARROWER("http://www.w3.org/2004/02/skos/core#narrower"), //this has narrower concept x
	
	/**
	 * This is the alternative Label
	 */
	ALT_LABEL("http://www.w3.org/2004/02/skos/core#altLabel"),	
	HIDDEN_LABEL("http://www.w3.org/2004/02/skos/core#hiddenLabel"),
	
	/**
	 * This is the preferred Label
	 */
	PREF_LABEL("http://www.w3.org/2004/02/skos/core#prefLabel"),
	/**
	 * This is the note
	 */
	NOTE("http://www.w3.org/2004/02/skos/core#note"),
	/**
	 * This is a enum which contains the URI for the Concept
	 * http://www.w3.org/2004/02/skos/core#Concept
	 */
	CONCEPT("http://www.w3.org/2004/02/skos/core#Concept");
	
	private final String uri;
	
	SKOS(String uri) {
		this.uri = uri;
	}
	
	/**
	 * returns the URI of the SKOS enum as a String
	 * @return String
	 */
	public String toString() {
		return uri;
	}
	
	/**
	 * Returns a SKOS enum for the given skosType as a String. 
	 * This SKOS contains the URI. And you can get it with the toString() 
	 * method.
	 * @param skosType
	 * @return SKOS
	 */
	public static SKOS getSKOS(String skosType) {
		for (SKOS skos: SKOS.values()) {
			if (skos.uri.equals(skosType)) return skos;
		}
		return null;
	}	
}
