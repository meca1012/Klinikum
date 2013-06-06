package service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openrdf.repository.RepositoryException;

import de.klinikum.domain.Address;
import de.klinikum.domain.Patient;
import de.klinikum.exceptions.SpirontoException;
import de.klinikum.persistence.SesameTripleStore;
import de.klinikum.service.Implementation.PatientServiceImpl;

@RunWith(Arquillian.class)
public class PatientServiceTest {

    @Inject
    PatientServiceImpl patientService;

    String patientUri;

    Patient patient;

    @Before
    public void before() throws IOException {
        this.patient = new Patient();
        this.patient.setFirstName("Alice");
        this.patient.setLastName("Smith");
        this.patient.setDateOfBirth(new Date());
        this.patient.setUri("http://spironto.de/spironto#patient-gen1");
        this.patient.setPatientNumber("112233");
        Address address = new Address(null, "Musterstr.", "1", "Musterstadt", "76123", "D", "110");
        this.patient.setAddress(address);
        this.patientService.createPatientRDF(this.patient);
    }

    /**
	 */
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class).addClass(PatientServiceImpl.class)
                .addClass(SesameTripleStore.class).addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    public void createPatientRDFTest() throws IOException {

        Patient patient = new Patient();
        patient.setFirstName("alice");
        patient.setLastName("smith");

        patient.setPatientNumber("1111");
        patient.setDateOfBirth(new Date());

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
        this.patient.setUri(this.patientUri);
    }

    @Test
    public void getPatientByUriTest() throws RepositoryException, IOException {
        Patient patient = new Patient();
        patient = this.patientService.getPatientByUri(this.patient.getUri());

        assertEquals(this.patient.getUri(), patient.getUri());
        assertNotNull(patient.getFirstName());
        assertNotNull(patient.getLastName());
        assertNotNull(patient.getAddress());
    }

    @Ignore
    @Test
    public void searchPatientTest() {

        List<Patient> patients = this.patientService.searchPatient(this.patient);
        // TODO: implement me
    }

    @Test
    public void searchPatientSPARQLTest() throws RepositoryException, IOException, SpirontoException {
        Patient patient = new Patient();
        patient.setUri(this.patientUri);
        List<Patient> patients = this.patientService.searchPatientSPARQL(patient);
        // TODO: implement me
    }

    // Test UpdatePatient
    @Test
    public void testUpdatePatient() throws IOException, RepositoryException {

        // Create
        Patient patient = new Patient();
        patient.setFirstName("klaus");
        patient.setLastName("blau");
        patient.setPatientNumber("3456");
        Address address = new Address(null, "Moltkestraﬂe", "30", "Karlsruhe", "76133", "D", "00");
        patient.setAddress(address);
        patient = this.patientService.createPatientRDF(patient);

        // Update
        patient.setFirstName("hans");
        patient.setLastName("gelb");
        patient.setPatientNumber("6789");
        Address address1 = new Address(null, "Kaiserstraﬂe", "40", "Stuttgart", "78999", "D", "09");
        patient.setAddress(address1);
        this.patientService.updatePatientRDF(patient);

        Patient patient1 = this.patientService.getPatientByUri(patient.getUri());

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

}
