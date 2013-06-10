package de.klinikum.service.Implementation;

import static de.klinikum.domain.NameSpaces.NOTE_HAS_DATE;
import static de.klinikum.domain.NameSpaces.NOTE_HAS_TEXT;
import static de.klinikum.domain.NameSpaces.NOTE_POINTS_TO_CONCEPT;
import static de.klinikum.domain.NameSpaces.NOTE_TYPE;
import static de.klinikum.domain.NameSpaces.PATIENT_HAS_NOTE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.openrdf.model.Literal;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.vocabulary.RDF;

import de.klinikum.domain.Concept;
import de.klinikum.domain.Note;
import de.klinikum.domain.Patient;
import de.klinikum.exceptions.SpirontoException;
import de.klinikum.helper.DateUtil;
import de.klinikum.persistence.SesameTripleStore;
import de.klinikum.service.Interfaces.NoteService;

public class NoteServiceImpl implements NoteService {

    @Inject
    SesameTripleStore tripleStore;

    @Inject
    ConceptServiceImpl conceptService;

    @Override
    public Note createNote(Note note) throws IOException {

        URI noteUri = this.tripleStore.getUniqueURI(NOTE_TYPE.toString());
        URI noteTypeUri = this.tripleStore.getValueFactory().createURI(NOTE_TYPE.toString());

        note.setUri(noteUri.toString());

        this.tripleStore.addTriple(noteUri, RDF.TYPE, noteTypeUri);
        this.tripleStore.addTriple(note.getPatientUri(), PATIENT_HAS_NOTE.toString(), note.getUri());

        Date date = new Date(System.currentTimeMillis());
        note.setCreated(DateUtil.getBirthDateFromString(date.toString()));
        Literal createdLiteral = this.tripleStore.getValueFactory().createLiteral(note.getCreated());
        Literal textLiteral = this.tripleStore.getValueFactory().createLiteral(note.getText());

        URI hasDateUri = this.tripleStore.getValueFactory().createURI(NOTE_HAS_DATE.toString());
        URI hasTextUri = this.tripleStore.getValueFactory().createURI(NOTE_HAS_TEXT.toString());

        this.tripleStore.addTriple(noteUri, hasDateUri, createdLiteral);
        this.tripleStore.addTriple(noteUri, hasTextUri, textLiteral);

        if (note.getConcepts() != null) {
            for (Concept c : note.getConcepts()) {
                if (!this.conceptService.conceptExists(c)) {
                    this.conceptService.addConceptToPatient(c);
                }
                this.tripleStore.addTriple(note.getUri(), NOTE_POINTS_TO_CONCEPT.toString(), c.getUri());
            }
        }
        return note;
    }

    @Override
    public Note addConceptToNote(Note note, Concept concept) throws IOException {
        if (this.tripleStore.repositoryHasStatement(note.getUri(), NOTE_POINTS_TO_CONCEPT.toString(), concept.getUri())) {
            return null;
        }
        if (note.getConcepts() == null) {
            note.setConcepts(new ArrayList<Concept>());
        }
        note.addConcept(concept);
        this.tripleStore.addTriple(note.getUri(), NOTE_POINTS_TO_CONCEPT.toString(), concept.getUri());
        return note;
    }

    @Override
    public List<Note> findNotes(Patient patient) throws SpirontoException {

        List<Note> notes = new ArrayList<Note>();

        String sparqlQuery = "SELECT ?Uri ?text ?created WHERE {";

        sparqlQuery += "<" + patient.getUri().toString() + "> <" + PATIENT_HAS_NOTE + "> ?Uri . ";

        sparqlQuery += "?Uri <" + RDF.TYPE + "> <" + NOTE_TYPE + "> . ";

        sparqlQuery += "?Uri <" + NOTE_HAS_TEXT + "> ?text . ";

        sparqlQuery += "?Uri <" + NOTE_HAS_DATE + "> ?created }";

        Set<HashMap<String, Value>> queryResult = this.tripleStore.executeSelectSPARQLQuery(sparqlQuery);

        for (HashMap<String, Value> item : queryResult) {
            Note note = new Note();
            note.setUri(item.get("Uri").toString());
            note.setPatientUri(patient.getUri());
            note.setCreated(DateUtil.getBirthDateFromString(item.get("created").stringValue()));
            note.setText(item.get("text").stringValue());
            notes.add(note);
        }

        return notes;
    }

    @Override
    public Note findNote(String noteUri) throws SpirontoException {

        Note note = new Note();
        note.setUri(noteUri);

        String sparqlQuery = "SELECT ?text ?patientUri ?created WHERE {";

        sparqlQuery += "<" + noteUri + "> <" + NOTE_HAS_TEXT + "> ?text . ";
        sparqlQuery += "<" + noteUri + "> <" + NOTE_HAS_DATE + "> ?created . ";
        sparqlQuery += "?patientUri <" + PATIENT_HAS_NOTE + "> <" + noteUri + ">}";

        Set<HashMap<String, Value>> queryResult = this.tripleStore.executeSelectSPARQLQuery(sparqlQuery);
        for (HashMap<String, Value> item : queryResult) {
            note.setText(item.get("text").stringValue());
            note.setCreated(DateUtil.getBirthDateFromString(item.get("created").stringValue()));
            note.setPatientUri(item.get("patientUri").toString());
        }
        return note;
    }
}
