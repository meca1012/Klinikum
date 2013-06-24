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
     * Creates a new Patient. If the patientNumber is already in use, null is returned.
     * 
     * @param patient with address
     * @return
     * @throws TripleStoreException
     */
    Patient createPatientRDF(Patient patient) throws TripleStoreException;

    /**
     * Returns a patient by various search criteria (patienNumber, fristName, lastName, birthDate)
     * 
     * @param patient
     * @return
     * @throws TripleStoreException
     * @throws SpirontoException
     */
    List<Patient> searchPatientSPARQL(Patient patient) throws TripleStoreException, SpirontoException;

    /**
     * Updates a patient identified by his patientUri.The transfered data is updated.
     * 
     * @param patient
     * @return
     * @throws IOException
     * @throws RepositoryException
     * @throws TripleStoreException
     */
    boolean updatePatientRDF(Patient patient) throws IOException, RepositoryException, TripleStoreException;

    /**
     * Returns an address by its Uri.
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
     * Creates a new address. Should only be called on the service layer.
     * 
     * @param address
     * @return
     * @throws TripleStoreException
     */
    Address createAddressRDF(Address address) throws TripleStoreException;

    /**
     * Updates an address. Should only be called from the service layer in case of updating a patient.
     * 
     * @param address
     * @return
     * @throws IOException
     * @throws RepositoryException
     * @throws TripleStoreException
     */
    boolean updateAddressRDF(Address address) throws IOException, RepositoryException, TripleStoreException;

    /**
     * Called on createPatient to create all standard concept for this patient.
     * 
     * @throws IOException
     */
    void createStandardConcepts(Patient patient) throws IOException;
}
