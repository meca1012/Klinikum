package service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import javax.inject.Inject;

import org.junit.Test;
import org.openrdf.repository.RepositoryException;

import de.klinikum.domain.Address;
import de.klinikum.domain.Patient;
import de.klinikum.service.PatientServiceImpl;

public class PatientServiceTest {
	
	@Inject
	PatientServiceImpl patientService;
	
	String patientUri;
		
	@Test
	public void testCreatePatient() throws IOException {
		
		Patient patient = new Patient();
		patient.setvName("alice");
		patient.setnName("smith");
		
		Address address = new Address(null, "Moltkestraﬂe", "Karlsruhe", "76133", "D", "00");
		patient.setAddress(address);
		
		Patient patient1 = this.patientService.createPatientRDF(patient);
		
		assertEquals(patient.getvName(), patient1.getvName());
		assertEquals(patient.getnName(), patient1.getnName());
		assertNotNull(patient.getUri());
		assertEquals(patient.getAddress().getStreet(), patient1.getAddress().getStreet());
		assertEquals(patient.getAddress().getCity(), patient1.getAddress().getCity());
		assertEquals(patient.getAddress().getZip(), patient1.getAddress().getZip());
		assertEquals(patient.getAddress().getCountry(), patient1.getAddress().getCountry());
		assertEquals(patient.getAddress().getPhone(), patient1.getAddress().getPhone());
		assertNotNull(patient.getAddress().getUri());
		
		this.patientUri = patient1.getUri();
	}
	
	@Test
	public void getPatientByUri() throws RepositoryException, IOException {
		Patient patient = new Patient();
		patient.setUri(this.patientUri);
		patient = this.patientService.getPatientByUri(patient);
		
		assertEquals(this.patientUri, patient.getUri());
		assertNotNull(patient.getvName());
		assertNotNull(patient.getnName());
		assertNotNull(patient.getAddress());
	}
}
