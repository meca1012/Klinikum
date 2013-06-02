package de.klinikum.communication;

import java.io.IOException;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.klinikum.domain.TextBlock;
import de.klinikum.service.TextBlockService;

/**
 * 
 */
@Path("/textBlock")
@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML, MediaType.APPLICATION_JSON })
@Consumes
@Stateless
public class TextBlockREST {
	
	@Inject
	TextBlockService textBlockService;
	
		@Path("/getTextBlockXML")
	    @GET
	    @Produces(MediaType.APPLICATION_XML)
	    public TextBlock getTextBlockXML() {
			return null;
		}
		
		@Path("/getTextBlock")
	    @GET
	    @Produces(MediaType.APPLICATION_XML)
	    public TextBlock getTextBlock(){
			return this.textBlockService.findTextBlock();
		}
		
		@Path("/createTextBlock")
	    @POST
	    @Produces(MediaType.APPLICATION_XML)
	    public TextBlock createTextBlock(TextBlock textBlock) throws IOException {
			return this.textBlockService.createTextBlock(textBlock);
		}

}
