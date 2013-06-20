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
import de.klinikum.service.interfaces.ConceptService;
import de.klinikum.service.interfaces.NoteService;

/**
 * 
 * ConceptREST.java
 * Purpose: REST- Connection- Points for UI 
 * For Sesame Ontologie notes
 * Stores CareTeams documentation
 * 
 * @author  Spironto Team 1
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
     * @param note -> Consumes an NoteObject from GUI- side
     * @return note
     * Purpose: Returns a NoteObject searched by URI
     * Mostly used for fetching data
     * @throws IOException
     * @throws SpirontoException
     */
    @Path("/getNoteByUri")
    @POST
    @Produces(MediaType.APPLICATION_XML)
    public Note getNote(Note note) throws IOException, SpirontoException {
        return this.noteService.getNoteByUri(note.getUri());
    }

    /**
     * 
     * @param note -> Consumes an NoteObject from GUI- side
     * @return note
     * Purpose: Creates a Note. Returns created note with new URI in it
     * @throws IOException
     * @throws SpirontoException 
     * @throws ModelException 
     * @throws RepositoryException 
     * @throws URISyntaxException 
     */
    @Path("/createNote")
    @POST
    @Produces(MediaType.APPLICATION_XML)
    public Note createNote(Note note) throws IOException, RepositoryException, ModelException, SpirontoException, URISyntaxException {
        note = this.noteService.createNote(note);
        
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
                        } else {
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

    /**
     * 
     * @param patient --> Consumes an PatienObject from GUI- side
     * @return Collection of notes
     * Purpose: Gets all linked Note to given Patient
     * @throws IOException
     * @throws SpirontoException
     */
    @Path("/getNotes")
    @POST
    @Produces(MediaType.APPLICATION_XML)
    public List<Note> getNotes(Patient patient) throws IOException, SpirontoException {
        return this.noteService.getNotes(patient);
    }
    
    /**
     * 
     * @param note -> Consumes an NoteObject from GUI- side
     * @param concept -> Consumes an ConceptObject from GUI- side
     * @return
     * Purpose: Connects given Concept with given note
     * @throws IOException
     * @throws SpirontoException
     */
    @Path("/addConceptToNote")
    @POST
    @Produces(MediaType.APPLICATION_XML)
    public Note addConceptToNote(Note note) throws IOException, SpirontoException {
        
        if (note.getConcepts() != null) {
            for (Concept c : note.getConcepts()) {
                if (c.getUri() != null) {
                    note = this.noteService.addConceptToNote(note, c);
                }
            }
            note = this.noteService.getConceptsToNote(note);
        } else {
            return null;
        }
        return note;
    }
    
    @Path("/updateNote")
    @POST
    @Produces(MediaType.APPLICATION_XML)
    public Note updateNote(Note note) throws IOException, RepositoryException, ModelException, SpirontoException {
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
        } else {
            return null;
        }
        return note;
    }
    
}
