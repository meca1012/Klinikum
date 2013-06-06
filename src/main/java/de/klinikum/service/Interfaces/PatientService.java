package de.klinikum.service.Interfaces;

import java.io.IOException;
import java.util.List;

import org.openrdf.repository.RepositoryException;

import de.klinikum.domain.Address;
import de.klinikum.domain.Patient;
import de.klinikum.exceptions.SpirontoException;

public interface PatientService {

    Patient getPatientByUri(String patientUri) throws RepositoryException, IOException;

    Patient createPatientRDF(Patient patient) throws IOException;

    List<Patient> searchPatient(Patient patient);

    List<Patient> searchPatientSPARQL(Patient patient) throws RepositoryException, SpirontoException, IOException;

    boolean updatePatientRDF(Patient patient) throws IOException, RepositoryException;

    Address getAddressByUri(Address address) throws RepositoryException, IOException;

    Patient getPatientByPatientNumber(String patientNumber) throws RepositoryException, IOException;

    Address createAddressRDF(Address address) throws IOException;

    boolean updateAddressRDF(Address address) throws IOException, RepositoryException;
}
