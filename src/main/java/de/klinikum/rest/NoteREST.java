package de.klinikum.rest;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.klinikum.domain.Concept;
import de.klinikum.domain.Patient;
import de.klinikum.domain.Note;
import de.klinikum.exceptions.SpirontoException;
import de.klinikum.helper.DateUtil;
import de.klinikum.service.Interfaces.NoteService;

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
        Timestamp tstamp = new Timestamp(new Date().getTime());
        note.setCreated(DateUtil.getBirthDateFromString(tstamp.toString()));
        note.setText("This is a wonderfull endless text about the most important patient N�11");
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
        return this.noteService.findNote(note.getUri());
    }

    /**
     * 
     * @param note -> Consumes an NoteObject from GUI- side
     * @return note
     * Purpose: Creates a Note. Returns created note with new URI in it
     * @throws IOException
     */
    @Path("/createNote")
    @POST
    @Produces(MediaType.APPLICATION_XML)
    public Note createNote(Note note) throws IOException {
        return this.noteService.createNote(note);
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
        return this.noteService.findNotes(patient);
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
    public Note addConceptToNote(Note note, Concept concept) throws IOException, SpirontoException {
        return this.noteService.addConceptToNote(note, concept);
    }
    
}