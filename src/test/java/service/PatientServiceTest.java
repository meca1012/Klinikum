package service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.joda.time.DateTime;
import org.junit.Before;
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

	@Inject
	PatientService patientService;

	private static Patient patient;
	
	private static String patientUri;
	
	@Before
	public void before() {
		this.patient = new Patient();
		this.patient.setFirstName("Alice");
		this.patient.setLastName("Smith");
		this.patient.setDateOfBirth(new DateTime());
//		this.patient.setUri("http://spironto.de/spironto#patient-gen1");
		this.patient.setPatientNumber("112233");
		Address address = new Address(null, "Musterstr.", "1", "Musterstadt",
				"76123", "D", "110");
		this.patient.setAddress(address);
//		this.patientService.createPatientRDF(this.patient);
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

		Patient returnPatient = this.patientService.createPatientRDF(this.patient);

		assertEquals(patient.getFirstName(), returnPatient.getFirstName());
		assertEquals(patient.getLastName(), returnPatient.getLastName());
		assertEquals(patient.getPatientNumber(), returnPatient.getPatientNumber());
		assertNotNull(patient.getUri());

		assertEquals(patient.getAddress().getStreet(), returnPatient.getAddress()
				.getStreet());
		assertEquals(patient.getAddress().getStreetNumber(), returnPatient
				.getAddress().getStreetNumber());
		assertEquals(patient.getAddress().getCity(), returnPatient.getAddress()
				.getCity());
		assertEquals(patient.getAddress().getZip(), returnPatient.getAddress()
				.getZip());
		assertEquals(patient.getAddress().getCountry(), returnPatient.getAddress()
				.getCountry());
		assertEquals(patient.getAddress().getPhone(), returnPatient.getAddress()
				.getPhone());
		assertNotNull(patient.getAddress().getUri());
	
		String patientUri = returnPatient.getUri();
		this.patientUri = patientUri;
	}

	@Test
	public void getPatientByUriTest() throws TripleStoreException {
		Patient patient = new Patient();
		patient = this.patientService.getPatientByUri(this.patientUri);

		assertEquals(this.patientUri, patient.getUri());
		assertNotNull(patient.getFirstName());
		assertNotNull(patient.getLastName());
		assertNotNull(patient.getAddress());
	}

	@Test
	public void searchPatientSPARQLTest() throws RepositoryException,
			IOException, SpirontoException {
		Patient patient = new Patient();
		patient.setUri(this.patientUri);
		List<Patient> patients = this.patientService
				.searchPatientSPARQL(patient);
		
		assertNotNull(patients);
	}

	@Test
	public void updatePatientRDFTest() throws IOException, RepositoryException,
			TripleStoreException {

		// Create a new patient
		Patient patient = new Patient();
		patient.setFirstName("Klaus");
		patient.setLastName("Blau");
		patient.setPatientNumber("3456");
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

		assertEquals(patient.getFirstName(), updatePatient.getFirstName());
		assertEquals(patient.getLastName(), updatePatient.getLastName());
		assertEquals(patient.getPatientNumber(), updatePatient.getPatientNumber());
		assertNotNull(patient.getUri());
		assertEquals(patient.getAddress().getStreet(), updatePatient.getAddress()
				.getStreet());
		assertEquals(patient.getAddress().getStreetNumber(), updatePatient
				.getAddress().getStreetNumber());
		assertEquals(patient.getAddress().getCity(), updatePatient.getAddress()
				.getCity());
		assertEquals(patient.getAddress().getZip(), updatePatient.getAddress()
				.getZip());
		assertEquals(patient.getAddress().getCountry(), updatePatient.getAddress()
				.getCountry());
		assertEquals(patient.getAddress().getPhone(), updatePatient.getAddress()
				.getPhone());
		assertNotNull(patient.getAddress().getUri());
	}

	@Test
	@Ignore
	public void getAddressByUriTest() {

	}

	@Test
	@Ignore
	public void getPatientByPatientNumberTest() {

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

	public static String getPatientUri() {
		return patientUri;
	}

	public static void setPatientUri(String patientUri) {
		PatientServiceTest.patientUri = patientUri;
	}

}
