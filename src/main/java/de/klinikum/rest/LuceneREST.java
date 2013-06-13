package de.klinikum.rest;

import java.io.IOException;
import java.net.URISyntaxException;
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
import org.apache.lucene.queryparser.classic.ParseException;

import de.klinikum.domain.Note;
import de.klinikum.lucene.LuceneServiceImpl;
import de.klinikum.service.Interfaces.ConceptService;

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
    
    
  //CDI of ConceptService.class
    @Inject
    LuceneServiceImpl luceneService;
    
    /**
     * Purpose: Create Lucene Index Files for Test purpose
     * @return
     * @throws IOException
     * @throws URISyntaxException 
     */
    @Path("/luceneCreateIndex")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String test2() throws IOException, URISyntaxException
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
        luceneService.storeNote(note);
        return "Done";
    }
    
    @Path("/searchNote")
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String storeNote(String searchString) throws ParseException, IOException
    {
                List<String> returnList = luceneService.search(searchString);
                return returnList.toString();
    }
    
}
