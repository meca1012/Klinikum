package de.klinikum.communication;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.klinikum.domain.Patient;
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
	    public TextBlock getTextBlockXML() throws ParseException {
			
			String dateString = "01/08/2013";
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			
			TextBlock textBlock = new TextBlock();
			textBlock.setUri("http://spironto.de/spironto#textBlock-gen3");
			textBlock.setPatientUri("http://spironto.de/spironto#patient-gen11");
			textBlock.setCreated(formatter.parse(dateString));
			textBlock.setText("This is a wonderfull endless text about the most important patient N°11");
			textBlock.setConcepts(null);
			return textBlock;
		}
		
		@Path("/getTextBlockByUri")
	    @POST
	    @Produces(MediaType.APPLICATION_XML)
	    public TextBlock getTextBlock(TextBlock textBlock) throws IOException{
			return this.textBlockService.findTextBlock(textBlock.getUri());
		}	  
		
		@Path("/createTextBlock")
	    @POST
	    @Produces(MediaType.APPLICATION_XML)
	    public TextBlock createTextBlock(TextBlock textBlock) throws IOException {
			return this.textBlockService.createTextBlock(textBlock);
		}

		@Path("/getTextBlocks")
	    @POST
	    @Produces(MediaType.APPLICATION_XML)
	    public List<TextBlock> getTextBlocks(Patient patient) throws IOException {
			return this.textBlockService.findTextBlocks(patient);
		}
		
}
