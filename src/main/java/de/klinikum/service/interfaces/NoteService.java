package de.klinikum.service.interfaces;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import de.klinikum.domain.Concept;
import de.klinikum.domain.Note;
import de.klinikum.domain.Patient;
import de.klinikum.exceptions.SpirontoException;

public interface NoteService {

    Note createNote(Note note) throws IOException, URISyntaxException;

    List<Note> findNotes(Patient patient) throws SpirontoException;

    Note getNoteByUri(String uri) throws SpirontoException;

    Note addConceptToNote(Note note, Concept concept) throws IOException;

    Note getConceptsToNote(Note note);
    
    Note updateNote(Note note) throws SpirontoException, IOException;
}
