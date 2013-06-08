package de.klinikum.rest;

import java.io.IOException;
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
import de.klinikum.service.SesameServiceImpl;

@Path("/sparql")
@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML,
		MediaType.APPLICATION_JSON })
@Consumes
@RequestScoped
public class SesameREST {

	@Inject
	SesameServiceImpl SesameService;
	
	@Inject
	AppStartup startup;

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
	
	@GET
	@Path("/createTestData")
	@Produces(MediaType.TEXT_PLAIN)
	public void createTestData() {
	    try {
            this.startup.createTestData();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }        
	}
}
