package de.klinikum.service;

import static de.klinikum.domain.NameSpaces.GUI_TAB_TYPE;
import static de.klinikum.domain.NameSpaces.ONTOLOGIE_CONCEPT_HAS_LABEL;
import static de.klinikum.domain.NameSpaces.ONTOLOGIE_CONCEPT_TYPE;
import static de.klinikum.domain.NameSpaces.PATIENT_HAS_CONCEPT;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.openrdf.model.Literal;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.vocabulary.RDF;

import de.klinikum.domain.Concept;
import de.klinikum.domain.Patient;
import de.klinikum.persistence.SesameTripleStore;

public class ConceptServiceImpl implements ConceptService {

	@Inject
	SesameTripleStore tripleStore;
	
	@Override
    public List<Concept> findTabConcepts(Patient patient) throws IOException {

        List<Concept> concepts = new ArrayList<Concept>();

        String sparqlQuery = "SELECT ?Uri ?Label WHERE {";

        sparqlQuery += "<" + patient.getUri().toString() + "> <" + PATIENT_HAS_CONCEPT + "> ?Uri . ";

        sparqlQuery += "?Uri <" + RDF.TYPE + "> <" + GUI_TAB_TYPE + "> . ";

        sparqlQuery += "?Uri <" + ONTOLOGIE_CONCEPT_HAS_LABEL + "> \"?Label\"}";

        Set<HashMap<String, Value>> queryResult = this.tripleStore.executeSelectSPARQLQuery(sparqlQuery);

        for (HashMap<String, Value> item : queryResult) {
            Concept concept = new Concept();
            concept.setUri(item.get("Uri").toString());
            concept.setLabel(item.get("Label").stringValue());
            concepts.add(concept);
        }

        return concepts;

    }

	@Override
	public Concept createConcept(Concept concept) {
		return null;
	}

	@Override
	public List<Concept> findConcepts(Patient patient) {
		return null;

	}

	@Override
	public Concept addConceptToPatient(Concept concept, Patient patient, boolean tabConcept) throws IOException {
		
//		TODO: prüfen ob Concept bereits vorhanden
		
//		Concept anlegen
		URI conceptUri = this.tripleStore.getUniqueURI(ONTOLOGIE_CONCEPT_TYPE.toString());
		
		concept.setUri(conceptUri.toString());
		
		URI conceptTypeUri = this.tripleStore.getValueFactory().createURI(ONTOLOGIE_CONCEPT_TYPE.toString());
			
		this.tripleStore.addTriple(conceptUri, RDF.TYPE, conceptTypeUri);
		
//		TODO: prüfen ob Patient vorhanden, wir gehen einfachm mal davon aus dass einer existiert
		URI patientUri = this.tripleStore.getValueFactory().createURI(patient.getUri());
		URI patientHasConceptUri = this.tripleStore.getValueFactory().createURI(PATIENT_HAS_CONCEPT.toString());
		
		this.tripleStore.addTriple(patientUri, patientHasConceptUri, conceptUri);
		
		URI conceptHasLabelUri = this.tripleStore.getValueFactory().createURI(ONTOLOGIE_CONCEPT_HAS_LABEL.toString());
		Literal conceptLabelLiteral = 	this.tripleStore.getValueFactory().createLiteral(concept.getLabel());
		
		this.tripleStore.addTriple(conceptUri, conceptHasLabelUri, conceptLabelLiteral);
		
		if(tabConcept){
			URI tabTypeUri = this.tripleStore.getValueFactory().createURI(GUI_TAB_TYPE.toString());
			this.tripleStore.addTriple(conceptUri, RDF.TYPE, tabTypeUri);
		}
		
		return concept;
	}

}
