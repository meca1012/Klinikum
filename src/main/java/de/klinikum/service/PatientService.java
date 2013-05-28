package de.klinikum.service;

import java.io.IOException;
import java.util.List;

import org.openrdf.repository.RepositoryException;

import de.klinikum.domain.Patient;

public interface PatientService {
	
	public abstract Patient createPatientRDF(Patient patient) throws IOException;

	public abstract List<Patient> searchPatient(Patient patient);
	
	public abstract List<Patient> searchPatientSPARQL(Patient patient) throws IOException, RepositoryException;

	public abstract Patient updatePatient(Patient patient);

}