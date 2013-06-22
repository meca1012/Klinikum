package de.klinikum.service.interfaces;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import de.klinikum.domain.Concept;
import de.klinikum.domain.Note;
import de.klinikum.domain.Patient;
import de.klinikum.exceptions.SpirontoException;

public interface NoteService {
    /**
     * Creates a new Note for a patient.
     * 
     * @param note
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */

    Note createNote(Note note) throws IOException, URISyntaxException;

    /**
     * Returns a note to a patient by his uri.
     * 
     * @param patient
     * @return
     * @throws SpirontoException
     */
    List<Note> getNotes(Patient patient) throws SpirontoException;

    /**
     * Returns a note by an uri with all connected concepts
     * 
     * @param uri
     * @return
     * @throws SpirontoException
     */
    Note getNoteByUri(String uri) throws SpirontoException;

    /**
     * Connect a concept to an note.
     * 
     * @param note
     * @param concept
     * @return
     * @throws IOException
     */

    Note addConceptToNote(Note note, Concept concept) throws IOException;

    /**
     * Returns all concepts to an note.
     * 
     * @param note
     * @return
     * @throws SpirontoException 
     */

    Note getConceptsToNote(Note note) throws SpirontoException;

    /**
     * Updates an note. All existing links to other concepts are being removed and the new ones are set. The
     * connectedConcepts themselves are ignored.
     * 
     * @param note
     * @return
     * @throws SpirontoException
     * @throws IOException
     */

    Note updateNote(Note note) throws SpirontoException, IOException;
}
