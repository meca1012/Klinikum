package de.klinikum.service.interfaces;

import java.io.IOException;
import java.util.List;

import org.openrdf.repository.RepositoryException;

import de.klinikum.domain.Address;
import de.klinikum.domain.Patient;
import de.klinikum.exceptions.SpirontoException;
import de.klinikum.exceptions.TripleStoreException;
/**
 * 
 * @author Andreas Schillinger, Carsten Meiser, Constantin Treiber, Ivan Tepeluk, Matthias Schwarzenbach
 *
 */
public interface PatientService {

    /**
     * Returns a Patient to a patientUri
     * 
     * @param patientUri
     * @return
     * @throws TripleStoreException
     * @throws SpirontoException 
     */
    Patient getPatientByUri(String patientUri) throws TripleStoreException, SpirontoException;

    /**
     * Creates a new Patient. If the patientNumber is already in use, null is returned.
     * 
     * @param patient
     *            with address
     * @return
     * @throws TripleStoreException
     * @throws Exception 
     */
    Patient createPatientRDF(Patient patient) throws TripleStoreException, Exception;

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
     * @throws SpirontoException 
     */
    boolean updatePatientRDF(Patient patient) throws IOException, RepositoryException, TripleStoreException, SpirontoException;

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
     * @throws SpirontoException 
     */
    Patient getPatientByPatientNumber(String patientNumber) throws TripleStoreException, SpirontoException;

    /**
     * Creates a new address. Should only be called on the service layer.
     * 
     * @param address
     * @return
     * @throws TripleStoreException
     * @throws Exception 
     */
    Address createAddressRDF(Address address) throws TripleStoreException, Exception;

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
     * Checks whether a patient exists.
     * 
     * @param patientUri
     * @return
     * @throws IOException
     */
    boolean patientExists(String patientUri) throws IOException;

    /**
     * Called on createPatient to create all standard concept for this patient.
     * 
     * @throws IOException
     * @throws TripleStoreException
     */
    void createStandardConcepts(Patient patient) throws IOException, TripleStoreException;
}
