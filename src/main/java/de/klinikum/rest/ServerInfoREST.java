package de.klinikum.rest;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.klinikum.lucene.LuceneServiceImpl;

/**
 * 
 * ServerInfoREST.java
 * Purpose: REST- Connection- Points for ServerInfos
 * Return Info if Webservice is running
 * 
 * @author  Spironto Team 1
 * @version 1.0 08/06/13
 */

@Path("/server")
@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML,
		MediaType.APPLICATION_JSON })
@Consumes
@RequestScoped
public class ServerInfoREST {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ServerInfoREST.class);
	
	//CDI for LuceneService
    @Inject
    LuceneServiceImpl luceneService;
    
    /**
     * 
     * @return Returns serverstate
     */
    @Path("/restInfo")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String test() {
    	String status = "REST- Service Running";
    	LOGGER.info(status);
		return status;
	}
    
}
