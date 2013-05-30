package de.klinikum.service;

import java.io.IOException;
import java.util.List;

import org.openrdf.repository.RepositoryException;

import de.klinikum.domain.Concept;
import de.klinikum.domain.Patient;

public interface ConceptService {

	List<Concept> findTabConceptsOfPatient(Patient patient) throws IOException;
	
	Concept addConceptToPatient(Concept concept) throws IOException;
	
	void connectSingleConcept(Concept from, Concept to) throws IOException;
	
	Concept addTabConcept(Concept concept) throws IOException;
	
	List<Concept> getDirectConnected(Concept concept) throws IOException;
	
	List<Concept> findAllConceptsOfPatient(Patient patient) throws IOException;
	
	List<Concept> findConceptsOfTabConcept(Concept tabConcept) throws RepositoryException, IOException;
	
	List<Concept> getConnected(String conceptUri, List<Concept> connected) throws RepositoryException, IOException;
	
	Concept getConceptByUri(String conceptUri) throws RepositoryException, IOException;
	
	void connectMultipleConcepts(Concept from, List<Concept> to) throws IOException;
	
	boolean isTabConcept(Concept concept) throws RepositoryException, IOException;
}
