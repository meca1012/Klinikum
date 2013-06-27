package service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.klinikum.domain.Address;
import de.klinikum.domain.Patient;
import de.klinikum.persistence.SesameTripleStore;
import de.klinikum.service.implementation.ConceptServiceImpl;
import de.klinikum.service.implementation.PatientServiceImpl;
import de.klinikum.service.interfaces.PatientService;

/**
 * 
 * @author Carsten Meiser, Matthias Schwarzenbach, Ivan Tepeluk
 * 
 */
@RunWith(Arquillian.class)
public class PatientServiceTest {

    @Inject
    PatientService patientService;

    Random generator = new Random(System.currentTimeMillis());

    private Patient patient;

    @Before
    public void createPatient() throws Exception {
        this.patient = new Patient();
        this.patient.setFirstName("Anke");
        this.patient.setLastName("Musterfrau");
        this.patient.setDateOfBirth(new DateTime());
        this.patient.setPatientNumber(String.valueOf(this.generator.nextInt()));
        Address address = new Address(null, "Musterstr.", "1", "Musterstadt", "76123", "D", "110");
        this.patient.setAddress(address);
        this.patient = this.patientService.createPatientRDF(this.patient);
    }

    /**
	 */
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class).addClass(PatientServiceImpl.class)
                .addClass(ConceptServiceImpl.class).addClass(SesameTripleStore.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    public void createPatientRDFTest() throws Exception {
        Patient patient = new Patient();
        patient.setFirstName("Alice");
        patient.setLastName("Smith");
        patient.setDateOfBirth(new DateTime());
        patient.setPatientNumber(String.valueOf(this.generator.nextInt()));
        Address address = new Address(null, "Musterstr.", "1", "Musterstadt", "76123", "D", "110");
        patient.setAddress(address);

        Patient returnPatient = this.patientService.createPatientRDF(patient);

        assertNotNull(returnPatient);
        assertNotNull(returnPatient.getUri());
    }

    @Test
    public void getPatientByUriTest() throws Exception {
        Patient returnPatient = this.patientService.getPatientByUri(this.patient.getUri());

        assertEquals(returnPatient, this.patient);
    }

    @Test
    public void searchPatientSPARQLTest() throws Exception {
        List<Patient> patients = this.patientService.searchPatientSPARQL(this.patient);

        assertNotNull(patients);
    }

    @Test
    public void updatePatientRDFTest() throws Exception {
        // Update the lastname
        Patient updatePatient = this.patientService.getPatientByUri(this.patient.getUri());
        updatePatient.setLastName("Gelb");
        boolean updated = this.patientService.updatePatientRDF(updatePatient);

        updatePatient = this.patientService.getPatientByUri(updatePatient.getUri());
        this.patient.setLastName("Gelb");

        assertTrue(updated);
        assertEquals(updatePatient, this.patient);
    }

    @Test
    public void getAddressByUriTest() throws Exception {
        Address returnAddress = this.patient.getAddress();
        returnAddress = this.patientService.getAddressByUri(returnAddress);

        assertEquals(returnAddress, this.patient.getAddress());
    }

    @Test
    public void getPatientByPatientNumberTest() throws Exception {
        String patientNumber = this.patient.getPatientNumber();
        Patient returnPatient = this.patientService.getPatientByPatientNumber(patientNumber);

        assertEquals(returnPatient.getUri(), this.patient.getUri());
    }

    @Test
    public void updateAddressRDFTest() {
        // updateAddressRDF is called in createPatientRDF
    }

    @Test
    public void createStandardConceptsTest() {
        // createStandardConceptsTest is called in createPatientRDF
        // and is tested in ConceptServiceTest
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

}
