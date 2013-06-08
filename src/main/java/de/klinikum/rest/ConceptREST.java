package de.klinikum.rest;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.openrdf.repository.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.klinikum.domain.Concept;
import de.klinikum.domain.Patient;
import de.klinikum.exceptions.SpirontoException;
import de.klinikum.service.Interfaces.ConceptService;

/**
 * 
 * ConceptREST.java
 * Purpose: REST- Connection- Points for Sesame Ontologie Concepts
 * 
 * @author  Spironto Team 1
 * @version 1.0 08/06/13
 */

@Path("/concept")
@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML, MediaType.APPLICATION_JSON })
@Consumes
@Stateless
public class ConceptREST {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ConceptREST.class);

    //CDI of ConceptService.class
	@Inject
    ConceptService conceptService;

    /**
     * 
     * @return Returns a standard XML- Parse of an ConceptObject 
     * @throws ParseException
     */
    @Path("/getConceptXML")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Concept getConceptXML() throws ParseException {

        Concept conceptToConnect1 = new Concept();
        conceptToConnect1.setLabel("connectedTestConcept1");
        conceptToConnect1.setUri("http://spironto.de/spironto#concept-gen15");
        conceptToConnect1.setPatientUri("http://spironto.de/spironto#patient-gen11");

        Concept conceptToConnect2 = new Concept();
        conceptToConnect2.setLabel("connectedTestConcept2");
        conceptToConnect2.setUri("http://spironto.de/spironto#concept-gen16");
        conceptToConnect2.setPatientUri("http://spironto.de/spironto#patient-gen11");

        Concept concept = new Concept();
        concept.setLabel("TestConcept");
        concept.addConnectedConcepts(conceptToConnect1);
        concept.addConnectedConcepts(conceptToConnect2);
        concept.setUri("http://spironto.de/spironto#concept-gen16");
        concept.setPatientUri("http://spironto.de/spironto#patient-gen11");
        return concept;
    }
    
    /**
     * 
     * @param patient -> Consumes an PatientObject from GUI- side 
     * and searches for TabConcepts linked to this Patient
     * @return TabConcepts linked to given Patient
     * TabConcepts are the Concepts to build the UI Main- Tabs 
     * @throws IOException
     * @throws SpirontoException
     */
    @Path("/getTabConcepts")
    @POST
    @Produces(MediaType.APPLICATION_XML)
    public List<Concept> getTabConcepts(Patient patient) throws IOException, SpirontoException {
        
        return this.conceptService.findTabConceptsOfPatient(patient);
    }

    /**
     * 
     * @param patient -> Consumes an PatienObject from GUI- side
     * @return -> Returns a List of concepts connected to the Patient. This is a collection of 
     * normal OntologieConcepts 
     * @throws IOException
     * @throws SpirontoException
     */
    @Path("/getConcepts")
    @POST
    @Produces(MediaType.APPLICATION_XML)
    public List<Concept> getConcepts(Patient patient) throws IOException, SpirontoException {

        return this.conceptService.findAllConceptsOfPatient(patient);
    }
    
    /**
     * 
     * @param concept -> Consumes an ConceptObject from GUI- side
     * Purpose: Stores given Concept to SesameStore
     * @return
     * @throws IOException
     */
    @Path("/createConcept")
    @POST
    @Produces(MediaType.APPLICATION_XML)
    public Concept createConcept(Concept concept) throws IOException {

        return this.conceptService.addConceptToPatient(concept);
    }

    /**
     * 
     * @param concept -> Consumes ConceptType with is created as TabConcept
     * Purpose: Creates TabConcepts needed for GUI build
     * @return: Returns Created TabConcept with URI as identifier
     * @throws IOException
     */
    @Path("/createTabConcept")
    @POST
    @Produces(MediaType.APPLICATION_XML)
    public Concept createTabConcept(Concept concept) throws IOException {

        return this.conceptService.addTabConcept(concept);
    }

    /**
     * 
     * @param concept -> Consumes an conceptObject from GUI- side
     * @return: Returns connected concepts
     * @throws IOException
     */
    @Path("/connectConcepts")
    @POST
    @Produces(MediaType.APPLICATION_XML)
    public Concept connectConcepts(Concept concept) throws IOException {

        if (concept.getConnectedConcepts() == null) {
            return null;
        }
        else {
            if (concept.getConnectedConcepts().size() == 1) {
                this.conceptService.connectSingleConcept(concept, concept.getConnectedConcepts().get(0));
            }
            else {
                this.conceptService.connectMultipleConcepts(concept, concept.getConnectedConcepts());
            }
        }
        return concept;
    }

    /**
     * 
     * @param Consumes an conceptObject from GUI- side
     * Purpose: Returns directly connected concepts to given  
     * @return
     * @throws IOException
     * @throws RepositoryException
     * @throws SpirontoException
     */
    @Path("/getConceptByUriFetchDirectConnected")
    @POST
    @Produces(MediaType.APPLICATION_XML)
    public Concept addConceptToPatient(Concept concept) throws IOException, RepositoryException, SpirontoException {

        concept = this.conceptService.getConceptByUri(concept.getUri());
        concept.setConnectedConcepts(this.conceptService.getDirectConnected(concept));
        return concept;
    }

    // @Path("/addConceptToPatient")
    // @POST
    // @Produces(MediaType.APPLICATION_XML)
    // public Concept addConceptToPatient(PatientDTO pDto) throws IOException {
    //
    // return this.conceptService.addConceptToPatient(pDto.getConcept(), pDto.isTabConcept());
    // }

}
