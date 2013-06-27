package service;

import static de.klinikum.persistence.NameSpaces.PATIENT_HAS_NOTE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
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
import org.openrdf.repository.RepositoryException;

import de.klinikum.domain.Address;
import de.klinikum.domain.Concept;
import de.klinikum.domain.Note;
import de.klinikum.domain.Patient;
import de.klinikum.exceptions.SpirontoException;
import de.klinikum.exceptions.TripleStoreException;
import de.klinikum.lucene.LuceneServiceImpl;
import de.klinikum.persistence.SesameTripleStore;
import de.klinikum.service.implementation.ConceptServiceImpl;
import de.klinikum.service.implementation.NoteServiceImpl;
import de.klinikum.service.implementation.PatientServiceImpl;
import de.klinikum.service.interfaces.ConceptService;
import de.klinikum.service.interfaces.NoteService;
import de.klinikum.service.interfaces.PatientService;

/**
 * 
 * @author Carsten Meiser
 * 
 */
@RunWith(Arquillian.class)
public class NoteServiceTest {

    @Inject
    ConceptService conceptService;
    
    @Inject
    NoteService noteService;

    @Inject
    PatientService patientService;
    
    @Inject
    SesameTripleStore tripleStore;

    private Random generator = new Random(System.currentTimeMillis());

    private Patient patient;

    @Before
    public void createNewPatientWithAddress() throws Exception {
        Patient patient = new Patient();
        patient.setFirstName("Alice");
        patient.setLastName("Smith");
        patient.setDateOfBirth(new DateTime());
        patient.setPatientNumber(String.valueOf(this.generator.nextInt()));
        Address address = new Address(null, "Musterstr.", "1", "Musterstadt", "76123", "D", "110");
        patient.setAddress(address);
        patient = this.patientService.createPatientRDF(patient);
        this.setPatient(patient);
    }

    /**
	 */
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class).addClass(PatientServiceImpl.class)
                .addClass(ConceptServiceImpl.class).addClass(NoteServiceImpl.class).addClass(LuceneServiceImpl.class)
                .addClass(SesameTripleStore.class).addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    public void createNoteTest() throws IOException, URISyntaxException, TripleStoreException {
        Note note = new Note();
        String patientUri = this.patient.getUri();
        note.setPatientUri(patientUri);
        note.setPriority(3);
        note.setTitle("Hier koennte Ihr Titel stehen:");
        note.setText("Das ist eine Notiz");
        note = this.noteService.createNote(note);

        assertNotNull(note);
    }
    
    @Test
    public void getNotesTest() throws SpirontoException, IOException{
       List<Note> notes = this.noteService.getNotes(this.patient);
       
       assertNotNull(notes);
       
       for(Note note : notes){
           this.tripleStore.repositoryHasStatement(this.patient.getUri(), PATIENT_HAS_NOTE.toString(), note.getUri());          
       }
    }
    
    @Test
    public void addConceptToNote() throws IOException, URISyntaxException, RepositoryException, SpirontoException {
        Note note = new Note();
        String patientUri = this.patient.getUri();
        note.setPatientUri(patientUri);
        note.setPriority(3);
        note.setTitle("Hier koennte Ihr Titel stehen:");
        note.setText("Das ist eine Notiz");
        note = this.noteService.createNote(note);

        Concept concept = new Concept();
        concept.setEditable(true);
        concept.setLabel("Ein neues Concept");
        concept.setPatientUri(patientUri);

        concept = this.conceptService.createConcept(concept);

        this.noteService.addConceptToNote(note, concept);

        Note returnNote = this.noteService.getConceptsToNote(note);
        List<Concept> returnedConcepts = returnNote.getConcepts();

        assertNotNull(returnedConcepts);
    }

    @Test
    public void getNoteByUriTest() throws IOException, URISyntaxException, RepositoryException, SpirontoException {
        Note note = new Note();
        String patientUri = this.patient.getUri();
        note.setPatientUri(patientUri);
        note.setPriority(3);
        note.setTitle("Hier koennte ein weiterer Titel stehen:");
        note.setText("Das ist eine weitere Notiz");
        note = this.noteService.createNote(note);
        Note returnedNote = this.noteService.getNoteByUri(note.getUri());

        assertNotNull(returnedNote.getUri());
    }

    @Test
    public void getConceptsToNoteTest() {
        // tested in addConceptToNote
    }

    @Test
    public void updateNoteTest() throws IOException, URISyntaxException, SpirontoException {
        Note note = new Note();
        String patientUri = this.patient.getUri();
        note.setPatientUri(patientUri);
        note.setPriority(3);
        note.setTitle("Hier koennte Ihr Titel stehen:");
        note.setText("Das ist eine Notiz");
        note = this.noteService.createNote(note);

        note.setText("Neuer Text");
        Note updatedNote = this.noteService.updateNote(note);

        assertEquals(updatedNote.getText(), note.getText());
    }
    
    @Test
    public void noteExists() throws TripleStoreException, IOException, URISyntaxException{
        Note note = new Note();
        note.setPatientUri(this.patient.getUri());
        note.setPriority(3);
        note.setTitle("Hier koennte Ihr Titel stehen:");
        note.setText("Das ist eine Notiz");
        note = this.noteService.createNote(note);
        
        boolean exists = this.noteService.noteExists(note.getUri());
        
        assertTrue(exists);
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

}
