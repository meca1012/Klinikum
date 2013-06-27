package service;

import static de.klinikum.persistence.NameSpaces.CONCEPT_LINKED_TO;
import static de.klinikum.persistence.NameSpaces.CONCEPT_TYPE;
import static de.klinikum.persistence.NameSpaces.GUI_TAB_TYPE;
import static de.klinikum.persistence.NameSpaces.PATIENT_HAS_CONCEPT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openrdf.model.util.ModelException;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.repository.RepositoryException;

import de.klinikum.domain.Address;
import de.klinikum.domain.Concept;
import de.klinikum.domain.Patient;
import de.klinikum.exceptions.SpirontoException;
import de.klinikum.exceptions.TripleStoreException;
import de.klinikum.persistence.SesameTripleStore;
import de.klinikum.service.implementation.ConceptServiceImpl;
import de.klinikum.service.implementation.PatientServiceImpl;
import de.klinikum.service.interfaces.ConceptService;
import de.klinikum.service.interfaces.PatientService;

/**
 * 
 * @author Carsten Meiser, Ivan Tepeluk, Andreas Schillinger
 *
 */
@RunWith(Arquillian.class)
public class ConceptServiceTest {

    @Inject
    ConceptService conceptService;

    @Inject
    PatientService patientService;

    @Inject
    SesameTripleStore tripleStore;
    
    Random generator = new Random(System.currentTimeMillis());
    
    private Patient patient;

