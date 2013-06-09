package de.klinikum.service.Interfaces;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.klinikum.domain.Concept;
import de.klinikum.domain.Patient;
import de.klinikum.domain.TextBlock;
import de.klinikum.exceptions.SpirontoException;
import de.klinikum.helper.DateUtil;

/**
 * 
 * ConceptREST.java
 * Purpose: REST- Connection- Points for UI 
 * For Sesame Ontologie textBlocks
 * Stores CareTeams documentation
 * 
 * @author  Spironto Team 1
 * @version 1.0 08/06/13
 */
@Path("/textBlock")
@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML, MediaType.APPLICATION_JSON })
@Consumes
@Stateless
public class TextBlockREST {

    // CDI for TextBlockService.class
    @Inject
    TextBlockService textBlockService;

    /**
     * 
     * @return Returns a standard XML- Parse of an TextBlock.class 
     * @throws ParseException
     */
    
    @Path("/getTextBlockXML")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public TextBlock getTextBlockXML() throws ParseException {

        TextBlock textBlock = new TextBlock();
        textBlock.setUri("http://spironto.de/spironto#textBlock-gen3");
        textBlock.setPatientUri("http://spironto.de/spironto#patient-gen11");
        Timestamp tstamp = new Timestamp(new Date().getTime());
        textBlock.setCreated(DateUtil.getBirthDateFromString(tstamp.toString()));
        textBlock.setText("This is a wonderfull endless text about the most important patient N°11");
        textBlock.setConcepts(null);
        return textBlock;
    }

    /**
     * 
     * @param textBlock -> Consumes an TextBlockObject from GUI- side
     * @return textBlock
     * Purpose: Returns a TextBlockObject searched by URI
     * Mostly used for fetching data
     * @throws IOException
     * @throws SpirontoException
     */
    @Path("/getTextBlockByUri")
    @POST
    @Produces(MediaType.APPLICATION_XML)
    public TextBlock getTextBlock(TextBlock textBlock) throws IOException, SpirontoException {
        return this.textBlockService.findTextBlock(textBlock.getUri());
    }

    /**
     * 
     * @param textBlock -> Consumes an TextBlockObject from GUI- side
     * @return textBlock
     * Purpose: Creates a TextBlock. Returns created textBlock with new URI in it
     * @throws IOException
     */
    @Path("/createTextBlock")
    @POST
    @Produces(MediaType.APPLICATION_XML)
    public TextBlock createTextBlock(TextBlock textBlock) throws IOException {
        return this.textBlockService.createTextBlock(textBlock);
    }

    /**
     * 
     * @param patient --> Consumes an PatienObject from GUI- side
     * @return Collection of textBlocks
     * Purpose: Gets all linked TextBlock to given Patient
     * @throws IOException
     * @throws SpirontoException
     */
    @Path("/getTextBlocks")
    @POST
    @Produces(MediaType.APPLICATION_XML)
    public List<TextBlock> getTextBlocks(Patient patient) throws IOException, SpirontoException {
        return this.textBlockService.findTextBlocks(patient);
    }
    
    /**
     * 
     * @param textBlock -> Consumes an TextBlockObject from GUI- side
     * @param concept -> Consumes an ConceptObject from GUI- side
     * @return
     * Purpose: Connects given Concept with given textBlock
     * @throws IOException
     * @throws SpirontoException
     */
    @Path("/addConceptToTextBlock")
    @POST
    @Produces(MediaType.APPLICATION_XML)
    public TextBlock addConceptToTextBlock(TextBlock textBlock, Concept concept) throws IOException, SpirontoException {
        return this.textBlockService.addConceptToTextBlock(textBlock, concept);
    }
    
}
