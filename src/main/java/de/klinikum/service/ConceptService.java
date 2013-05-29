package de.klinikum.service;

import java.io.IOException;
import java.util.List;

import de.klinikum.domain.Concept;
import de.klinikum.domain.Patient;

public interface ConceptService {

	List<Concept> findTabConcepts(Patient patient) throws IOException;

	List<Concept> findConcepts(Patient patient);
	
	Concept createConcept(Concept concept);
	
	Concept addConceptToPatient(Concept concept, Patient patient, boolean tabConcept) throws IOException;

}
