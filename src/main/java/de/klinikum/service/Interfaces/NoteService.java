package de.klinikum.service.Interfaces;

import java.io.IOException;
import java.util.List;

import de.klinikum.domain.Concept;
import de.klinikum.domain.Note;
import de.klinikum.domain.Patient;
import de.klinikum.exceptions.SpirontoException;

public interface NoteService {

    Note createNote(Note note) throws IOException;

    List<Note> findNotes(Patient patient) throws SpirontoException;

    Note findNote(String uri) throws SpirontoException;

    Note addConceptToNote(Note note, Concept concept) throws IOException;

}