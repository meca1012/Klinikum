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
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.openrdf.repository.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.klinikum.domain.Address;
import de.klinikum.domain.Patient;
import de.klinikum.exceptions.SpirontoException;
import de.klinikum.exceptions.TripleStoreException;
import de.klinikum.service.implementation.PatientServiceImpl;
import de.klinikum.service.interfaces.PatientService;

/**
 * 
 * PatientRest.java
 * Purpose: REST- Connection- Points for UI
 * Main Task is to deliver Patientdata from SesameStore
 * 
 * @author  Spironto Team 1
 * @version 1.0 08/06/13
 */
@Path("/patient")
@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML, MediaType.APPLICATION_JSON })
@Consumes
@Stateless
public class PatientREST {

	private static final Logger LOGGER = LoggerFactory.getLogger(PatientREST.class);

	//CDI for PatientServiceClass -> TODO: Change to Interface 
	@Inject
	PatientService patientService;

    /**
     * 
     * @return Returns a standard XML- Parse of an Patient.class 
     * @throws ParseException
     */
    @Path("/getPatientXML")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Patient getPatientXML() throws ParseException {

        DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yyyy");  
        DateTime date = dtf.parseDateTime("01/08/1985");

        Address a1 = new Address("http://spironto.de/spironto#address-gen4", "Hauptstrasse", "12", "Karlsruhe",
                "432433", "Deutschland", "081511833");
        a1.setUri("http://spironto.de/spironto#address-gen2");

        Patient p1 = new Patient();
        p1.setPatientNumber("081512321");
        p1.setAddress(a1);
        p1.setUri("http://spironto.de/spironto#patient-gen1");
        p1.setLastName("Power");
        p1.setFirstName("Max");
        p1.setDateOfBirth(date);
        return p1;
    }

    /**
     * TODO: ReWrite
     * @param patientNumber -> Consumes an StringnObject from GUI- side
     * @return Returns Patient search by PatientNumber
     * Purpose: Main Searched used by Careteam. Patientnumber is set from #
     * CareTeam and should
     * be the same as in the Patient- Documents from the Hospital or be different in #
     * case of anoymous SpirontoOPatient
     * and hospital documentation
     * @throws SpirontoException
     */
	@Path("/getPatientByPatientNumber/{patientNumber}")
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Patient getPatientByPatientNumber(@PathParam("patientNumber") String patientNumber) 
	                                                                    throws SpirontoException {
		try {
			return this.patientService.getPatientByPatientNumber(patientNumber);
		} 
		    catch (TripleStoreException e) {
			e.printStackTrace();
			throw new SpirontoException("Error getting patient: " + e.toString(), e);
		} 
		    catch (Exception e) {
			e.printStackTrace();
			throw new SpirontoException("Error getting patient: " + e.toString(), e);
		}
	}

	/**
	 * 
	 * @param patient -> Consumes an PatienObject from GUI- side
	 * @return Response HTTP status code depending on function result
	 * Purpose: Update hole Patient Data. Function updates all notNull Field 
	 * from given Object
	 * @throws IOException
	 * @throws RepositoryException
	 */
	@POST
	@Path("/updatePatient")
	@Consumes(MediaType.APPLICATION_XML)
	public Response updatePatientRDF(Patient patient) throws IOException, RepositoryException {

		if (this.patientService.updatePatientRDF(patient)) {
			return Response.status(Response.Status.OK).entity("ok").build();
		}
		return Response.status(Response.Status.NOT_MODIFIED).entity("not modified").build();

	}

	/**
	 * 
	 * @param patient -> Consumes an PatienObject from GUI- side
	 * @return Returns created PatientObject with URI generated by Sesame
	 * Purpose: Creates User in Sesame
	 * @throws SpirontoException
	 */
	@POST
	@Path("/createPatient")
	@Consumes(MediaType.APPLICATION_XML)
	public Patient createPatientRDF(Patient patient) throws SpirontoException {
		try {
			return this.patientService.createPatientRDF(patient);
		}
		    catch (TripleStoreException e) {
    			e.printStackTrace();
    			throw new SpirontoException("Error creating patient: " + e.toString(), e);
		} 
		    catch (Exception e) {
    			e.printStackTrace();
    			throw new SpirontoException("Error creating patient: " + e.toString(), e);
		}

	}

	/**
	 * 
	 * @param patient -> Consumes an PatienObject from GUI- side
	 * @return PatientObject
	 * Purpose: Searches for Patient by URI -> Mostly used for fetching data. 
	 * Normally the PatientNumber is the used primarykey
	 * @throws SpirontoException
	 */
	@POST
	@Path("/getPatientByUri")
	@Consumes(MediaType.APPLICATION_XML)
	public Patient getPatientByUri(Patient patient) throws SpirontoException {
		try {
			return this.patientService.getPatientByUri(patient.getUri());
		}
		catch (TripleStoreException e) {
			e.printStackTrace();
			throw new SpirontoException("Error getting patient: " + e.toString(), e);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new SpirontoException("Error getting patient: " + e.toString(), e);
		}
	}
	
	/**
	 * 
	 * @param patient -> Consumes an PatienObject from GUI- side
	 * @return -> Consumes an PatienObject or null depending on Methodresult
	 * Pupose: Searches for Patient in Sesame on given Patiendate
	 * Used Parameters in Searchmethod (searchPatientSPARQL)
	 *     Firstname, Lastname, DateofBirth, PatientNumber
	 * @throws SpirontoException
	 */
	@POST
	@Path("/searchPatient")
	@Consumes(MediaType.APPLICATION_XML)
	public List<Patient> searchPatient(Patient patient) throws SpirontoException {
		try {
			List<Patient> p1 = this.patientService.searchPatientSPARQL(patient);
			return p1;
		}
		catch (TripleStoreException e) {
			e.printStackTrace();
			throw new SpirontoException("Error searching patient: " + e.toString(), e);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new SpirontoException("Error searching patient: " + e.toString(), e);
		}
	}

}
