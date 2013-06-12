package de.klinikum.service.Implementation;

import static de.klinikum.domain.NameSpaces.GUI_TAB_TYPE;
import static de.klinikum.domain.NameSpaces.ONTOLOGIE_CONCEPT_HAS_LABEL;
import static de.klinikum.domain.NameSpaces.ONTOLOGIE_CONCEPT_LINKED_TO;
import static de.klinikum.domain.NameSpaces.ONTOLOGIE_CONCEPT_TYPE;
import static de.klinikum.domain.NameSpaces.PATIENT_HAS_CONCEPT;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.openrdf.model.Literal;
import org.openrdf.model.Model;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.util.ModelException;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.repository.RepositoryException;

import de.klinikum.domain.Concept;
import de.klinikum.domain.Patient;
import de.klinikum.exceptions.SpirontoException;
import de.klinikum.persistence.SesameTripleStore;
import de.klinikum.service.Interfaces.ConceptService;

public class ConceptServiceImpl implements ConceptService {

    @Inject
    private SesameTripleStore tripleStore;

    @Override
    public List<Concept> findTabConceptsOfPatient(Patient patient) throws SpirontoException {

        List<Concept> concepts = new ArrayList<Concept>();

        String sparqlQuery = "SELECT DISTINCT ?Uri ?Label WHERE {";

        sparqlQuery += "<" + patient.getUri().toString() + "> <" + PATIENT_HAS_CONCEPT + "> ?Uri . ";

        sparqlQuery += "?Uri <" + RDF.TYPE + "> <" + ONTOLOGIE_CONCEPT_TYPE + "> . ";

        sparqlQuery += "?Uri <" + RDF.TYPE + "> <" + GUI_TAB_TYPE + "> . ";

        sparqlQuery += "?Uri <" + ONTOLOGIE_CONCEPT_HAS_LABEL + "> ?Label}";

        Set<HashMap<String, Value>> queryResult = this.tripleStore.executeSelectSPARQLQuery(sparqlQuery);

        for (HashMap<String, Value> item : queryResult) {
            Concept concept = new Concept();
            concept.setUri(item.get("Uri").toString());
            concept.setLabel(item.get("Label").stringValue());
            concept.setPatientUri(patient.getUri());
            concepts.add(concept);
        }
        return concepts;
    }

    @Override
    public Concept addConceptToPatient(Concept concept) throws IOException {

        // TODO: pr�fen ob Concept bereits vorhanden

        // Concept anlegen
        URI conceptUri = this.tripleStore.getUniqueURI(ONTOLOGIE_CONCEPT_TYPE.toString());

        concept.setUri(conceptUri.toString());

        URI conceptTypeUri = this.tripleStore.getValueFactory().createURI(ONTOLOGIE_CONCEPT_TYPE.toString());

        this.tripleStore.addTriple(conceptUri, RDF.TYPE, conceptTypeUri);

        // TODO: pr�fen ob Patient vorhanden, wir gehen einfachm mal davon aus dass einer existiert
        URI patientUri = this.tripleStore.getValueFactory().createURI(concept.getPatientUri());
        URI patientHasConceptUri = this.tripleStore.getValueFactory().createURI(PATIENT_HAS_CONCEPT.toString());

        this.tripleStore.addTriple(patientUri, patientHasConceptUri, conceptUri);

        URI conceptHasLabelUri = this.tripleStore.getValueFactory().createURI(ONTOLOGIE_CONCEPT_HAS_LABEL.toString());
        Literal conceptLabelLiteral = this.tripleStore.getValueFactory().createLiteral(concept.getLabel());

        this.tripleStore.addTriple(conceptUri, conceptHasLabelUri, conceptLabelLiteral);

//        if (concept.getConnectedConcepts() != null) {
//            for (Concept c : concept.getConnectedConcepts()) {
//                if (!conceptExists(c)) {
//                    addConceptToPatient(c);
//                }
//                this.connectSingleConcept(concept, c);
//            }
//        }
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
    public Concept addTabConcept(Concept concept) throws IOException {

        concept = this.addConceptToPatient(concept);
        this.tripleStore.addTriple(concept.getUri(), RDF.TYPE.toString(), GUI_TAB_TYPE.toString());
        return concept;
    }

    @Override
    public List<Concept> getDirectConnected(Concept concept, boolean onlyUris) throws SpirontoException {
        
        List <Concept> connectedConcepts = new ArrayList<Concept>();
        Model statementList;
        
        try {
            statementList = this.tripleStore.getStatementList(concept.getUri(), ONTOLOGIE_CONCEPT_LINKED_TO.toString(), null);           
            for (Statement conceptStatement : statementList) {
                if (onlyUris) {
                    Concept conceptToAdd = new Concept();
                    conceptToAdd.setUri(conceptStatement.getObject().stringValue());
                    connectedConcepts.add(conceptToAdd);
                } else {
                    connectedConcepts.add(getConceptByUri(conceptStatement.getObject().toString()));
                }
                
            }
            statementList = this.tripleStore.getStatementList(null, ONTOLOGIE_CONCEPT_LINKED_TO.toString(), concept.getUri());
            for (Statement conceptStatement : statementList) {
                if (onlyUris) {
                    Concept conceptToAdd = new Concept();
                    conceptToAdd.setUri(conceptStatement.getSubject().stringValue());
                    connectedConcepts.add(conceptToAdd);
                }
                connectedConcepts.add(getConceptByUri(conceptStatement.getSubject().stringValue()));
            }
        }
        catch (RepositoryException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }              
        return connectedConcepts;
    }

    @Override
    public List<Concept> findAllConceptsOfPatient(Patient patient) throws SpirontoException {
        List<Concept> concepts = new ArrayList<Concept>();

        String sparqlQuery = "SELECT ?Uri ?Label WHERE {";

        sparqlQuery += "<" + patient.getUri().toString() + "> <" + PATIENT_HAS_CONCEPT + "> ?Uri . ";

        sparqlQuery += "?Uri <" + RDF.TYPE + "> <" + ONTOLOGIE_CONCEPT_TYPE + "> . ";

        sparqlQuery += "?Uri <" + ONTOLOGIE_CONCEPT_HAS_LABEL + "> ?Label}";

        Set<HashMap<String, Value>> queryResult = this.tripleStore.executeSelectSPARQLQuery(sparqlQuery);

        for (HashMap<String, Value> item : queryResult) {
            Concept concept = new Concept();
            concept.setUri(item.get("Uri").toString());
            concept.setLabel(item.get("Label").stringValue());
            concept.setPatientUri(patient.getUri());
            concepts.add(concept);
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
    public List<Concept> getConnected(String conceptUri, List<Concept> connected, boolean onlyUris) throws RepositoryException,
            IOException, ModelException, SpirontoException {

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
                } else {
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
    
    public boolean conceptExists(Concept concept) throws IOException {
        if (concept.getUri() == null) {
            return false;
        } else {
            return this.tripleStore.repositoryHasStatement(concept.getUri(), RDF.TYPE.toString(), ONTOLOGIE_CONCEPT_TYPE.toString());
        }
    }
    
    @Override
    public List<Concept> getConnectedConceptUris(Concept concept) throws RepositoryException, ModelException, IOException, SpirontoException {
      
        List<Concept> conceptsToReturn = getDirectConnected(concept, true);       
      
        return conceptsToReturn;
    }
}
