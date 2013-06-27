package service;

import static de.klinikum.persistence.NameSpaces.GUI_TAB_TYPE;
import static de.klinikum.persistence.NameSpaces.ONTOLOGIE_CONCEPT_LINKED_TO;
import static de.klinikum.persistence.NameSpaces.PATIENT_HAS_CONCEPT;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openrdf.model.vocabulary.RDF;

import de.klinikum.domain.Address;
import de.klinikum.domain.Concept;
import de.klinikum.domain.Patient;
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

    /**
         */
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class).addClass(ConceptServiceImpl.class)
                .addClass(PatientServiceImpl.class).addClass(SesameTripleStore.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    private Patient generateNewPatientWithAddress() {
        Patient patient = new Patient();
        patient.setFirstName("Alice");
        patient.setLastName("Smith");
        patient.setDateOfBirth(new DateTime());
        patient.setPatientNumber(String.valueOf(this.generator.nextInt()));
        Address address = new Address(null, "Musterstr.", "1", "Musterstadt", "76123", "D", "110");
        patient.setAddress(address);
        return patient;
    }

    // Get the standard tabConcepts
    @Test
    public void getTabConcepts() throws Exception {
        Patient patient = this.generateNewPatientWithAddress();
        patient = this.patientService.createPatientRDF(patient);
        List<Concept> tabConcepts = new ArrayList<Concept>();
        tabConcepts = this.conceptService.getTabConcepts(patient);

        assertNotNull(tabConcepts);
    }

    @Test
    public void createConcept() throws Exception {
        Patient patient = this.generateNewPatientWithAddress();
        patient = this.patientService.createPatientRDF(patient);
        Concept newConcept = new Concept();
        newConcept.setLabel("Neues TestConcept");
        newConcept.setPatientUri(patient.getUri());
        newConcept = this.conceptService.createConcept(newConcept);
        
        assertTrue(this.tripleStore.repositoryHasStatement(patient.getUri(), PATIENT_HAS_CONCEPT.toString(), newConcept.getUri()));
        assertNotNull(newConcept.getUri());
    }

    @Test
    public void connectSingleConcept() throws Exception {
        Patient patient = this.generateNewPatientWithAddress();
        patient = this.patientService.createPatientRDF(patient);
        Concept concept1 = new Concept();
        concept1.setLabel("Concept 1");
        concept1.setPatientUri(patient.getUri());
        concept1 = this.conceptService.createConcept(concept1);
        
        Concept concept2 = new Concept();
        concept2.setLabel("Concept 2");
        concept2.setPatientUri(patient.getUri());
        concept2 = this.conceptService.createConcept(concept2);
        
        this.conceptService.connectSingleConcept(concept1, concept2);
        
        assertTrue(this.tripleStore.repositoryHasStatement(concept1.getUri(), ONTOLOGIE_CONCEPT_LINKED_TO.toString(), concept2.getUri()));      
    }

    @Test
    public void addTabConcept() throws Exception {
        Patient patient = this.generateNewPatientWithAddress();
        patient = this.patientService.createPatientRDF(patient);
        Concept tabConcept = new Concept();
        tabConcept.setLabel("Neues TabConcept");
        tabConcept.setPatientUri(patient.getUri());
        tabConcept = this.conceptService.createTabConcept(tabConcept);
        
        assertTrue(this.tripleStore.repositoryHasStatement(tabConcept.getUri(), RDF.TYPE.toString(), GUI_TAB_TYPE.toString()));      
    }

    @Test
    @Ignore
    public void getDirectConnected() {
    }

    @Test
    @Ignore
    public void findAllConceptsOfPatient() {
    }

    @Test
    @Ignore
    public void findConceptsOfTabConcept() {
    }

    @Test
    @Ignore
    public void getConnected() {
    }

    @Test
    @Ignore
    public void getConceptByUri() {
    }

    @Test
    @Ignore
    public void connectMultipleConcepts() {
    }

    @Test
    @Ignore
    public void isTabConcept() {
    }

    @Test
    @Ignore
    public void conceptExists() {
    }

    @Test
    @Ignore
    public void getConnectedConceptUris(Concept concept) {
    }

    @Test
    @Ignore
    public void updateConcept(Concept concept) {
    }

}
