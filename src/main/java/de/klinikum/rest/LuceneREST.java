package de.klinikum.rest;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.lucene.index.IndexWriterConfig.OpenMode;

import de.klinikum.domain.LuceneSearchRequest;
import de.klinikum.domain.Note;
import de.klinikum.lucene.LuceneService;
import de.klinikum.service.interfaces.NoteService;
/**
 * 
 * LuceneREST.java
 * Purpose: REST- Connection- Points for Lucene Fulltext Search
 * 
 * @author  Spironto Team 1
 * @version 1.0 08/06/13
 */

@Path("/lucene")
@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML, MediaType.APPLICATION_JSON })
@Consumes
@Stateless
public class LuceneREST {
    
    @Inject
    LuceneService luceneService;
    
    @Inject
    NoteService noteService;
    
    /**
     * Purpose: Create LuceneSearchRequestElement for Testing
     * @return
     * @throws IOException
     * @throws URISyntaxException 
     */
    @Path("/getRequestXML")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public LuceneSearchRequest returnRequest()
    {
        LuceneSearchRequest request = new LuceneSearchRequest();
        request.setPatientUri("http://spironto.de/spironto#patient-gen1");
        request.setSearchString("endless");
        return request;
   }
    
    /**
     * Purpose: Create Lucene Index Files for Test purpose
     * @return
     * @throws Exception 
     */
    @Path("/luceneCreateIndex")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String test2() throws Exception
    {
        luceneService.initalizeWriter(OpenMode.CREATE);
        return "Done";
    }
    
    /**
     * Purpose: Adds Note as Document to Lucene
     * @return
     * @throws IOException
     * @throws URISyntaxException 
     */
    @Path("/storeNote")
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.TEXT_PLAIN)
    public String storeNote(Note note) throws IOException, URISyntaxException
    {
        try {
            luceneService.storeNote(note);
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "Done";
    }
    
    /**
     * 
     * @param String -> Consumes an LuceneSearchRequestObejct from GUI- side
     * @return List of Notes search in LuceneIndex Folder -> Datafetch in RDF with URIs returned
     * from Index
     * @throws Exception 
     */
    @Path("/searchNote")
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public List<Note> searchNote(LuceneSearchRequest request) throws Exception
    {
                List<String> uriList = luceneService.searchNotes(request);
                List<Note> noteResult = new ArrayList<Note>();
                for(String uri : uriList)
                {
                    noteResult.add(noteService.getNoteByUri(uri));
                }
                return noteResult;
    }
        
}
 