    /**
     */
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class).addClass(ConceptServiceImpl.class)
                .addClass(PatientServiceImpl.class).addClass(SesameTripleStore.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    @Before
    public void createPatient() throws Exception{
        this.patient = new Patient();
        this.patient.setFirstName("Anke");
        this.patient.setLastName("Musterfrau");
        this.patient.setDateOfBirth(new DateTime());
        this.patient.setPatientNumber(String.valueOf(this.generator.nextInt()));
        Address address = new Address(null, "Musterstr.", "1", "Musterstadt", "76123", "D", "110");
        this.patient.setAddress(address);
        this.patient = this.patientService.createPatientRDF(this.patient);
    }
 
    @Test
    public void createConcept() throws Exception {
        Concept newConcept = new Concept();
        newConcept.setLabel("Neues TestConcept");
        newConcept.setPatientUri(this.patient.getUri());
        newConcept = this.conceptService.createConcept(newConcept);
        
        assertNotNull(newConcept.getUri());
        assertTrue(this.tripleStore.repositoryHasStatement(this.patient.getUri(), PATIENT_HAS_CONCEPT.toString(), newConcept.getUri()));
    }
    
    @Test
    public void getConceptByUri() throws IOException, RepositoryException, SpirontoException {
        Concept concept = new Concept();
        concept.setLabel("Neues TabConcept");
        concept.setPatientUri(this.patient.getUri());
        concept = this.conceptService.createConcept(concept);
        
        Concept foundConcept = this.conceptService.getConceptByUri(concept.getUri());
        assertNotNull(foundConcept);
        assertEquals(foundConcept, concept);
    }
    
    /**
     * Get the standard tabConcepts
     * @throws Exception 
     */
    @Test
    public void getTabConcepts() throws Exception {
        List<Concept> tabConcepts = new ArrayList<Concept>();
        tabConcepts = this.conceptService.getTabConcepts(this.patient);
       
        assertNotNull(tabConcepts);
        for(Concept concept : tabConcepts){
            assertTrue(this.tripleStore.repositoryHasStatement(concept.getUri(), RDF.TYPE.toString(), GUI_TAB_TYPE.toString()));         
        }             
    }

    @Test
    public void connectSingleConcept() throws Exception {
        Concept concept1 = new Concept();
        concept1.setLabel("Concept 1");
        concept1.setPatientUri(this.patient.getUri());
        concept1 = this.conceptService.createConcept(concept1);
        
        Concept concept2 = new Concept();
        concept2.setLabel("Concept 2");
        concept2.setPatientUri(this.patient.getUri());
        concept2 = this.conceptService.createConcept(concept2);
        
        this.conceptService.connectSingleConcept(concept1, concept2);        
        assertTrue(this.tripleStore.repositoryHasStatement(concept1.getUri(), CONCEPT_LINKED_TO.toString(), concept2.getUri()));      
    }

    @Test
    public void addTabConcept() throws TripleStoreException, IOException {
        Concept tabConcept = new Concept();
        tabConcept.setLabel("Neues TabConcept");
        tabConcept.setPatientUri(this.patient.getUri());
        tabConcept = this.conceptService.createTabConcept(tabConcept);
        
        assertNotNull(tabConcept);
        assertTrue(this.tripleStore.repositoryHasStatement(tabConcept.getUri(), RDF.TYPE.toString(), GUI_TAB_TYPE.toString()));      
    }

    @Test
    public void getDirectConnected() {
        /*
         * Tested in method getTabConcepts().
         */
    }

    @Test
    public void findAllConceptsOfPatient() throws IOException, SpirontoException {
        
        List<Concept> concepts = this.conceptService.findAllConceptsOfPatient(this.patient);
        
        assertNotNull(concepts);
        for(Concept concept : concepts){
            assertTrue(this.tripleStore.repositoryHasStatement(concept.getUri(), RDF.TYPE.toString(), CONCEPT_TYPE.toString()));         
        }       
    }
    
    @Test
    public void updateConcept(Concept concept) throws RepositoryException, SpirontoException, IOException {
   
     Concept newConcept = new Concept();
     newConcept.setLabel("Neues TestConcept");
     newConcept.setPatientUri(this.patient.getUri());
     newConcept = this.conceptService.createConcept(newConcept);
     newConcept.setLabel("Neuer Conceptname :-)");
     Concept updateConcept = this.conceptService.updateConcept(newConcept);
     
     assertEquals(updateConcept, newConcept);
    }

    @Test
    public void findConceptsOfTabConcept() throws IOException, SpirontoException, RepositoryException, ModelException {
        Concept firstTabConcept = this.conceptService.getTabConcepts(this.patient).get(0);
        List<Concept> concepts = this.conceptService.findConceptsOfTabConcept(firstTabConcept);
        assertNotNull(concepts);
        for (Concept c : concepts) {
            assertTrue(this.tripleStore.repositoryHasStatement(c.getUri(), RDF.TYPE.toString(), CONCEPT_TYPE.toString()));
            boolean one = this.tripleStore.repositoryHasStatement(c.getUri(), CONCEPT_LINKED_TO.toString(), firstTabConcept.getUri());
            boolean two = this.tripleStore.repositoryHasStatement(firstTabConcept.getUri(), CONCEPT_LINKED_TO.toString(), c.getUri());
            boolean isConnected = false;
            if (one || two) {
                isConnected = true;
            }
            assertTrue(isConnected);
        }
    }

    @Test
    public void getConnected() throws IOException, SpirontoException, RepositoryException, ModelException {
        Concept concept = this.conceptService.getTabConcepts(this.patient).get(0);
        List<Concept> connected = this.conceptService.getConnectedConceptUris(concept);
        assertNotNull(connected);
        for (Concept c : connected) {
            assertNotNull(c);
            assertTrue(this.conceptService.conceptExists(c.getUri()));
            boolean one = this.tripleStore.repositoryHasStatement(c.getUri(), CONCEPT_LINKED_TO.toString(), concept.getUri());
            boolean two = this.tripleStore.repositoryHasStatement(concept.getUri(), CONCEPT_LINKED_TO.toString(), c.getUri());
            boolean isConnected = false;
            if (one || two) {
                isConnected = true;
            }
            assertTrue(isConnected);
        }
    }

    @Test    
    public void connectMultipleConcepts() {
        /*
         * Is tested with connectSingle test.
         */
    }

    @Test
    public void isTabConcept() throws TripleStoreException, IOException, RepositoryException {
        Concept concept = new Concept();
        concept.setLabel("New TabConcept");
        concept.setPatientUri(this.patient.getUri());
        concept = this.conceptService.createTabConcept(concept);
        
        assertTrue(this.conceptService.isTabConcept(concept));
    }

    @Test
    public void conceptExists() throws TripleStoreException, IOException {
        Concept concept = new Concept();
        concept.setLabel("New Concept");
        concept.setPatientUri(this.patient.getUri());
        concept = this.conceptService.createConcept(concept);
        
        assertTrue(this.conceptService.conceptExists(concept.getUri()));
    }

    @Test
    public void getConnectedConceptUris() throws IOException, SpirontoException, RepositoryException, ModelException {
        Concept concept = this.conceptService.getTabConcepts(this.patient).get(0);
        List<Concept> connected = this.conceptService.getConnectedConceptUris(concept);
        assertNotNull(connected);
        for (Concept c : connected) {
            assertTrue(this.conceptService.conceptExists(c.getUri()));
        }
        
    }

    public Patient getPatient() {
        return this.patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

}
