package de.klinikum.communication;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.openrdf.model.Value;

import de.klinikum.exceptions.SpirontoException;
import de.klinikum.service.SesameServiceImpl;

@Path("/sparql")
@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML, MediaType.APPLICATION_JSON })
@Consumes
@RequestScoped
public class SesameRest {

    @Inject
    private SesameServiceImpl SesameService;

    @POST
    @Path("/executequery")
    @Consumes(MediaType.TEXT_PLAIN)
    public Set<HashMap<String, Value>> executeSPARQLQuery(String queryString) {
    	try{
        return this.SesameService.executeSPARQLQuery(queryString);}catch(SpirontoException e)
        {e.printStackTrace();}
    }

}
