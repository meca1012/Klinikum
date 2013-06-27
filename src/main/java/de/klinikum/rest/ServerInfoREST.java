package de.klinikum.rest;

import java.io.IOException;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.klinikum.lucene.LuceneService;
import de.klinikum.service.interfaces.SesameService;

/**
 * 
 * ServerInfoREST.java Purpose: REST- Connection- Points for ServerInfos Return Info if Webservice is running
 * 
 * @author Carsten Meiser, Constantin Treiber
 * @version 1.0 08/06/13
 */

@Path("/server")
@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML, MediaType.APPLICATION_JSON })
@Consumes
@RequestScoped
public class ServerInfoREST {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerInfoREST.class);

    // CDI for LuceneService
    @Inject
    LuceneService luceneService;

    @Inject
    SesameService SesameService;

    /**
     * 
     * @return Returns serverstate
     */
    @Path("/restInfo")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String restInfo() {
        String status = "REST- Service Running";
        LOGGER.info(status);
        return status;
    }

    /**
     * Changes Language-Settings on ServerSide
     * 
     * @throws IOException
     */
    @Path("/changeLanguageSetting/{language}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Response changeLanguageSetting(@PathParam("language") String language) throws IOException {
        try {
            if(this.SesameService.changeLanguageSetting(language))
                {
                return Response.status(Response.Status.OK).build();
                }
            else
            {
                return Response.status(Response.Status.NOT_MODIFIED).build();
            }
               
        }
        catch (IOException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_MODIFIED).build();
        }
    }
}
