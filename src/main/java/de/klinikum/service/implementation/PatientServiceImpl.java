package de.klinikum.service.implementation;

import static de.klinikum.persistence.NameSpaces.ADDRESS_HAS_ADDRESS_CITY;
import static de.klinikum.persistence.NameSpaces.ADDRESS_HAS_ADDRESS_STREET;
import static de.klinikum.persistence.NameSpaces.ADDRESS_HAS_ADDRESS_STREET_NUMBER;
import static de.klinikum.persistence.NameSpaces.ADDRESS_HAS_ADDRESS_ZIP;
import static de.klinikum.persistence.NameSpaces.ADDRESS_HAS_PHONENUMBER;
import static de.klinikum.persistence.NameSpaces.ADDRESS_IN_COUNTRY;
import static de.klinikum.persistence.NameSpaces.ADDRESS_TYPE;
import static de.klinikum.persistence.NameSpaces.PATIENT_HAS_ADDRESS;
import static de.klinikum.persistence.NameSpaces.PATIENT_HAS_DATE_OF_BIRTH;
import static de.klinikum.persistence.NameSpaces.PATIENT_HAS_FIRST_NAME;
import static de.klinikum.persistence.NameSpaces.PATIENT_HAS_LAST_NAME;
import static de.klinikum.persistence.NameSpaces.PATIENT_HAS_PATIENT_NUMBER;
import static de.klinikum.persistence.NameSpaces.PATIENT_TYPE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.joda.time.DateTime;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.repository.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.klinikum.domain.Address;
import de.klinikum.domain.Concept;
import de.klinikum.domain.Patient;
import de.klinikum.domain.enums.BackgroundConcepts;
import de.klinikum.domain.enums.DiseaseConcepts;
import de.klinikum.domain.enums.FamilyConcepts;
import de.klinikum.domain.enums.SpiritualCareInterventionConcepts;
import de.klinikum.domain.enums.SpiritualityConcepts;
import de.klinikum.domain.enums.TabConcepts;
import de.klinikum.exceptions.SpirontoException;
import de.klinikum.exceptions.TripleStoreException;
import de.klinikum.helper.DateUtil;
import de.klinikum.persistence.SesameTripleStore;
import de.klinikum.service.interfaces.ConceptService;
import de.klinikum.service.interfaces.PatientService;

/**
 * 
 * @author Andreas Schillinger, Carsten Meiser, Constantin Treiber, Ivan Tepeluk, Matthias Schwarzenbach
 * 
 */
