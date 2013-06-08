package de.klinikum.service.Interfaces;

import java.io.IOException;
import java.util.List;

import org.openrdf.repository.RepositoryException;

import de.klinikum.domain.Address;
import de.klinikum.domain.Patient;
import de.klinikum.exceptions.TripleStoreException;

public interface PatientService {

    Patient getPatientByUri(String patientUri) throws TripleStoreException;

    Patient createPatientRDF(Patient patient) throws TripleStoreException;

    List<Patient> searchPatient(Patient patient) throws TripleStoreException;

    List<Patient> searchPatientSPARQL(Patient patient) throws TripleStoreException;

    boolean updatePatientRDF(Patient patient) throws IOException, RepositoryException;

    Address getAddressByUri(Address address) throws TripleStoreException;

    Patient getPatientByPatientNumber(String patientNumber) throws TripleStoreException;

    Address createAddressRDF(Address address) throws TripleStoreException;

    boolean updateAddressRDF(Address address) throws IOException, RepositoryException;
}
