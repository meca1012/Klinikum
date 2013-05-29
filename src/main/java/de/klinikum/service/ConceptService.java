package de.klinikum.service;

import java.io.IOException;
import java.util.List;

import org.openrdf.repository.RepositoryException;

import de.klinikum.domain.Concept;
import de.klinikum.domain.Patient;

public interface ConceptService {

	List<Concept> findTabConceptsOfPatient(Patient patient) throws IOException;

	List<Concept> findAllConceptsOfPatient(Patient patient) throws IOException;
	
	List<Concept> findConceptsOfTabConcept(Concept tabConcept) throws RepositoryException, IOException;
	
	Concept createConcept(Concept concept);
	
	Concept addConceptToPatient(Concept concept) throws IOException;
	
	Concept addTabConcept(Concept concept) throws IOException;
	
	void connectSingleConcept(Concept from, Concept to) throws IOException;
	
	void connectMultipleConcepts(Concept from, List<Concept> to);

	List<Concept> getConnectedConceptsOfConcept(Concept concept) throws IOException;
	
	boolean isTabConcept(Concept concept) throws RepositoryException, IOException;
}
