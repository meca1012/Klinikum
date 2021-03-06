package de.klinikum.service.interfaces;

import java.io.IOException;
import java.util.List;

import org.openrdf.model.util.ModelException;
import org.openrdf.repository.RepositoryException;

import de.klinikum.domain.Concept;
import de.klinikum.domain.Patient;
import de.klinikum.exceptions.SpirontoException;
import de.klinikum.exceptions.TripleStoreException;
/**
 * 
 * @author Andreas Schillinger, Carsten Meiser
 *
 */
public interface ConceptService {

    /**
     * Returns all TabConcepts to a patientUri. Fetches the connectedConcepts of each.
     * 
     * @return
     * @throws IOException
     * @throws SpirontoException
     */
    List<Concept> getTabConcepts(Patient patient) throws IOException, SpirontoException;

    /**
     * Creates a new concept to a patient by his uri. ConnectedConcepts are getting set.
     * 
     * @param concept
     * @return
     * @throws IOException
     * @throws TripleStoreException
     */

    Concept createConcept(Concept concept) throws IOException, TripleStoreException;

    /**
     * Connect a concept to an other concept.
     * 
     * @param from
     * @param to
     * @throws IOException
     * @throws TripleStoreException
     */
    void connectSingleConcept(Concept from, Concept to) throws IOException, TripleStoreException;

    /**
     * Create a new tabConcept to a patient. ConnectedConcepts are getting set.
     * 
     * @param concept
     * @return
     * @throws IOException
     * @throws TripleStoreException
     */
    Concept createTabConcept(Concept concept) throws IOException, TripleStoreException;

    /**
     * Returnes all concept to a concept that are direct connected to it.
     * 
     * @param concept
     * @param onlyUris
     *            -> if set only the uris are set in the concept object.
     * @return
     * @throws IOException
     * @throws SpirontoException
     */
    List<Concept> getDirectConnected(Concept concept, boolean onlyUris) throws IOException, SpirontoException;

    /**
     * Returns all concepts of a patient.
     * 
     * @param patient
     * @return
     * @throws IOException
     * @throws SpirontoException
     */

    List<Concept> findAllConceptsOfPatient(Patient patient) throws IOException, SpirontoException;

    /**
     * Return all concepts that are connected to a tabconcept. Returns null if not tabconcept.
     * 
     * @param tabConcept
     * @return
     * @throws RepositoryException
     * @throws IOException
     * @throws ModelException
     * @throws SpirontoException
     */
    List<Concept> findConceptsOfTabConcept(Concept tabConcept) throws RepositoryException, IOException, ModelException,
            SpirontoException;

    /**
     * Recursive method to return all concepts that are connected to a concept. Gets directly and indirectly connected.
     * Searches bidirectional -> a->b and b->a.
     * 
     * @param conceptUri
     * @param connected
     * @param onlyUris
     * @return
     * @throws RepositoryException
     * @throws IOException
     * @throws ModelException
     * @throws SpirontoException
     */
    List<Concept> getConnected(String conceptUri, List<Concept> connected, boolean onlyUris)
            throws RepositoryException, IOException, ModelException, SpirontoException;

    /**
     * Returns a concept object to a uri. ConnectedConcepts are not getting set.
     * 
     * @param conceptUri
     * @return
     * @throws SpirontoException
     */
    Concept getConceptByUri(String conceptUri) throws RepositoryException, SpirontoException;

    /**
     * Method to connect one concept to a list of other concepts. Uses the connectSingle method.
     * 
     * @param from
     *            -> concept which gets connected to the to list
     * @param to
     *            -> List of concepts to whose the from concept gets connected
     * @throws IOException
     * @throws TripleStoreException
     */
    void connectMultipleConcepts(Concept from, List<Concept> to) throws IOException, TripleStoreException;

    /**
     * Checks wether a concept is a tabConcept or not
     * 
     * @param concept
     * @return -> boolean, true if concept is a tabConcept
     * @throws RepositoryException
     * @throws IOException
     */
    boolean isTabConcept(Concept concept) throws RepositoryException, IOException;

    /**
     * Used to return only the uris of the direct connected concepts to a concept.
     * 
     * @param concept
     * @return -> List of Concepts with only the uri set
     * @throws RepositoryException
     * @throws ModelException
     * @throws IOException
     * @throws SpirontoException
     */
    List<Concept> getConnectedConceptUris(Concept concept) throws RepositoryException, ModelException, IOException,
            SpirontoException;

    /**
     * Updates a concept. If connectedConcepts are set, all existing links to other concepts are being removed and the
     * new ones are set. The connectedConcepts themselves are not getting updated..
     * 
     * @throws ModelException
     * @throws RepositoryException
     */
    Concept updateConcept(Concept concept) throws SpirontoException, IOException, RepositoryException, ModelException;

    /**
     * Checks whether a concept exists.
     * 
     * @param conceptUri
     * @return
     * @throws IOException
     */
    boolean conceptExists(String conceptUri) throws IOException;

    /**
     * Checks whether a patient exists.
     * 
     * @param patientUri
     * @return
     * @throws IOException
     */
    boolean patientExists(String patientUri) throws IOException;

}
