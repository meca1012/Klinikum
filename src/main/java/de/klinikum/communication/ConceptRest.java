package de.klinikum.communication;

import java.io.IOException;
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
import de.klinikum.domain.PatientDTO;
import de.klinikum.service.ConceptService;

@Path("/concept")
@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML, MediaType.APPLICATION_JSON })
@Consumes
@Stateless
public class ConceptRest {

    @Inject
    ConceptService conceptService;

    @Path("/getTabConcepts")
    @POST
    @Produces(MediaType.APPLICATION_XML)
    public List<Concept> getTabConcepts(Patient patient) throws IOException {

        return this.conceptService.findTabConcepts(patient);
    }

    @Path("/getConcepts")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public List<Concept> getConcepts(Patient patient) {

        return this.conceptService.findConcepts(patient);
    }

    @Path("/createConcept")
    @POST
    @Produces(MediaType.APPLICATION_XML)
    public Concept createConcept(Concept concept) {

        return this.conceptService.createConcept(concept);
    }

    @Path("/addConceptToPatient")
    @POST
    @Produces(MediaType.APPLICATION_XML)
    public Concept addConceptToPatient(PatientDTO pDto) throws IOException {

        return this.conceptService.addConceptToPatient(pDto.getConcept(), pDto.getPatient(), pDto.isTabConcept());
    }

}
