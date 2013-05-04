package de.klinikum.domain.skos;

import java.util.HashSet;
import java.util.Set;

import de.klinikum.domain.SKOS;

public class Concept {
	
	private String uri;
	
	private VariedRelationStore<String> relations = new VariedRelationStore<String>();
	private VariedRelationStore<String> texts = new VariedRelationStore<String>();
	
	public Concept(String uri) {
		this.uri = uri;
	}
	
	public void initializeRelations(SKOS property, String uri) {
		relations.addValue(property, uri);
	}
	
	public void initializeTexts(SKOS property, String text) {
		texts.addValue(property, text);
	}
	
	public String getUri() {
		return uri;
	}
	
	public Set<String> getConnected(SKOS property) {
		return relations.getValues(property);
	}
	
	public Set<String> getTexts() {
		return texts.getValues();
	}
	
//	public Set<Concept> getConnectAsConcepts(SKOS property, Taxonomy taxonomy) {
//		Set<String> uris = getConnected(property);
//		Set<Concept> toReturn = new HashSet<Concept>();
//		for (String s : uris) {
//			toReturn.add(taxonomy.get(s));
//		}
//		return toReturn;
//	}
	
	public Set<String> getTexts(SKOS property) {		
		return (texts.getValues(property));		
	}
	
	public Set<String> getTexts(SKOS... properties) {
		Set<String> toReturn = new HashSet<String>();
		for(SKOS prop : properties) {
			toReturn.addAll(texts.getValues(prop));
		}
		return toReturn;
	}
	
	public boolean hasEqualValues(Concept other) {
		if (!getUri().equals(other.getUri())) return false;
		else if (!texts.hasEqualValues(other.texts)) return false;
		else if (!relations.hasEqualValues(other.relations)) return false;
		else return true;
	}
	
//	public void addRelation(RDFTriple triple) {
//		relations.addValue(triple.getConnection(), triple.getToUri());
//	}
//	
//	public void removeRelation(RDFTriple triple) {
//		relations.removeValue(triple.getConnection(), triple.getToUri());
//	}
//	
//	public void addText(RDFLabel label) {
//		
//	}
}
