package de.klinikum.helper;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.inject.Inject;
import javax.inject.Named;

import org.joda.time.DateTime;
import org.openrdf.model.vocabulary.RDF;

import de.klinikum.domain.Address;
import de.klinikum.domain.Concept;
import de.klinikum.domain.Note;
import de.klinikum.domain.Patient;
import de.klinikum.exceptions.TripleStoreException;
import de.klinikum.persistence.SesameTripleStore;
import de.klinikum.service.implementation.ConceptServiceImpl;
import de.klinikum.service.implementation.NoteServiceImpl;
import de.klinikum.service.implementation.PatientServiceImpl;

/**
 * 
 * AppStartup.java Purpose: Creates TestData for Prototype- Test. Creates connected patients, concepts and notes.
 * 
 * @author Spironto Team 1
 * @version 1.0 08/06/13
 */

@Named
public class AppStartup {

    @Inject
    PatientServiceImpl patientService;

    @Inject
    ConceptServiceImpl conceptService;

    @Inject
    NoteServiceImpl noteService;

    @Inject
    SesameTripleStore triplestore;

    public void createPreDefinedConcepts() {

    }

    public void createTestData() throws IOException, URISyntaxException {

        try {
            this.triplestore.removeTriples(null, RDF.TYPE, null);
            this.triplestore.setDatastoreTriple();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Address
        Address address1 = new Address();
        address1.setStreet("Hauptstraﬂe");
        address1.setStreetNumber("1");
        address1.setZip("11111");
        address1.setCity("Hauptstadt");
        address1.setCountry("Hauptland");
        address1.setPhone("11111");

        // Patient
        Patient patient1 = new Patient();
        patient1.setFirstName("Max");
        patient1.setLastName("Mustermann");
        patient1.setPatientNumber("1");
        patient1.setDateOfBirth(new DateTime());
        patient1.setAddress(address1);

        Patient patient2 = new Patient();
        patient2.setFirstName("Maria");
        patient2.setLastName("Musterfrau");
        patient2.setPatientNumber("2");
        patient2.setDateOfBirth(new DateTime());
        patient2.setAddress(address1);

        try {
            patient1 = this.patientService.createPatientRDF(patient1);
            patient2 = this.patientService.createPatientRDF(patient2);
        }
        catch (TripleStoreException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Concept
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
            concept2 = this.conceptService.createConcept(concept2);
            concept3 = this.conceptService.createConcept(concept3);
            concept5 = this.conceptService.createConcept(concept5);
            concept6 = this.conceptService.createConcept(concept6);

            concept1 = this.conceptService.createTabConcept(concept1);
            concept4 = this.conceptService.createTabConcept(concept4);

            concept1.addConnectedConcepts(concept2);
            this.conceptService.connectSingleConcept(concept1, concept2);
            concept1.addConnectedConcepts(concept3);
            this.conceptService.connectSingleConcept(concept1, concept3);

            concept4.addConnectedConcepts(concept5);
            this.conceptService.connectSingleConcept(concept4, concept5);
            concept4.addConnectedConcepts(concept6);
            this.conceptService.connectSingleConcept(concept4, concept6);
        }
        catch (TripleStoreException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Note
        Note note1 = new Note();
        note1.setText("This is a so important text, that you can't imagine how endless its context is.");
        note1.setTitle("endless context");
        note1.setPriority(2);
        note1.setPatientUri(patient1.getUri());
        note1.setCreated(new DateTime());
        note1.setConcepts(concept1.getConnectedConcepts());

        Note note2 = new Note();
        note2.setText("This is a really endless text. Go and get a coffee before reading it.");
        note2.setTitle("endless coffee");
        note2.setPriority(1);
        note2.setPatientUri(patient2.getUri());
        note2.setCreated(new DateTime());
        note2.setConcepts(concept4.getConnectedConcepts());

        note1 = this.noteService.createNote(note1);
        for (Concept c : note1.getConcepts()) {
            note1 = this.noteService.addConceptToNote(note1, c);
        }
        note2 = this.noteService.createNote(note2);
        for (Concept c : note2.getConcepts()) {
            note2 = this.noteService.addConceptToNote(note2, c);
        }

    }

}
