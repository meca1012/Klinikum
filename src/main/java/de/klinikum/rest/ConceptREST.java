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

@Path("/concept")
@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML, MediaType.APPLICATION_JSON })
@Consumes
@Stateless
public class ConceptREST {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ConceptREST.class);

    @Inject
    ConceptService conceptService;

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

    @Path("/getTabConcepts")
    @POST
    @Produces(MediaType.APPLICATION_XML)
    public List<Concept> getTabConcepts(Patient patient) throws IOException, SpirontoException {

        return this.conceptService.findTabConceptsOfPatient(patient);
    }

    @Path("/getConcepts")
    @POST
    @Produces(MediaType.APPLICATION_XML)
    public List<Concept> getConcepts(Patient patient) throws IOException, SpirontoException {

        return this.conceptService.findAllConceptsOfPatient(patient);
    }

    @Path("/createConcept")
    @POST
    @Produces(MediaType.APPLICATION_XML)
    public Concept createConcept(Concept concept) throws IOException {

        return this.conceptService.addConceptToPatient(concept);
    }

    @Path("/createTabConcept")
    @POST
    @Produces(MediaType.APPLICATION_XML)
    public Concept createTabConcept(Concept concept) throws IOException {

        return this.conceptService.addTabConcept(concept);
    }

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
