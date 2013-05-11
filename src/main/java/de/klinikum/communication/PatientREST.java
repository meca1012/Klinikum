package de.klinikum.communication;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.klinikum.domain.Patient;
import de.klinikum.service.PatientServiceImpl;

@Path("/patient")
@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML,
		MediaType.APPLICATION_JSON })
@Consumes
@RequestScoped
public class PatientREST {

	@Inject
	private PatientServiceImpl patientService;


	@Path("/getPatient/{patientNumber}")
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Patient getPatient(@PathParam("patientNumber") String patientNumber) {
		Patient p1 = patientService.getPatientPatientnumber(patientNumber);
		
		return p1;
	}

	@POST
	@Path("/createPatient")
	@Consumes(MediaType.APPLICATION_XML)
	public Patient createPatient(Patient patient) {
		Patient p1 = patientService.createPatient(patient);
		return p1;
	}

	@POST
	@Path("/searchPatient")
	@Consumes(MediaType.APPLICATION_XML)
	public List<Patient> searchPatient(Patient patient) {
		List<Patient> p1 = patientService.searchPatient(patient);
		return p1;
	}

}
