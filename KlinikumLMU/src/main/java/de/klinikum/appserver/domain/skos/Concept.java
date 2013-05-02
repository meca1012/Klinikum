package main.java.de.klinikum.appserver.domain.skos;

import java.util.Set;

import main.java.de.klinikum.appserver.domain.SKOS;

public class Concept {
	
	private String uri;
	
	private VariedRelationStore<String> relations = new VariedRelationStore<String>();
	
	public Concept(String uri) {
		this.uri = uri;
	}
	
	public void initializeRelations(SKOS property, String uri) {
		relations.addValue(property, uri);
	}
	
	public String getUri() {
		return uri;
	}
	
	public Set<String> getConnected(SKOS property) {
		return relations.getValues(property);
	}
}
