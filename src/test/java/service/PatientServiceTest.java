package service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openrdf.repository.RepositoryException;

import de.klinikum.domain.Address;
import de.klinikum.domain.Patient;
import de.klinikum.persistence.SesameTripleStore;
import de.klinikum.service.PatientServiceImpl;

@RunWith(Arquillian.class)
public class PatientServiceTest {
	
	@Inject
	PatientServiceImpl patientService;
	
	String patientUri;
	
	/**
	 */
	@Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
            .addClass(PatientServiceImpl.class)
            .addClass(SesameTripleStore.class)
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
	
	@Test
	public void testCreatePatient() throws IOException {
		
		Patient patient = new Patient();
		patient.setFirstName("alice");
		patient.setLastName("smith");
		patient.setPatientNumber("12345");
		
		Address address = new Address(null, "Moltkestraﬂe", "30", "Karlsruhe", "76133", "D", "00");
		patient.setAddress(address);
		
		Patient patient1 = this.patientService.createPatientRDF(patient);
		
		assertEquals(patient.getFirstName(), patient1.getFirstName());
		assertEquals(patient.getLastName(), patient1.getLastName());
		assertEquals(patient.getPatientNumber(), patient1.getPatientNumber());
		assertNotNull(patient.getUri());
		assertEquals(patient.getAddress().getStreet(), patient1.getAddress().getStreet());
		assertEquals(patient.getAddress().getStreetNumber(), patient1.getAddress().getStreetNumber());
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
		patient = this.patientService.getPatientByUri(patient.getUri());
		
		assertEquals(this.patientUri, patient.getUri());
		assertNotNull(patient.getFirstName());
		assertNotNull(patient.getLastName());
		assertNotNull(patient.getAddress());
	}
	
	//TODO 
	@Test
	public void testUpdatePatient() throws IOException, RepositoryException {
		
		Patient patient = new Patient();
		patient = this.patientService.createPatientRDF(patient);
		
		
		
				
	}
	
	
}
