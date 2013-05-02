package main.java.de.klinikum.appserver.communication.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/patient/")
@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML,
		MediaType.APPLICATION_JSON })
@Consumes
public class PatientREST {

	@Path("test")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String test() {
		return "Yea! ";
	}

	@Path("test2")
	@POST
	@Produces(MediaType.APPLICATION_XML)
	public String newPatient() {
		return "This should be a patient!";
	}

}
