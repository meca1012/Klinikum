package de.klinikum.rest;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.openrdf.model.Value;

import de.klinikum.exceptions.SpirontoException;
import de.klinikum.helper.AppStartup;
import de.klinikum.service.interfaces.SesameService;

/**
 * 
 * ConceptREST.java
 * Purpose: REST- Connection- Points for UI
 * Supports direct SPARQL Queries
 * 
 * @author  Spironto Team 1
 * @version 1.0 08/06/13
 */

@Path("/sparql")
@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML,
		MediaType.APPLICATION_JSON })
@Consumes
@RequestScoped
public class SesameREST {

    //CDI for SesameService
	@Inject
	SesameService SesameService;
	
	//CDI for Appstartup  initialization bootstrap to load Data 
	@Inject
	AppStartup startup;

	/**
	 * 
	 * @param String -> Consumes an StringObject from GUI- side
	 * Purpose: Execute given String directly in Sesamestore
	 * @return Depence on Result, Return of Hashmap not yet possible
	 */
	@POST
	@Path("/executequery")
	@Consumes(MediaType.TEXT_PLAIN)
	public Set<HashMap<String, Value>> executeSPARQLQuery(String queryString) {
		try {
			return this.SesameService.executeSPARQLQuery(queryString);
		} catch (SpirontoException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Creates Testdata on call. 
	 * @throws URISyntaxException 
	 */
	@GET
	@Path("/createTestData")
	@Produces(MediaType.TEXT_PLAIN)
	public void createTestData() throws URISyntaxException {
	    try {
            this.startup.createTestData();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }        
	}
}
