package de.klinikum.service;

import java.io.IOException;
import java.util.List;

import de.klinikum.domain.Patient;

public interface PatientService {

	public abstract Patient createPatient(Patient patient);
	
	public abstract Patient createPatientRDF(Patient patient) throws IOException;

	public abstract List<Patient> searchPatient(Patient patient);

}