@Named
public class PatientServiceImpl implements PatientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PatientServiceImpl.class);

    @Inject
    ConceptService conceptService;

    @Inject
    SesameTripleStore tripleStore;

    @Override
    public Patient getPatientByUri(String patientUri) throws SpirontoException {

        Patient returnPatient = new Patient();
        try {
            URI patientURI = this.tripleStore.getValueFactory().createURI(patientUri.toString());
            String firstName = this.tripleStore.getObjectString(patientURI.toString(),
                    PATIENT_HAS_FIRST_NAME.toString());
            String lastName = this.tripleStore.getObjectString(patientURI.toString(), PATIENT_HAS_LAST_NAME.toString());
            String patientNumber = this.tripleStore.getObjectString(patientURI.toString(),
                    PATIENT_HAS_PATIENT_NUMBER.toString());

            DateTime dateOfBirth = DateUtil.getDateTimeFromString(this.tripleStore.getObjectString(
                    patientURI.toString(), PATIENT_HAS_DATE_OF_BIRTH.toString()));

            returnPatient.setAddress(new Address());
            String addressUri = this.tripleStore.getObjectString(patientURI.toString(), PATIENT_HAS_ADDRESS.toString());
            returnPatient.getAddress().setUri(addressUri);
            returnPatient.setAddress(this.getAddressByUri(returnPatient.getAddress()));
            // }
            returnPatient.setUri(patientURI.toString());
            returnPatient.setPatientNumber(patientNumber);
            returnPatient.setFirstName(firstName);
            returnPatient.setLastName(lastName);
            returnPatient.setDateOfBirth(dateOfBirth);

            if (returnPatient.getUri() == null) {
                throw new TripleStoreException("Patient with the uri " + patientUri + " was not found!");
            }
        }
        catch (RepositoryException e) {
            e.printStackTrace();
            throw new SpirontoException(e);
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new SpirontoException(e);
        }
        return returnPatient;
    }

    @Override
    public Patient getPatientByPatientNumber(String patientNumber) throws SpirontoException {

        List<Patient> patientToReturn = new ArrayList<Patient>();

        String sparqlQuery = "SELECT DISTINCT ?Uri WHERE {";

        sparqlQuery += "?Uri <" + PATIENT_HAS_PATIENT_NUMBER + ">\"" + patientNumber + "\"";

        sparqlQuery += "}";

        Set<HashMap<String, Value>> result = this.tripleStore.executeSelectSPARQLQuery(sparqlQuery);

        for (HashMap<String, Value> item : result) {
            Patient patient = new Patient();
            String patientUri = item.get("Uri").stringValue();
            patient = getPatientByUri(patientUri);
            patientToReturn.add(patient);
        }
        if (patientToReturn.size() > 1) {
            throw new TripleStoreException("Multiple patients with the patient number " + patientNumber + " were found!");
        }

        if (!patientToReturn.isEmpty()) {
            return patientToReturn.get(0);
        } else {
            return null;
        }
        
    }

    @Override
    public Address getAddressByUri(Address address) throws TripleStoreException {

        Address addressToReturn = new Address();
        try {
            URI addressURI = this.tripleStore.getValueFactory().createURI(address.getUri());
            String addressStreet = this.tripleStore.getObjectString(addressURI.toString(),
                    ADDRESS_HAS_ADDRESS_STREET.toString());
            String addressStreetNumber = this.tripleStore.getObjectString(addressURI.toString(),
                    ADDRESS_HAS_ADDRESS_STREET_NUMBER.toString());
            String addressCity = this.tripleStore.getObjectString(addressURI.toString(),
                    ADDRESS_HAS_ADDRESS_CITY.toString());
            String addressZip = this.tripleStore.getObjectString(addressURI.toString(),
                    ADDRESS_HAS_ADDRESS_ZIP.toString());
            String addressCountry = this.tripleStore.getObjectString(addressURI.toString(),
                    ADDRESS_IN_COUNTRY.toString());
            String addressPhone = this.tripleStore.getObjectString(addressURI.toString(),
                    ADDRESS_HAS_PHONENUMBER.toString());
            addressToReturn.setUri(address.getUri());
            addressToReturn.setStreet(addressStreet);
            addressToReturn.setStreetNumber(addressStreetNumber);
            addressToReturn.setCity(addressCity);
            addressToReturn.setZip(addressZip);
            addressToReturn.setCountry(addressCountry);
            addressToReturn.setPhone(addressPhone);
        }
        catch (RepositoryException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return addressToReturn;
    }

    @Override
    public Patient createPatientRDF(Patient patient) throws Exception, TripleStoreException {
        try {
            if (getPatientByPatientNumber(patient.getPatientNumber()) != null) {
                throw new TripleStoreException("Patientnumber " + patient.getPatientNumber() + " already exists!");
            }

            // Get Unique PatientURI Prefix
            URI patientUri = this.tripleStore.getUniqueURI(PATIENT_TYPE.toString());

            // Adding URI to Patient Element
            patient.setUri(patientUri.toString());

            this.tripleStore.addTriple(patientUri.toString(), RDF.TYPE.toString(), PATIENT_TYPE.toString());

            this.tripleStore.addTripleWithStringLiteral(patient.getUri(), PATIENT_HAS_PATIENT_NUMBER.toString(),
                    patient.getPatientNumber());
            this.tripleStore.addTripleWithStringLiteral(patient.getUri(), PATIENT_HAS_LAST_NAME.toString(),
                    patient.getLastName());
            this.tripleStore.addTripleWithStringLiteral(patient.getUri(), PATIENT_HAS_FIRST_NAME.toString(),
                    patient.getFirstName());
            this.tripleStore.addTripleWithStringLiteral(patient.getUri(), PATIENT_HAS_DATE_OF_BIRTH.toString(), patient
                    .getDateOfBirth().toString());

            // check if patient has address, if true call createAddress
            if (patient.getAddress() != null) {
                patient.setAddress(this.createAddressRDF(patient.getAddress()));

                // Create Address Element Triple
                this.tripleStore.addTriple(patient.getUri(), PATIENT_HAS_ADDRESS.toString(), patient.getAddress()
                        .getUri());
            }
            this.createStandardConcepts(patient);
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new Exception(e);
        }
        return patient;
    }

    @Override
    public Address createAddressRDF(Address address) throws Exception, TripleStoreException {
        try {
            URI addressUri = this.tripleStore.getUniqueURI(ADDRESS_TYPE.toString());

            address.setUri(addressUri.toString());

            this.tripleStore.addTriple(address.getUri(), RDF.TYPE.toString(), ADDRESS_TYPE.toString());

            this.tripleStore.addTripleWithStringLiteral(address.getUri(), ADDRESS_HAS_ADDRESS_STREET.toString(),
                    address.getStreet());
            this.tripleStore.addTripleWithStringLiteral(address.getUri(), ADDRESS_HAS_ADDRESS_STREET_NUMBER.toString(),
                    address.getStreetNumber());
            this.tripleStore.addTripleWithStringLiteral(address.getUri(), ADDRESS_HAS_ADDRESS_ZIP.toString(),
                    address.getZip());
            this.tripleStore.addTripleWithStringLiteral(address.getUri(), ADDRESS_HAS_ADDRESS_CITY.toString(),
                    address.getCity());
            this.tripleStore.addTripleWithStringLiteral(address.getUri(), ADDRESS_IN_COUNTRY.toString(),
                    address.getCountry());
            this.tripleStore.addTripleWithStringLiteral(address.getUri(), ADDRESS_HAS_PHONENUMBER.toString(),
                    address.getPhone());

            return address;

        }
        catch (IOException e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }

    @Override
    public boolean updatePatientRDF(Patient patient) throws IOException, RepositoryException, SpirontoException {

        if (patient == null) {
            throw new TripleStoreException("Patient is null!");
        }

        if (patient.getUri() == null) {
            throw new TripleStoreException("Patient uri is null!");
        }

        if (!patientExists(patient.getUri())) {
            throw new TripleStoreException("Patient with the uri \"" + patient.getUri() + "\" already exists!");
        }

        Patient existingPatient = getPatientByUri(patient.getUri());

        // FirstName
        if (patient.getFirstName() != null && !patient.getFirstName().isEmpty()) {
            if (!patient.getFirstName().equals(existingPatient.getFirstName())) {
                this.tripleStore.removeTriples(existingPatient.getUri(), PATIENT_HAS_FIRST_NAME.toString(), null);
                this.tripleStore.addTripleWithStringLiteral(patient.getUri(), PATIENT_HAS_FIRST_NAME.toString(),
                        patient.getFirstName());
            }
        }

        // LastName
        if (patient.getLastName() != null && !patient.getLastName().isEmpty()) {
            if (!patient.getLastName().equals(existingPatient.getLastName())) {
                this.tripleStore.removeTriples(existingPatient.getUri(), PATIENT_HAS_LAST_NAME.toString(), null);
                this.tripleStore.addTripleWithStringLiteral(patient.getUri(), PATIENT_HAS_LAST_NAME.toString(),
                        patient.getLastName());
            }
        }

        // PatientNumber
        if (patient.getPatientNumber() != null && !patient.getPatientNumber().isEmpty()) {
            if (!patient.getPatientNumber().equals(existingPatient.getPatientNumber())) {
                this.tripleStore.removeTriples(existingPatient.getUri(), PATIENT_HAS_PATIENT_NUMBER.toString(), null);
                this.tripleStore.addTripleWithStringLiteral(patient.getUri(), PATIENT_HAS_PATIENT_NUMBER.toString(),
                        patient.getPatientNumber());
            }
        }

        // DateOfBirth
        if (patient.getDateOfBirth() != null) {
            if (!patient.getDateOfBirth().equals(existingPatient.getDateOfBirth())) {
                this.tripleStore.removeTriples(existingPatient.getUri(), PATIENT_HAS_DATE_OF_BIRTH.toString(), null);
                this.tripleStore.addTripleWithStringLiteral(patient.getUri(), PATIENT_HAS_DATE_OF_BIRTH.toString(),
                        patient.getDateOfBirth().toString());
            }
        }

        // Address
        if (patient.getAddress() != null) {
            this.updateAddressRDF(patient.getAddress());
        }

        return true;
    }

    @Override
    public boolean updateAddressRDF(Address address) throws IOException, RepositoryException, TripleStoreException {

        if (address.getUri() == null) {
            throw new TripleStoreException("Address uri is null!");
        }

        Address existingAddress = getAddressByUri(address);

        // Street
        if (address.getStreet() != null && !address.getStreet().isEmpty()) {
            if (!address.getStreet().equals(existingAddress.getStreet())) {
                this.tripleStore.removeTriples(existingAddress.getUri(), ADDRESS_HAS_ADDRESS_STREET.toString(), null);
                this.tripleStore.addTripleWithStringLiteral(address.getUri(), ADDRESS_HAS_ADDRESS_STREET.toString(),
                        address.getStreet());
            }
        }

        // StreetNumber
        if (address.getStreetNumber() != null && !address.getStreetNumber().isEmpty()) {
            if (!address.getStreetNumber().equals(existingAddress.getStreetNumber())) {
                this.tripleStore.removeTriples(existingAddress.getUri(), ADDRESS_HAS_ADDRESS_STREET_NUMBER.toString(),
                        null);
                this.tripleStore.addTripleWithStringLiteral(address.getUri(),
                        ADDRESS_HAS_ADDRESS_STREET_NUMBER.toString(), address.getStreetNumber());
            }
        }

        // ZIP
        if (address.getZip() != null && !address.getZip().isEmpty()) {
            if (!address.getZip().equals(existingAddress.getZip())) {
                this.tripleStore.removeTriples(existingAddress.getUri(), ADDRESS_HAS_ADDRESS_ZIP.toString(), null);
                this.tripleStore.addTripleWithStringLiteral(address.getUri(), ADDRESS_HAS_ADDRESS_ZIP.toString(),
                        address.getZip());
            }
        }

        // City
        if (address.getCity() != null && !address.getCity().isEmpty()) {
            if (!address.getCity().equals(existingAddress.getCity())) {
                this.tripleStore.removeTriples(existingAddress.getUri(), ADDRESS_HAS_ADDRESS_CITY.toString(), null);
                this.tripleStore.addTripleWithStringLiteral(address.getUri(), ADDRESS_HAS_ADDRESS_CITY.toString(),
                        address.getCity());
            }
        }

        // Country
        if (address.getCountry() != null && !address.getCountry().isEmpty()) {
            if (!address.getCountry().equals(existingAddress.getCountry())) {
                this.tripleStore.removeTriples(existingAddress.getUri(), ADDRESS_IN_COUNTRY.toString(), null);
                this.tripleStore.addTripleWithStringLiteral(address.getUri(), ADDRESS_IN_COUNTRY.toString(),
                        address.getCountry());
            }
        }

        // Phone
        if (address.getPhone() != null && !address.getPhone().isEmpty()) {
            if (!address.getPhone().equals(existingAddress.getPhone())) {
                this.tripleStore.removeTriples(existingAddress.getUri(), ADDRESS_HAS_PHONENUMBER.toString(), null);
                this.tripleStore.addTripleWithStringLiteral(address.getUri(), ADDRESS_HAS_PHONENUMBER.toString(),
                        address.getPhone());
            }
        }

        return true;

    }

    @Override
    public List<Patient> searchPatientSPARQL(Patient patient) throws SpirontoException, TripleStoreException {
        List<Patient> returnPatientList = new ArrayList<Patient>();
        String sparqlQuery = "SELECT ?Uri WHERE {";

        if (patient.getPatientNumber() != null && !patient.getPatientNumber().isEmpty()) {
            sparqlQuery += "?Uri <" + PATIENT_HAS_PATIENT_NUMBER + ">\"" + patient.getPatientNumber() + "\". ";
        }
        if (patient.getLastName() != null && !patient.getLastName().isEmpty()) {
            sparqlQuery += "?Uri <" + PATIENT_HAS_LAST_NAME + ">\"" + patient.getLastName() + "\". ";
        }
        if (patient.getFirstName() != null && !patient.getFirstName().isEmpty()) {
            sparqlQuery += "?Uri <" + PATIENT_HAS_FIRST_NAME + ">\"" + patient.getFirstName() + "\". ";
        }

        if (patient.getDateOfBirth() != null) {

            sparqlQuery += "?Uri <" + PATIENT_HAS_DATE_OF_BIRTH + ">\"" + patient.getDateOfBirth().toString() + "\"";
        }

        sparqlQuery += "}";

        Set<HashMap<String, Value>> result = this.tripleStore.executeSelectSPARQLQuery(sparqlQuery);

        for (HashMap<String, Value> item : result) {
            returnPatientList.add(this.getPatientByUri(item.get("Uri").toString()));
        }

        return returnPatientList;
    }

    @Override
    public boolean patientExists(String patientUri) throws IOException {
        return this.tripleStore.repositoryHasStatement(patientUri, RDF.TYPE.toString(), PATIENT_TYPE.toString());
    }

    @Override
    public void createStandardConcepts(Patient patient) throws IOException, TripleStoreException {
        LOGGER.info("Starts creating the standard concept.");

        for (TabConcepts tc : TabConcepts.values()) {
            Concept tabConcept = new Concept();
            tabConcept.setPatientUri(patient.getUri());
            tabConcept.setLabel(tc.toString());
            tabConcept.setEditable(false);
            this.conceptService.createTabConcept(tabConcept);

            switch (tc) {

            case FAMILY:
                // adds family concepts
                for (FamilyConcepts fc : FamilyConcepts.values()) {
                    Concept concept = new Concept();
                    concept.setPatientUri(patient.getUri());
                    concept.setLabel(fc.toString());
                    concept.setEditable(false);
                    this.conceptService.createConcept(concept);
                    this.conceptService.connectSingleConcept(tabConcept, concept);
                }
                break;

            case COURSEOFDISEASE:
                // adds disease running concepts
                for (DiseaseConcepts dc : DiseaseConcepts.values()) {
                    Concept concept = new Concept();
                    concept.setPatientUri(patient.getUri());
                    concept.setLabel(dc.toString());
                    concept.setEditable(false);
                    this.conceptService.createConcept(concept);
                    this.conceptService.connectSingleConcept(tabConcept, concept);
                }
                break;

            case SPIRITUALITY_RELIGION:
                // adds spirituality concepts
                for (SpiritualityConcepts sc : SpiritualityConcepts.values()) {
                    Concept concept = new Concept();
                    concept.setPatientUri(patient.getUri());
                    concept.setLabel(sc.toString());
                    concept.setEditable(false);
                    this.conceptService.createConcept(concept);
                    this.conceptService.connectSingleConcept(tabConcept, concept);
                }
                break;

            case ORIGIN:
                // adds background concepts
                for (BackgroundConcepts bc : BackgroundConcepts.values()) {
                    Concept concept = new Concept();
                    concept.setPatientUri(patient.getUri());
                    concept.setLabel(bc.toString());
                    concept.setEditable(false);
                    this.conceptService.createConcept(concept);
                    this.conceptService.connectSingleConcept(tabConcept, concept);
                }
                break;

            case SPIRITUAL_CARE_INTERVENTION:
                // adds spiritualy care interventions concepts
                for (SpiritualCareInterventionConcepts sci : SpiritualCareInterventionConcepts.values()) {
                    Concept concept = new Concept();
                    concept.setPatientUri(patient.getUri());
                    concept.setLabel(sci.toString());
                    concept.setEditable(false);
                    this.conceptService.createConcept(concept);
                    this.conceptService.connectSingleConcept(tabConcept, concept);
                }
                break;
            }
        }
    }
}
