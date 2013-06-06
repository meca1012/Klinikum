package de.klinikum.helper;

import java.io.IOException;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;

import org.openrdf.model.vocabulary.RDF;

import de.klinikum.domain.Address;
import de.klinikum.domain.Concept;
import de.klinikum.domain.Patient;
import de.klinikum.domain.TextBlock;
import de.klinikum.persistence.SesameTripleStore;
import de.klinikum.service.Implementation.ConceptServiceImpl;
import de.klinikum.service.Implementation.PatientServiceImpl;
import de.klinikum.service.Implementation.TextBlockServiceImpl;

@Named
public class AppStartup {
    
    @Inject
    PatientServiceImpl patientService;
    
    @Inject
    ConceptServiceImpl conceptService;
    
    @Inject
    TextBlockServiceImpl textBlockService;
    
    @Inject
    SesameTripleStore triplestore;  
    
    public void createPreDefinedConcepts() {
        
    }
    
    public void createTestData() {
        
        try {
            triplestore.removeTriples(null, RDF.TYPE, null);
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        //Address
        Address address1 = new Address();
        address1.setStreet("Hauptstraﬂe");
        address1.setStreetNumber("1");
        address1.setZip("11111");
        address1.setCity("Hauptstadt");
        address1.setCountry("Hauptland");
        address1.setPhone("11111");
        
        //Patient
        Patient patient1 = new Patient();
        patient1.setFirstName("Max");
        patient1.setLastName("Mustermann");
        patient1.setPatientNumber("1");
        patient1.setDateOfBirth(new Date(1, 1, 2011));
        patient1.setAddress(address1);
        
        Patient patient2 = new Patient();
        patient2.setFirstName("Maria");
        patient2.setLastName("Musterfrau");
        patient2.setPatientNumber("2");
        patient2.setDateOfBirth(new Date(2, 2, 2012));
        patient2.setAddress(address1);
        
        try {
            patient1 = patientService.createPatientRDF(patient1);
            patient2 = patientService.createPatientRDF(patient2);
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        //Concept
        Concept concept1 = new Concept();
        concept1.setLabel("concept1");
        concept1.setPatientUri(patient1.getUri());
        
        Concept concept2 = new Concept();
        concept2.setLabel("concept2");
        concept2.setPatientUri(patient1.getUri());
        
        Concept concept3 = new Concept();
        concept3.setLabel("concept3");
        concept3.setPatientUri(patient1.getUri());
        
        Concept concept4 = new Concept();
        concept4.setLabel("concept4");
        concept4.setPatientUri(patient2.getUri());
        
        Concept concept5 = new Concept();
        concept5.setLabel("concept5");
        concept5.setPatientUri(patient2.getUri());
        
        Concept concept6 = new Concept();
        concept6.setLabel("concept6");
        concept6.setPatientUri(patient2.getUri());       
        
        try {
            concept2 = conceptService.addConceptToPatient(concept2);
            concept3 = conceptService.addConceptToPatient(concept3);
            concept5 = conceptService.addConceptToPatient(concept5);
            concept6 = conceptService.addConceptToPatient(concept6);
            
            concept1 = conceptService.addTabConcept(concept1);
            concept4 = conceptService.addTabConcept(concept4);
            
            concept1.addConnectedConcepts(concept2);
            concept1.addConnectedConcepts(concept3);
            
            concept4.addConnectedConcepts(concept5);
            concept4.addConnectedConcepts(concept6);
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        //TextBlock
        TextBlock textBlock1 = new TextBlock();
        textBlock1.setText("This is a so important text, that you can't imagine how endless its context is.");
        textBlock1.setPatientUri(patient1.getUri());
        textBlock1.setCreated(new Date(3, 3, 3013));        
        textBlock1.setConcepts(concept1.getConnectedConcepts());
        
        TextBlock textBlock2 = new TextBlock();
        textBlock2.setText("This is a really endless text. Go and get a coffee before reading it.");
        textBlock2.setPatientUri(patient2.getUri());
        textBlock2.setCreated(new Date(4, 4, 4014));        
        textBlock2.setConcepts(concept4.getConnectedConcepts());
        
        try {
            textBlock1 = textBlockService.createTextBlock(textBlock1);
            textBlock2 = textBlockService.createTextBlock(textBlock2);           
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
}
