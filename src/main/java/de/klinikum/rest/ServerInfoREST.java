package de.klinikum.rest;

import java.io.IOException;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.klinikum.lucene.LuceneServiceImpl;


@Path("/server")
@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML,
		MediaType.APPLICATION_JSON })
@Consumes
@RequestScoped
public class ServerInfoREST {
	
    @Inject
    LuceneServiceImpl luceneService;
    
    @Path("/restInfo")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String test() {
		return "REST- Service Running";
	}
    
	@Path("/luceneTest")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String test2() throws IOException
	{
	    luceneService.createIndex();
	    return "Done";
	}
}
