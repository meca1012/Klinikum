package de.klinikum.communication;

import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
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
@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML, MediaType.APPLICATION_JSON })
@Consumes
@Stateless
public class PatientREST {


	@Inject 
	private PatientService patientService;
	
	@Path("/test")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String test() {
		return "REST- Service Running";
	}

	@Path("/test2")
	@POST
	@Produces(MediaType.APPLICATION_XML)
	public String newPatient() {
		return "This should be a patient!";
	}

	 @Path("/getPatient/{patientNumber}")
	 @GET
	 @Produces(MediaType.APPLICATION_XML)
	 public Patient getPatient(@PathParam("patientNumber") String
	 patientNumber)
	 {
		 System.out.println("RestFuncStart");
		 System.out.println("PatientNumber:" + patientNumber);
		 System.out.println("ClasseName:" + patientService.getClassName());
		 Patient test = patientService.getPatient(patientNumber);
		 System.out.println("RestFuncEnd");
	 return test;
	 }

	@Path("/getPatient")
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Patient getPatient2() {
		Patient patient = new Patient();
		patient.setnName("Mueller");
		patient.setvName("Uli");
		patient.setUri("http://spironto.de/patient/123123123");
		return patient;
	}

//	public PatientService getPatientService() {
//		return patientService;
//	}

//	public void setPatientService(PatientService patientService) {
//		this.patientService = patientService;
//	}

}
