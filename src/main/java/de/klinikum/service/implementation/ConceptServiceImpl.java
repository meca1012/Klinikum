package de.klinikum.service.implementation;

import static de.klinikum.domain.NameSpaces.CONCEPT_IS_EDITABLE;
import static de.klinikum.domain.NameSpaces.GUI_TAB_TYPE;
import static de.klinikum.domain.NameSpaces.ONTOLOGIE_CONCEPT_HAS_LABEL;
import static de.klinikum.domain.NameSpaces.ONTOLOGIE_CONCEPT_LINKED_TO;
import static de.klinikum.domain.NameSpaces.ONTOLOGIE_CONCEPT_TYPE;
import static de.klinikum.domain.NameSpaces.PATIENT_HAS_CONCEPT;
import static de.klinikum.domain.NameSpaces.PATIENT_TYPE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.openrdf.model.Model;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.util.ModelException;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.repository.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.klinikum.domain.Concept;
import de.klinikum.domain.Patient;
import de.klinikum.exceptions.SpirontoException;
import de.klinikum.persistence.SesameTripleStore;
import de.klinikum.service.interfaces.ConceptService;

@Named
public class ConceptServiceImpl implements ConceptService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConceptServiceImpl.class);
	
    @Inject
    SesameTripleStore tripleStore;

    @Override
    public List<Concept> getTabConcepts(Patient patient) throws SpirontoException {
        
        List<Concept> tabConcepts = new ArrayList<Concept>();
        
        String sparqlQuery = "SELECT DISTINCT ?uri WHERE {";        
        sparqlQuery += "?uri <" + RDF.TYPE + "> <" + GUI_TAB_TYPE + "> . ";
        sparqlQuery += "<" + patient.getUri() + "> <" + PATIENT_HAS_CONCEPT + "> ?uri}";
        Set<HashMap<String, Value>> queryResult = this.tripleStore.executeSelectSPARQLQuery(sparqlQuery);
        for (HashMap<String, Value> item : queryResult) {
            Concept concept = new Concept();
            concept.setUri(item.get("uri").stringValue());
            concept = getConceptByUri(concept.getUri());
            concept.setConnectedConcepts(getDirectConnected(concept, false));
            tabConcepts.add(concept);
        }
        return tabConcepts;
    }

    @Override
    public Concept createConcept(Concept concept) throws IOException {

        if (concept.getPatientUri() == null) {
            return null;
        }
        
        if (!this.tripleStore.repositoryHasStatement(concept.getPatientUri(), RDF.TYPE.toString(), PATIENT_TYPE.toString())) {
            return null;
        }

        // Concept anlegen
        URI conceptUri = this.tripleStore.getUniqueURI(ONTOLOGIE_CONCEPT_TYPE.toString());

        concept.setUri(conceptUri.toString());        

        this.tripleStore.addTriple(conceptUri.toString(), RDF.TYPE.toString(), ONTOLOGIE_CONCEPT_TYPE.toString());

        this.tripleStore.addTriple(concept.getPatientUri(), PATIENT_HAS_CONCEPT.toString(), conceptUri.toString());

        this.tripleStore.addTripleWithStringLiteral(conceptUri.toString(), ONTOLOGIE_CONCEPT_HAS_LABEL.toString(), concept.getLabel());
        
        this.tripleStore.addTripleWithBooleanLiteral(conceptUri.toString(), CONCEPT_IS_EDITABLE.toString(), concept.isEditable());

        return concept;
    }

    @Override
    public void connectSingleConcept(Concept from, Concept to) throws IOException {
        if (this.tripleStore.repositoryHasStatement(from.getUri(), ONTOLOGIE_CONCEPT_LINKED_TO.toString(), to.getUri())) {
            return;
        }
        this.tripleStore.addTriple(from.getUri(), ONTOLOGIE_CONCEPT_LINKED_TO.toString(), to.getUri());
    }

    @Override
    public Concept createTabConcept(Concept concept) throws IOException {

        concept.setEditable(false);
        concept = this.createConcept(concept);
        if (concept == null) {
            return null;
        }
        this.tripleStore.addTriple(concept.getUri(), RDF.TYPE.toString(), GUI_TAB_TYPE.toString());
        return concept;
    }

    @Override
    public List<Concept> getDirectConnected(Concept concept, boolean onlyUris) throws SpirontoException {

        List<Concept> connectedConcepts = new ArrayList<Concept>();
        Model statementList;

        try {
            statementList = this.tripleStore.getStatementList(concept.getUri(), ONTOLOGIE_CONCEPT_LINKED_TO.toString(),
                    null);
            for (Statement conceptStatement : statementList) {
                if (onlyUris) {
                    Concept conceptToAdd = new Concept();
                    conceptToAdd.setUri(conceptStatement.getObject().stringValue());
                    connectedConcepts.add(conceptToAdd);
                }
                else {
                    Concept conceptToAdd = new Concept();
                    conceptToAdd = this.getConceptByUri(conceptStatement.getObject().stringValue());
                    connectedConcepts.add(conceptToAdd);
                }
            }
            statementList = this.tripleStore.getStatementList(null, ONTOLOGIE_CONCEPT_LINKED_TO.toString(),
                    concept.getUri());
            for (Statement conceptStatement : statementList) {                
                if (onlyUris) {
                    Concept conceptToAdd = new Concept();
                    conceptToAdd.setUri(conceptStatement.getSubject().stringValue());
                    if (!connectedConcepts.contains(conceptToAdd)) {
                        connectedConcepts.add(conceptToAdd);
                    }
                }
                else {
                    Concept conceptToAdd = new Concept();
                    conceptToAdd = this.getConceptByUri(conceptStatement.getSubject().stringValue());
                    if (!connectedConcepts.contains(conceptToAdd)) {
                        connectedConcepts.add(conceptToAdd);
                    }
                }
            }
        }
        catch (RepositoryException e) {            
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return connectedConcepts;
    }

    @Override
    public List<Concept> findAllConceptsOfPatient(Patient patient) throws SpirontoException {
        List<Concept> concepts = new ArrayList<Concept>();
        Model statementList;

        try {
            statementList = this.tripleStore.getStatementList(patient.getUri(), PATIENT_HAS_CONCEPT.toString(), null);
            for (Statement conceptStatement : statementList) {
                concepts.add(this.getConceptByUri(conceptStatement.getObject().toString()));
            }
        }
        catch (RepositoryException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return concepts;
    }

    @Override
    public List<Concept> findConceptsOfTabConcept(Concept tabConcept) throws RepositoryException, IOException,
            ModelException, SpirontoException {
        if (!this.isTabConcept(tabConcept)) {
            return null;
        }
        List<Concept> conceptsToReturn = new ArrayList<Concept>();
        conceptsToReturn = this.getConnected(tabConcept.getUri(), conceptsToReturn, false);
        return conceptsToReturn;

    }

    @Override
    public List<Concept> getConnected(String conceptUri, List<Concept> connected, boolean onlyUris)
            throws RepositoryException, IOException, ModelException, SpirontoException {

        Model statementList;
        try {
            statementList = this.tripleStore.getStatementList(conceptUri, ONTOLOGIE_CONCEPT_LINKED_TO.toString(), null);
            if (statementList.size() > 1) {
                for (Statement conceptStatement : statementList) {
                    this.getConnected(conceptStatement.getObject().stringValue(), connected, onlyUris);
                }
            }
            else {
                if (onlyUris) {
                    Concept conceptToAdd = new Concept();
                    conceptToAdd.setUri(statementList.objectURI().toString());
                    connected.add(conceptToAdd);
                }
                else {
                    connected.add(this.getConceptByUri(statementList.objectURI().toString()));
                }
            }
            return connected;
        }
        catch (RepositoryException re) {
            throw new IOException(re);
        }
    }

    @Override
    public Concept getConceptByUri(String conceptUri) throws SpirontoException {
        Concept conceptToReturn = new Concept();
        conceptToReturn.setUri(conceptUri);

        String sparqlQuery = "SELECT ?Label ?patientUri WHERE {";
        sparqlQuery += "<" + conceptUri + "> <" + ONTOLOGIE_CONCEPT_HAS_LABEL + "> ?Label . ";
        sparqlQuery += "?patientUri <" + PATIENT_HAS_CONCEPT + "> <" + conceptUri + ">}";
        Set<HashMap<String, Value>> queryResult = this.tripleStore.executeSelectSPARQLQuery(sparqlQuery);
        for (HashMap<String, Value> item : queryResult) {
            conceptToReturn.setLabel(item.get("Label").stringValue());
            conceptToReturn.setPatientUri(item.get("patientUri").toString());
        }
        return conceptToReturn;
    }

    @Override
    public void connectMultipleConcepts(Concept from, List<Concept> to) throws IOException {
        for (Concept c : to) {
            this.connectSingleConcept(from, c);
        }
    }

    @Override
    public boolean isTabConcept(Concept concept) throws RepositoryException, IOException {
        return this.tripleStore.repositoryHasStatement(concept.getUri(), RDF.TYPE.toString(), GUI_TAB_TYPE.toString());
    }

    @Override
    public boolean conceptExists(Concept concept) throws IOException {
        if (concept.getUri() == null) {
            return false;
        }
        else {
            return this.tripleStore.repositoryHasStatement(concept.getUri(), RDF.TYPE.toString(),
                    ONTOLOGIE_CONCEPT_TYPE.toString());
        }
    }

    @Override
    public List<Concept> getConnectedConceptUris(Concept concept) throws RepositoryException, ModelException,
            IOException, SpirontoException {

        List<Concept> conceptsToReturn = this.getDirectConnected(concept, true);

        return conceptsToReturn;
    }
    
    @Override
    public Concept updateConcept(Concept concept) throws SpirontoException, IOException, RepositoryException, ModelException {
        
        if (!this.tripleStore.repositoryHasStatement(concept.getUri(),RDF.TYPE.toString() , ONTOLOGIE_CONCEPT_TYPE.toString())) {
            return null;
        }
        
        Concept existingConcept = getConceptByUri(concept.getUri());        
        
        if (concept.getLabel() != null) {
            if (!concept.getLabel().equals(existingConcept.getLabel())) {
                this.tripleStore.removeTriples(existingConcept.getUri(), ONTOLOGIE_CONCEPT_HAS_LABEL.toString(),null);
                this.tripleStore.addTripleWithStringLiteral(concept.getUri(), ONTOLOGIE_CONCEPT_HAS_LABEL.toString(), concept.getLabel());
            }
        }
        
        if (concept.getConnectedConcepts() != null) {
            
            existingConcept.setConnectedConcepts(getConnectedConceptUris(existingConcept));
            
            if (existingConcept.getConnectedConcepts() != null) {
                for (Concept ec : existingConcept.getConnectedConcepts()) {
                    
                    if (this.tripleStore.repositoryHasStatement(existingConcept.getUri(), ONTOLOGIE_CONCEPT_LINKED_TO.toString(), ec.getUri())) {
                        this.tripleStore.removeTriples(existingConcept.getUri(), ONTOLOGIE_CONCEPT_LINKED_TO.toString(), ec.getUri());
                    } else {
                        this.tripleStore.removeTriples(ec.getUri(), ONTOLOGIE_CONCEPT_LINKED_TO.toString(), existingConcept.getUri());
                    }
                }
            }
            
            for (Concept c : concept.getConnectedConcepts()) {
                this.tripleStore.addTriple(concept.getUri(), ONTOLOGIE_CONCEPT_LINKED_TO.toString(), c.getUri());
            }
        }
        
        return concept;
    }
}
