package service;

import javax.inject.Inject;

import org.junit.Test;

import de.klinikum.domain.Patient;
import de.klinikum.service.PatientService;
import de.klinikum.service.PatientServiceImpl;

public class PatientServiceTest {
	
	@Inject
	PatientServiceImpl patientService;
	
	@Test
	public void testCreatePatient() {
		Patient patient = new Patient();
	}
}
