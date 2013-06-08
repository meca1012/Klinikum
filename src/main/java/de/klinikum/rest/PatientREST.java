package de.klinikum.rest;

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
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.openrdf.repository.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.klinikum.domain.Address;
import de.klinikum.domain.Patient;
import de.klinikum.exceptions.SpirontoException;
import de.klinikum.exceptions.TripleStoreException;
import de.klinikum.service.Implementation.PatientServiceImpl;

/**
 *
 */
@Path("/patient")
@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML, MediaType.APPLICATION_JSON })
@Consumes
@Stateless
public class PatientREST {

	private static final Logger LOGGER = LoggerFactory.getLogger(PatientREST.class);

	@Inject
	PatientServiceImpl patientService;

	@Path("/getPatientXML")
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Patient getPatientXML() {
		try {
			String dateString = "01/08/1985";
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

			Address a1 = new Address("http://spironto.de/spironto#address-gen4", "Hauptstrasse", "12", "Karlsruhe",
					"432433", "Deutschland", "081511833");
			a1.setUri("de.spironto/address/" + "432432");

			Patient p1 = new Patient();
			p1.setPatientNumber("081512321");
			p1.setAddress(a1);
			p1.setUri("http://spironto.de/spironto#patient-gen5");
			p1.setLastName("Power");
			p1.setFirstName("Max");
			p1.setDateOfBirth(formatter.parse(dateString));
			return p1;
		} catch (ParseException pe) {
			pe.printStackTrace();
		}
		return null;
	}

	@Path("/getPatientByPatientNumber/{patientNumber}")
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Patient getPatientByPatientNumber(@PathParam("patientNumber") String patientNumber) throws SpirontoException {
		try {
			return this.patientService.getPatientByPatientNumber(patientNumber);
		} catch (TripleStoreException e) {
			e.printStackTrace();
			throw new SpirontoException("Error getting patient: " + e.toString(), e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SpirontoException("Error getting patient: " + e.toString(), e);
		}
	}

	@POST
	@Path("/updatePatient")
	@Consumes(MediaType.APPLICATION_XML)
	public Response updatePatientRDF(Patient patient) throws IOException, RepositoryException {

		if (this.patientService.updatePatientRDF(patient)) {
			return Response.status(Response.Status.OK).entity("ok").build();
		}
		return Response.status(Response.Status.NOT_MODIFIED).entity("not modified").build();

	}

	@POST
	@Path("/createPatient")
	@Consumes(MediaType.APPLICATION_XML)
	public Patient createPatientRDF(Patient patient) throws SpirontoException {
		try {
			return this.patientService.createPatientRDF(patient);
		} catch (TripleStoreException e) {
			e.printStackTrace();
			throw new SpirontoException("Error creating patient: " + e.toString(), e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SpirontoException("Error creating patient: " + e.toString(), e);
		}

	}

	@POST
	@Path("/getPatientByUri")
	@Consumes(MediaType.APPLICATION_XML)
	public Patient getPatientByUri(Patient patient) throws SpirontoException {
		try {
			return this.patientService.getPatientByUri(patient.getUri());
		} catch (TripleStoreException e) {
			e.printStackTrace();
			throw new SpirontoException("Error getting patient: " + e.toString(), e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SpirontoException("Error getting patient: " + e.toString(), e);
		}
	}

	@POST
	@Path("/searchPatient")
	@Consumes(MediaType.APPLICATION_XML)
	public List<Patient> searchPatient(Patient patient) throws SpirontoException {
		try {
			List<Patient> p1 = this.patientService.searchPatientSPARQL(patient);
			return p1;
		} catch (TripleStoreException e) {
			e.printStackTrace();
			throw new SpirontoException("Error searching patient: " + e.toString(), e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SpirontoException("Error searching patient: " + e.toString(), e);
		}
	}

}