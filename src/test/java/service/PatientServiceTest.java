package service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.joda.time.DateTime;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openrdf.repository.RepositoryException;

import de.klinikum.domain.Address;
import de.klinikum.domain.Patient;
import de.klinikum.exceptions.SpirontoException;
import de.klinikum.exceptions.TripleStoreException;
import de.klinikum.persistence.SesameTripleStore;
import de.klinikum.service.implementation.ConceptServiceImpl;
import de.klinikum.service.implementation.PatientServiceImpl;
import de.klinikum.service.interfaces.PatientService;

@RunWith(Arquillian.class)
public class PatientServiceTest {
	
	Random generator = new Random(System.currentTimeMillis());

	@Inject
	PatientService patientService;

	private Patient generateNewPatientWithAddress() {
		Patient patient = new Patient();
		patient.setFirstName("Alice");
		patient.setLastName("Smith");
		patient.setDateOfBirth(new DateTime());
		patient.setPatientNumber(String.valueOf(this.generator.nextInt()));
		Address address = new Address(null, "Musterstr.", "1", "Musterstadt",
				"76123", "D", "110");
		patient.setAddress(address);
		return patient;
	}
	
	private Patient generateNewPatientWithoutAddress() {
		Patient patient = new Patient();
		patient.setFirstName("Alice");
		patient.setLastName("Smith");
		patient.setDateOfBirth(new DateTime());
		patient.setPatientNumber(String.valueOf(this.generator.nextInt()));
		return patient;
	}

	/**
	 */
	@Deployment
	public static JavaArchive createDeployment() {
		return ShrinkWrap.create(JavaArchive.class)
				.addClass(PatientServiceImpl.class)
				.addClass(ConceptServiceImpl.class)
				.addClass(SesameTripleStore.class)
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@Test
	public void createPatientRDFTest() throws TripleStoreException {
		Patient patient = this.generateNewPatientWithAddress();	
		Patient returnPatient = this.patientService
				.createPatientRDF(patient);

		assertNotNull(returnPatient);
	}

	@Test
	public void getPatientByUriTest() throws TripleStoreException{
		Patient patient = this.generateNewPatientWithAddress();
		patient = this.patientService.createPatientRDF(patient);
		
		Patient returnPatient = this.patientService.getPatientByUri(patient.getUri());

		assertEquals(returnPatient, patient);
	}

	@Test
	public void searchPatientSPARQLTest() throws SpirontoException{
		Patient patient = this.generateNewPatientWithAddress();
		patient = this.patientService.createPatientRDF(patient);
		
		List<Patient> patients = this.patientService.searchPatientSPARQL(patient);

		assertNotNull(patients);
	}

	@Test
	public void updatePatientRDFTest() throws IOException, RepositoryException,
			TripleStoreException {

		// Create a new patient
		Patient patient = new Patient();
		patient.setFirstName("Klaus");
		patient.setLastName("Blau");
		patient.setPatientNumber(String.valueOf(this.generator.nextInt()));
		patient.setDateOfBirth(new DateTime());
		Address address = new Address(null, "Moltkestraﬂe", "30", "Karlsruhe",
				"76133", "D", "00");
		patient.setAddress(address);
		patient = this.patientService.createPatientRDF(patient);

		// Update the lastname
		Patient updatePatient = new Patient();
		updatePatient.setUri(patient.getUri());
		updatePatient.setLastName("Gelb");
		this.patientService.updatePatientRDF(updatePatient);

		updatePatient = this.patientService.getPatientByUri(patient.getUri());
		patient.setLastName("Gelb");

		assertEquals(updatePatient, patient);
	}

	@Test
	public void getAddressByUriTest() throws TripleStoreException {
		Patient patient = this.generateNewPatientWithAddress();
		patient = this.patientService.createPatientRDF(patient);
		Address returnAddress = patient.getAddress();
		try {
			returnAddress = this.patientService.getAddressByUri(returnAddress);
		} catch (TripleStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(returnAddress, patient.getAddress());
	}

	@Test
	public void getPatientByPatientNumberTest() throws TripleStoreException {
		Patient patient = this.generateNewPatientWithAddress();
		patient = this.patientService.createPatientRDF(patient);	
		String patientNumber = patient.getPatientNumber();
		Patient returnPatient = null;
		
		returnPatient = this.patientService.getPatientByPatientNumber(patientNumber);

		assertEquals(returnPatient.getUri(),patient.getUri());
	}

	@Test
	@Ignore
	public void createAddressRDFTest() {

	}

	@Test
	@Ignore
	public void updateAddressRDFTest() {

	}

	@Test
	@Ignore
	public void createStandardConceptsTest() {

	}

}
