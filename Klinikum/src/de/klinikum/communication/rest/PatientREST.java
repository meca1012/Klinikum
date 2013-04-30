package de.klinikum.communication.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/patient")
@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML, MediaType.APPLICATION_JSON })
@Consumes
public class PatientREST {

	@Path( "/test" )
	public class MessageResource
	{
	  @GET
	  @Produces( MediaType.TEXT_PLAIN )
	  public String message()
	  {
	    return "Yea! ";
	  }
	}
	
}
