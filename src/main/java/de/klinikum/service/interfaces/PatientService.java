package de.klinikum.service.interfaces;

import java.io.IOException;
import java.util.List;

import org.openrdf.repository.RepositoryException;

import de.klinikum.domain.Address;
import de.klinikum.domain.Patient;
import de.klinikum.exceptions.SpirontoException;
import de.klinikum.exceptions.TripleStoreException;

public interface PatientService {

    /**
     * Returns a Patient to a patientUri
     * 
     * @param patientUri
     * @return
     * @throws TripleStoreException
     */
    Patient getPatientByUri(String patientUri) throws TripleStoreException;

    /**
     * Creates a new Patient.
     * 
     * @param patient
     * @return
     * @throws TripleStoreException
     */
    Patient createPatientRDF(Patient patient) throws TripleStoreException;

    List<Patient> searchPatientSPARQL(Patient patient) throws TripleStoreException, SpirontoException;

    boolean updatePatientRDF(Patient patient) throws IOException, RepositoryException, TripleStoreException;

    Address getAddressByUri(Address address) throws TripleStoreException;

    Patient getPatientByPatientNumber(String patientNumber) throws TripleStoreException;

    Address createAddressRDF(Address address) throws TripleStoreException;

    boolean updateAddressRDF(Address address) throws IOException, RepositoryException, TripleStoreException;

    /**
     * adds standard tabConcepts and concepts to patient
     * 
     * @throws IOException
     */
    void createStandardConcepts(Patient patient) throws IOException;
}
