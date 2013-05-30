package de.klinikum.service;

import java.io.IOException;
import java.util.List;

import org.openrdf.repository.RepositoryException;

import de.klinikum.domain.Address;
import de.klinikum.domain.Patient;

public interface PatientService {

    Patient createPatientRDF(Patient patient) throws IOException;

    List<Patient> searchPatient(Patient patient);

    List<Patient> searchPatientSPARQL(Patient patient) throws IOException, RepositoryException;

	boolean updatePatientRDF(Patient patient) throws IOException, RepositoryException;
	
	boolean updateAddressRDF(Address address) throws IOException, RepositoryException;	
}

