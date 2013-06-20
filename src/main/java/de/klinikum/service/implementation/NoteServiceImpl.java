package de.klinikum.service.implementation;

import static de.klinikum.domain.NameSpaces.NOTE_HAS_DATE;
import static de.klinikum.domain.NameSpaces.NOTE_HAS_PRIORITY;
import static de.klinikum.domain.NameSpaces.NOTE_HAS_TEXT;
import static de.klinikum.domain.NameSpaces.NOTE_HAS_TITLE;
import static de.klinikum.domain.NameSpaces.NOTE_POINTS_TO_CONCEPT;
import static de.klinikum.domain.NameSpaces.NOTE_TYPE;
import static de.klinikum.domain.NameSpaces.PATIENT_HAS_NOTE;
import static de.klinikum.domain.NameSpaces.PATIENT_TYPE;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.joda.time.DateTime;
import org.openrdf.model.Model;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.repository.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.klinikum.domain.Concept;
import de.klinikum.domain.Note;
import de.klinikum.domain.Patient;
import de.klinikum.exceptions.SpirontoException;
import de.klinikum.helper.DateUtil;
import de.klinikum.lucene.LuceneServiceImpl;
import de.klinikum.persistence.SesameTripleStore;
import de.klinikum.service.interfaces.ConceptService;
import de.klinikum.service.interfaces.NoteService;

@Named
public class NoteServiceImpl implements NoteService {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(NoteServiceImpl.class);

    @Inject
    SesameTripleStore tripleStore;

    @Inject
    ConceptService conceptService;

    @Inject
    LuceneServiceImpl luceneService;
       
    @Override
    public Note createNote(Note note) throws IOException, URISyntaxException {
        
        if (note.getPatientUri() == null) {
            return null;
        }
        
        if (!this.tripleStore.repositoryHasStatement(note.getPatientUri(), RDF.TYPE.toString(), PATIENT_TYPE.toString())) {
            return null;
        }

        URI noteUri = this.tripleStore.getUniqueURI(NOTE_TYPE.toString());

        note.setUri(noteUri.toString());
        note.setCreated(new DateTime(System.currentTimeMillis()));

        this.tripleStore.addTriple(noteUri.toString(), RDF.TYPE.toString(), NOTE_TYPE.toString());
        this.tripleStore.addTriple(note.getPatientUri(), PATIENT_HAS_NOTE.toString(), note.getUri());
        
        this.tripleStore.addTripleWithStringLiteral(noteUri.toString(), NOTE_HAS_DATE.toString(), note.getCreated().toString());
        this.tripleStore.addTripleWithStringLiteral(noteUri.toString(), NOTE_HAS_TEXT.toString(), note.getText());
        this.tripleStore.addTripleWithStringLiteral(noteUri.toString(), NOTE_HAS_TITLE.toString(), note.getTitle());
        this.tripleStore.setValue(noteUri.toString(), NOTE_HAS_PRIORITY.toString(), note.getPriority());
        
        try {
            luceneService.storeNote(note);
        }
        catch (Exception e) {
            LOGGER.warn("Note could not been stored to LuceneIndex");
            LOGGER.warn(e.toString());
        }
        
        return note;
    }

    @Override
    public Note addConceptToNote(Note note, Concept concept) throws IOException {
        if (this.tripleStore.repositoryHasStatement(note.getUri(), NOTE_POINTS_TO_CONCEPT.toString(), concept.getUri())) {
            return null;
        }        
        this.tripleStore.addTriple(note.getUri(), NOTE_POINTS_TO_CONCEPT.toString(), concept.getUri());
        return note;
    }

