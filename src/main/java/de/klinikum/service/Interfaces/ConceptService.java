package de.klinikum.service.Interfaces;

import java.io.IOException;
import java.util.List;

import org.openrdf.model.util.ModelException;
import org.openrdf.repository.RepositoryException;

import de.klinikum.domain.Concept;
import de.klinikum.domain.Patient;
import de.klinikum.exceptions.SpirontoException;

public interface ConceptService {

    List<Concept> getTabConcepts() throws IOException, SpirontoException;

    Concept createConcept(Concept concept) throws IOException;

    void connectSingleConcept(Concept from, Concept to) throws IOException;

    Concept addTabConcept(Concept concept) throws IOException;

    List<Concept> getDirectConnected(Concept concept, boolean onlyUris) throws IOException, SpirontoException;

    List<Concept> findAllConceptsOfPatient(Patient patient) throws IOException, SpirontoException;

    List<Concept> findConceptsOfTabConcept(Concept tabConcept) throws RepositoryException, IOException, ModelException,
            SpirontoException;

    List<Concept> getConnected(String conceptUri, List<Concept> connected, boolean onlyUris) throws RepositoryException, IOException,
            ModelException, SpirontoException;

    Concept getConceptByUri(String conceptUri) throws RepositoryException, SpirontoException;

    void connectMultipleConcepts(Concept from, List<Concept> to) throws IOException;

    boolean isTabConcept(Concept concept) throws RepositoryException, IOException;

    List<Concept> getConnectedConceptUris(Concept concept) throws RepositoryException, ModelException, IOException,
            SpirontoException;
}
