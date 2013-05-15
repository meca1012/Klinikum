package de.klinikum.communication;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.klinikum.domain.Address;
import de.klinikum.domain.Patient;
import de.klinikum.service.PatientServiceImpl;

@Path("/patient")
@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML,
		MediaType.APPLICATION_JSON })
@Consumes
@Stateless
public class PatientREST {

	@Inject
	PatientServiceImpl patientService;

	@Path("/getPatientXML/{patientNumber}")
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Patient getPatientXML(@PathParam("patientNumber") String patientNumber) throws ParseException {
		
		Address a1 = new Address("Hauptstrass 12","Karlsruhe", "432433", "Deutschland", "081511833");
		a1.setUri("de.spironto/address/"+ "432432");
		Patient p1 = new Patient();
		p1.setAddress(a1);
		p1.setUri("de.spironto/patient/"+ "432432");
		p1.setnName("Power");
		p1.setvName("Max");
		return p1;
	}

//	@Path("/getPatient/{patientNumber}")
//	@GET
//	@Produces(MediaType.APPLICATION_XML)
//	public Patient getPatient(@PathParam("patientNumber") String patientNumber) {
//		Patient p1 = patientService.getPatientPatientnumber(patientNumber);
//		return p1;
//	}

	@POST
	@Path("/createPatient")
	@Consumes(MediaType.APPLICATION_XML)
	public Patient createPatientRDF(Patient patient) throws IOException {
		return this.patientService.createPatientRDF(patient);
	}

	@POST
	@Path("/searchPatient")
	@Consumes(MediaType.APPLICATION_XML)
	public List<Patient> searchPatient(Patient patient) {
		List<Patient> p1 = patientService.searchPatient(patient);
		return p1;
	}

}