    @Override
    public List<Note> getNotes(Patient patient) throws SpirontoException {

        List<Note> notes = new ArrayList<Note>();
        Model statementList;
        
        try {
            statementList = this.tripleStore.getStatementList(patient.getUri(), PATIENT_HAS_NOTE.toString(), null);
            for (Statement noteStatement : statementList) {
                notes.add(getNoteByUri(noteStatement.getObject().toString()));
            }
        }
        catch (RepositoryException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (SpirontoException e) {
            e.printStackTrace();
        }
        return notes;
    }

    @Override
    public Note getNoteByUri(String noteUri) throws SpirontoException {

        Note note = new Note();
        note.setUri(noteUri);

        String sparqlQuery = "SELECT ?text ?patientUri ?created ?title ?priority WHERE {";

        sparqlQuery += "<" + noteUri + "> <" + NOTE_HAS_TEXT + "> ?text . ";
        sparqlQuery += "<" + noteUri + "> <" + NOTE_HAS_DATE + "> ?created . ";
        sparqlQuery += "<" + noteUri + "> <" + NOTE_HAS_TITLE + "> ?title . ";
        sparqlQuery += "<" + noteUri + "> <" + NOTE_HAS_PRIORITY + "> ?priority . ";
        sparqlQuery += "?patientUri <" + PATIENT_HAS_NOTE + "> <" + noteUri + ">}";

        Set<HashMap<String, Value>> queryResult = this.tripleStore.executeSelectSPARQLQuery(sparqlQuery);
        for (HashMap<String, Value> item : queryResult) {
            note.setText(item.get("text").stringValue());
            note.setCreated(DateUtil.getDateTimeFromString(item.get("created").stringValue()));
            note.setPatientUri(item.get("patientUri").stringValue());
            note.setTitle(item.get("title").stringValue());
            note.setPriority(Integer.parseInt(item.get("priority").stringValue()));
        }        
        note = getConceptsToNote(note);
        return note;
    }
    
    @Override
    public Note getConceptsToNote(Note note) {
        
        Model statementList;        
       
        try {
            statementList = this.tripleStore.getStatementList(note.getUri(), NOTE_POINTS_TO_CONCEPT.toString(), null);
            for (Statement conceptStatement : statementList) {
                note.addConcept(this.conceptService.getConceptByUri(conceptStatement.getObject().toString()));
            }
        }
        catch (RepositoryException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (SpirontoException e) {
            e.printStackTrace();
        }
        return note;
    }
    
    @Override
    public Note updateNote(Note note) throws SpirontoException, IOException {
        
        if (!this.tripleStore.repositoryHasStatement(note.getUri(),RDF.TYPE.toString() , NOTE_TYPE.toString())) {
            return null;
        }
        
        Note existingNote = getNoteByUri(note.getUri());       
        
        if (!note.getTitle().equals(existingNote.getTitle())) {
            this.tripleStore.removeTriples(existingNote.getUri(), NOTE_HAS_TITLE.toString(), null);
            this.tripleStore.addTripleWithStringLiteral(note.getUri(), NOTE_HAS_TITLE.toString(), note.getTitle());
        }
        
        if (!note.getText().equals(existingNote.getText())) {
            this.tripleStore.removeTriples(existingNote.getUri(), NOTE_HAS_TEXT.toString(), null);
            this.tripleStore.addTripleWithStringLiteral(note.getUri(), NOTE_HAS_TEXT.toString(), note.getText());
        }
        
        if (note.getPriority() != existingNote.getPriority()) {
            this.tripleStore.removeTriples(existingNote.getUri(), NOTE_HAS_PRIORITY.toString(), null);
            this.tripleStore.addTriple(note.getUri(), NOTE_HAS_PRIORITY.toString(), note.getPriority());
        }
        
        if (note.getConcepts() != null) {
            
            existingNote.setConcepts(getConceptsToNote(existingNote).getConcepts());
            
            if (existingNote.getConcepts() != null) {
                for (Concept ec : existingNote.getConcepts()) {
                    this.tripleStore.removeTriples(existingNote.getUri(), NOTE_POINTS_TO_CONCEPT.toString(), ec.getUri());               
                }
            }
            
            for (Concept c : note.getConcepts()) {
                this.tripleStore.addTriple(note.getUri(), NOTE_POINTS_TO_CONCEPT.toString(), c.getUri());
            }            
        }        
        return note;
    }
}
