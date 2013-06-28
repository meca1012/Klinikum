package de.klinikum.rest;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.joda.time.DateTime;
import org.openrdf.model.util.ModelException;
import org.openrdf.repository.RepositoryException;

import de.klinikum.domain.Concept;
import de.klinikum.domain.Note;
import de.klinikum.domain.Patient;
import de.klinikum.exceptions.SpirontoException;
import de.klinikum.exceptions.TripleStoreException;
import de.klinikum.service.interfaces.ConceptService;
import de.klinikum.service.interfaces.NoteService;

/**
 * 
 * ConceptREST.java Purpose: REST- Connection- Points for UI For Sesame Ontologie notes Stores CareTeams documentation
 * 
 * @author Carsten Meiser, Andreas Schillinger, Matthias Schwarzenbach
 * @version 1.0 08/06/13
 */
@Path("/note")
@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML, MediaType.APPLICATION_JSON })
@Consumes
@Stateless
public class NoteREST {

    // CDI for NoteService.class
    @Inject
    NoteService noteService;

    @Inject
    ConceptService conceptService;

    /**
     * 
     * @return Returns a standard XML- Parse of an Note.class
     * @throws ParseException
     */

    @Path("/getNoteXML")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Note getNoteXML() throws ParseException {

        Note note = new Note();
        note.setUri("http://spironto.de/spironto#note-gen3");
        note.setPatientUri("http://spironto.de/spironto#patient-gen11");
        note.setCreated(new DateTime());
        note.setText("This is a wonderfull endless text about the most important patient N°11");
        note.setTitle("endless important");
        note.setPriority(1);
        note.setConcepts(null);
        return note;
    }

    /**
     * 
     * @param note
     *            -> Consumes an NoteObject from GUI- side
     * @return note Purpose: Returns a NoteObject searched by URI Mostly used for fetching data
     * @throws IOException
     * @throws SpirontoException
     * @throws RepositoryException
     */
    @Path("/getNoteByUri")
    @POST
    @Produces(MediaType.APPLICATION_XML)
    public Note getNote(Note note) throws IOException, SpirontoException, RepositoryException {

        try {
            return this.noteService.getNoteByUri(note.getUri());
        }
        catch (TripleStoreException e) {
            e.printStackTrace();
            throw new SpirontoException("Error getting note: " + e);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new SpirontoException("Error getting note: " + e.toString(), e);
        }
    }

    /**
     * 
     * @param note
     *            -> Consumes an NoteObject from GUI- side
     * @return note Purpose: Creates a Note. Returns created note with new URI in it
     * @throws IOException
     * @throws SpirontoException
     * @throws ModelException
     * @throws RepositoryException
     * @throws URISyntaxException
     */
    @Path("/createNote")
    @POST
    @Produces(MediaType.APPLICATION_XML)
    public Note createNote(Note note) throws IOException, RepositoryException, ModelException, SpirontoException,
            URISyntaxException {

        try {
            note = this.noteService.createNote(note);

            if (note == null) {
                return null;
            }

            if (note.getConcepts() != null) {
                for (Concept c : note.getConcepts()) {
                    if (c.getUri() == null) {
                        c = this.conceptService.createConcept(c);
                    }
                    note = this.noteService.addConceptToNote(note, c);
                    if (c.getConnectedConcepts() != null) {
                        for (Concept con : c.getConnectedConcepts()) {
                            if (con.getUri() == null) {
                                con = this.conceptService.createConcept(con);
                            }
                            else {
                                con = this.conceptService.getConceptByUri(con.getUri());
                            }
                            this.conceptService.connectSingleConcept(c, con);
                        }
                    }
                    c.setConnectedConcepts(this.conceptService.getConnectedConceptUris(c));
                }
            }
            return note;
        }
        catch (TripleStoreException e) {
            e.printStackTrace();
            throw new SpirontoException("Error creating note: " + e);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new SpirontoException("Error creating note: " + e.toString(), e);
        }

    }

    /**
     * 
     * @param patient
     *            --> Consumes an PatienObject from GUI- side
     * @return Collection of notes Purpose: Gets all linked Note to given Patient
     * @throws IOException
     * @throws SpirontoException
     */
    @Path("/getNotes")
    @POST
    @Produces(MediaType.APPLICATION_XML)
    public List<Note> getNotes(Patient patient) throws IOException, SpirontoException {
        try {
            if (patient.getUri() == null) {
                return null;
            }

            return this.noteService.getNotes(patient);
        }
        catch (TripleStoreException e) {
            e.printStackTrace();
            throw new SpirontoException("Error getting note: " + e);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new SpirontoException("Error getting note: " + e.toString(), e);
        }
    }

    /**
     * 
     * @param note
     *            -> Consumes an NoteObject from GUI- side
     * @param concept
     *            -> Consumes an ConceptObject from GUI- side
     * @return Purpose: Connects given Concept with given note
     * @throws IOException
     * @throws SpirontoException
     * @throws RepositoryException
     */
    @Path("/addConceptToNote")
    @POST
    @Produces(MediaType.APPLICATION_XML)
    public Note addConceptToNote(Note note) throws IOException, SpirontoException, RepositoryException {

        try {
            if (note.getConcepts() != null) {
                for (Concept c : note.getConcepts()) {
                    if (c.getUri() != null) {
                        note = this.noteService.addConceptToNote(note, c);
                    }
                }
                note = this.noteService.getConceptsToNote(note);
            }
            else {
                return null;
            }
            return note;
        }
        catch (TripleStoreException e) {
            e.printStackTrace();
            throw new SpirontoException("Error getting note: " + e);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new SpirontoException("Error getting note: " + e.toString(), e);
        }
    }

    @Path("/updateNote")
    @POST
    @Produces(MediaType.APPLICATION_XML)
    public Note updateNote(Note note) throws IOException, RepositoryException, ModelException, SpirontoException {

        try {
            if (note != null) {
                if (note.getConcepts() != null) {
                    for (Concept c : note.getConcepts()) {
                        c.setConnectedConcepts(null);
                        if (c.getUri() == null) {
                            c = this.conceptService.createConcept(c);
                        }
                        c = this.conceptService.updateConcept(c);
                    }
                }
                note = this.noteService.updateNote(note);
                if (note == null) {
                    return null;
                }
            }
            else {
                return null;
            }
            return note;
        }
        catch (TripleStoreException e) {
            e.printStackTrace();
            throw new SpirontoException("Error updating note: " + e);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new SpirontoException("Error updating note: " + e.toString(), e);
        }
    }

    @Path("/getConceptsToNote")
    @POST
    @Produces(MediaType.APPLICATION_XML)
    public Note getConceptsToNote(Note note) throws SpirontoException, RepositoryException, IOException {

        try {
            if (note != null) {
                if (note.getUri() != null) {
                    note = this.noteService.getConceptsToNote(note);
                    if (note != null) {
                        return note;
                    }
                }
            }
            return null;
        }
        catch (TripleStoreException e) {
            e.printStackTrace();
            throw new SpirontoException("Error getting note: " + e);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new SpirontoException("Error getting note: " + e.toString(), e);
        }

    }
}
