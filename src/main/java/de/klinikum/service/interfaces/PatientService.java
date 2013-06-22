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

    /**
     * Returns a patien by various search criteria (patienNumber, fristName, lastName)
     * 
     * @param patient
     * @return
     * @throws TripleStoreException
     * @throws SpirontoException
     */
    List<Patient> searchPatientSPARQL(Patient patient) throws TripleStoreException, SpirontoException;

    /**
     * Updates a patient to a patientUri.The transferred data are updated.
     * 
     * @param patient
     * @return
     * @throws IOException
     * @throws RepositoryException
     * @throws TripleStoreException
     */
    boolean updatePatientRDF(Patient patient) throws IOException, RepositoryException, TripleStoreException;

    /**
     * Returns an address to a patientUri.
     * 
     * @param address
     * @return
     * @throws TripleStoreException
     */
    Address getAddressByUri(Address address) throws TripleStoreException;

    /**
     * Returns a patient by a unique patientNumber.
     * 
     * @param patientNumber
     * @return
     * @throws TripleStoreException
     */
    Patient getPatientByPatientNumber(String patientNumber) throws TripleStoreException;

    /**
     * Creates a new address to a Patient.
     * 
     * @param address
     * @return
     * @throws TripleStoreException
     */
    Address createAddressRDF(Address address) throws TripleStoreException;

    /**
     * Updating the address of a patient.
     * 
     * @param address
     * @return
     * @throws IOException
     * @throws RepositoryException
     * @throws TripleStoreException
     */
    boolean updateAddressRDF(Address address) throws IOException, RepositoryException, TripleStoreException;

    /**
     * adds standard tabConcepts and concepts to patient
     * 
     * @throws IOException
     */
    void createStandardConcepts(Patient patient) throws IOException;
}