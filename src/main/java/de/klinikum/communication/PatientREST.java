package de.klinikum.communication;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.klinikum.domain.Patient;
import de.klinikum.service.PatientService;

@Path("/patient")
@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML,
		MediaType.APPLICATION_JSON })
@Consumes
public class PatientREST {
	
	public PatientREST(){
		this.patientService = new PatientService();
	}
	
	private PatientService patientService;

	@Path("/test")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String test() {
		return "Yea! ";
	}

	@Path("/test2")
	@POST
	@Produces(MediaType.APPLICATION_XML)
	public String newPatient() {
		return "This should be a patient!";
	}

//	@Path("/getPatient/{patientNumber}")
//	@GET
//	@Produces(MediaType.APPLICATION_XML)
//	public Patient getPatient(@PathParam("patientNumber") String patientNumber) 
//	{
//		return PatientService.getPatient(patientNumber);
//	}
	
	@Path("/getPatient")
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Patient getPatient() 
	{
		return this.patientService.getPatient("123123123");
	}
	
	@Path("/getPatient2")
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Patient getPatient2() 
	{
		Patient patient = new Patient();
		patient.setnName("Mueller");
		patient.setvName("Uli");
		patient.setUri("blabla");
		return patient;
	}

	public PatientService getPatientService() {
		return patientService;
	}

	public void setPatientService(PatientService patientService) {
		this.patientService = patientService;
	}
	
}
