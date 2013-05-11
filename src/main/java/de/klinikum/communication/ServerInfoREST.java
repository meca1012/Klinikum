package de.klinikum.communication;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/server")
@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML,
		MediaType.APPLICATION_JSON })
@Consumes
@RequestScoped
public class ServerInfoREST {
	@Path("/restInfo")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String test() {
		return "REST- Service Running";
	}
}
