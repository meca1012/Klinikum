package de.klinikum.service.Implementation;

import static de.klinikum.domain.NameSpaces.ADDRESS_HAS_ADDRESS_CITY;
import static de.klinikum.domain.NameSpaces.ADDRESS_HAS_ADDRESS_STREET;
import static de.klinikum.domain.NameSpaces.ADDRESS_HAS_ADDRESS_STREET_NUMBER;
import static de.klinikum.domain.NameSpaces.ADDRESS_HAS_ADDRESS_ZIP;
import static de.klinikum.domain.NameSpaces.ADDRESS_HAS_PHONENUMBER;
import static de.klinikum.domain.NameSpaces.ADDRESS_IN_COUNTRY;
import static de.klinikum.domain.NameSpaces.ADDRESS_TYPE;
import static de.klinikum.domain.NameSpaces.PATIENT_HAS_ADDRESS;
import static de.klinikum.domain.NameSpaces.PATIENT_HAS_DATE_OF_BIRTH;
import static de.klinikum.domain.NameSpaces.PATIENT_HAS_FIRST_NAME;
import static de.klinikum.domain.NameSpaces.PATIENT_HAS_LAST_NAME;
import static de.klinikum.domain.NameSpaces.PATIENT_HAS_PATIENT_NUMBER;
import static de.klinikum.domain.NameSpaces.PATIENT_TYPE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.openrdf.model.Literal;
import org.openrdf.model.Model;
import org.openrdf.model.Resource;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.repository.RepositoryException;

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
import de.klinikum.service.Interfaces.ConceptService;
import de.klinikum.service.Interfaces.PatientService;

@Named
public class PatientServiceImpl implements PatientService {

	private String className = "PatientServiceImpl";

	@Inject
	ConceptService conceptService;
	
	@Inject
	SesameTripleStore tripleStore;

	@PostConstruct
	public void afterCreate() {
	}

	@Override
	public Patient getPatientByUri(String patientUri) throws TripleStoreException {
		try {
			Patient returnPatient = new Patient();
			URI patientURI = this.tripleStore.getValueFactory().createURI(patientUri.toString());
			String firstName = this.tripleStore.getObjectString(patientURI.toString(),
					PATIENT_HAS_FIRST_NAME.toString());
			String lastName = this.tripleStore.getObjectString(patientURI.toString(), PATIENT_HAS_LAST_NAME.toString());
			String patientNumber = this.tripleStore.getObjectString(patientURI.toString(),
					PATIENT_HAS_PATIENT_NUMBER.toString());

			Date dateOfBirth = DateUtil.getBirthDateFromString(this.tripleStore.getObjectString(patientURI.toString(),
					PATIENT_HAS_DATE_OF_BIRTH.toString()));

			// Date patientDate = Date.parse(dateOfBirth);

			// if(patient.getAddress() != null) {
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
			return returnPatient;

		} catch (RepositoryException e) {
			e.printStackTrace();
			throw new TripleStoreException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new TripleStoreException(e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new TripleStoreException(e);
		}
	}

	@Override
	public Patient getPatientByPatientNumber(String patientNumber) throws TripleStoreException {
		try {
			URI patientHasPatientNUmber = this.tripleStore.getValueFactory().createURI(
					PATIENT_HAS_PATIENT_NUMBER.toString());
			Literal patientNumer = this.tripleStore.getValueFactory().createLiteral(patientNumber);

			Model statementList;

			statementList = this.tripleStore.getStatementList(null, patientHasPatientNUmber, patientNumer);

			Set<Resource> patientRessourceList = statementList.subjects();

			Patient p = new Patient();

			for (Resource patientRessource : patientRessourceList) {
				p = this.getPatientByUri(patientRessource.toString());
			}
			return p;

		} catch (RepositoryException e) {
			e.printStackTrace();
			throw new TripleStoreException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new TripleStoreException(e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new TripleStoreException(e);
		}
	}

	@Override
	public Address getAddressByUri(Address address) throws TripleStoreException {
		try {
			URI addressURI = this.tripleStore.getValueFactory().createURI(address.getUri().toString());
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
			address.setStreet(addressStreet);
			address.setStreetNumber(addressStreetNumber);
			address.setCity(addressCity);
			address.setZip(addressZip);
			address.setCountry(addressCountry);
			address.setPhone(addressPhone);
			return address;

		} catch (RepositoryException e) {
			e.printStackTrace();
			throw new TripleStoreException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new TripleStoreException(e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new TripleStoreException(e);
		}
	}

	@Override
	public Patient createPatientRDF(Patient patient) throws TripleStoreException {
		try {
			// Get Unique PatientURI Prefix
			URI patientUri = this.tripleStore.getUniqueURI(PATIENT_TYPE.toString());

			// Adding URI to Patient Element
			patient.setUri(patientUri.toString());

			// Creating Person_TYP URI -> Patient is RDF:TYPE Person
			URI personTypeURI = this.tripleStore.getValueFactory().createURI(PATIENT_TYPE.toString());

			this.tripleStore.addTriple(patientUri, RDF.TYPE, personTypeURI);

			// Creates URI pointing to Literals
			URI hasPatientNumber = this.tripleStore.getValueFactory().createURI(PATIENT_HAS_PATIENT_NUMBER.toString());
			URI hasLastName = this.tripleStore.getValueFactory().createURI(PATIENT_HAS_LAST_NAME.toString());
			URI hasFirstName = this.tripleStore.getValueFactory().createURI(PATIENT_HAS_FIRST_NAME.toString());
			URI hasdateOfBirth = this.tripleStore.getValueFactory().createURI(PATIENT_HAS_DATE_OF_BIRTH.toString());

			// Create Literal and Add to Sesame for PatientNumber
			Literal vPatientNumerLiteral = this.tripleStore.getValueFactory().createLiteral(patient.getPatientNumber());
			this.tripleStore.addTriple(patientUri, hasPatientNumber, vPatientNumerLiteral);

			// Create Literal and Add to Sesame for FirstName
			Literal vNameLiteral = this.tripleStore.getValueFactory().createLiteral(patient.getFirstName());
			this.tripleStore.addTriple(patientUri, hasFirstName, vNameLiteral);

			// Create Literal and Add to Sesame for LastName
			Literal nNameLiteral = this.tripleStore.getValueFactory().createLiteral(patient.getLastName());
			this.tripleStore.addTriple(patientUri, hasLastName, nNameLiteral);

			// Create Literal an Add to Sesame for DateOfBirth

			Literal dateOfBirthLiteral = this.tripleStore.getValueFactory().createLiteral(
					patient.getDateOfBirth().toString());
			this.tripleStore.addTriple(patientUri, hasdateOfBirth, dateOfBirthLiteral);

			// check if patient has address, if true call createAddress
			if (patient.getAddress() != null) {
				patient.setAddress(this.createAddressRDF(patient.getAddress()));
				URI hasAddress = this.tripleStore.getValueFactory().createURI(PATIENT_HAS_ADDRESS.toString());
				URI addressURI = this.tripleStore.getValueFactory().createURI(patient.getAddress().getUri().toString());

				// Create Address Element Triple
				this.tripleStore.addTriple(patientUri, hasAddress, addressURI);
			}
			
//			TODO: add createStandardConcepts here
			this.createStandardConcepts(patient);
			
			return patient;

		} catch (IOException e) {
			e.printStackTrace();
			throw new TripleStoreException(e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new TripleStoreException(e);
		}
	}

	@Override
	public Address createAddressRDF(Address address) throws TripleStoreException {
		try {
			URI addressUri = this.tripleStore.getUniqueURI(ADDRESS_TYPE.toString());

			URI addressTypeURI = this.tripleStore.getValueFactory().createURI(ADDRESS_TYPE.toString());

			this.tripleStore.addTriple(addressUri, RDF.TYPE, addressTypeURI);

			URI hasStreet = this.tripleStore.getValueFactory().createURI(ADDRESS_HAS_ADDRESS_STREET.toString());
			URI hasStreetNumber = this.tripleStore.getValueFactory().createURI(
					ADDRESS_HAS_ADDRESS_STREET_NUMBER.toString());
			URI hasZip = this.tripleStore.getValueFactory().createURI(ADDRESS_HAS_ADDRESS_ZIP.toString());
			URI hasCity = this.tripleStore.getValueFactory().createURI(ADDRESS_HAS_ADDRESS_CITY.toString());
			URI hasCountry = this.tripleStore.getValueFactory().createURI(ADDRESS_IN_COUNTRY.toString());
			URI hasPhone = this.tripleStore.getValueFactory().createURI(ADDRESS_HAS_PHONENUMBER.toString());

			// Creating Address Literals
			Literal adressStreetLiteral = this.tripleStore.getValueFactory().createLiteral(address.getStreet());
			Literal adressStreetNumberLiteral = this.tripleStore.getValueFactory().createLiteral(
					address.getStreetNumber());
			Literal adressZipLiteral = this.tripleStore.getValueFactory().createLiteral(address.getZip());
			Literal adressCityLiteral = this.tripleStore.getValueFactory().createLiteral(address.getCity());
			Literal adressCountryLiteral = this.tripleStore.getValueFactory().createLiteral(address.getCountry());
			Literal adressPhoneLiteral = this.tripleStore.getValueFactory().createLiteral(address.getPhone());

			// Adding Literals to Triples
			this.tripleStore.addTriple(addressUri, hasStreet, adressStreetLiteral);
			this.tripleStore.addTriple(addressUri, hasStreetNumber, adressStreetNumberLiteral);
			this.tripleStore.addTriple(addressUri, hasZip, adressZipLiteral);
			this.tripleStore.addTriple(addressUri, hasCity, adressCityLiteral);
			this.tripleStore.addTriple(addressUri, hasCountry, adressCountryLiteral);
			this.tripleStore.addTriple(addressUri, hasPhone, adressPhoneLiteral);

			address.setUri(addressUri.toString());

			return address;

		} catch (IOException e) {
			e.printStackTrace();
			throw new TripleStoreException(e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new TripleStoreException(e);
		}
	}

	// Update Patient
	@Override
	public boolean updatePatientRDF(Patient patient) throws IOException, RepositoryException {

		URI patientURI = this.tripleStore.getValueFactory().createURI(patient.getUri());

		// FirstName
		if (patient.getFirstName() != null && !patient.getFirstName().isEmpty()) {

			URI hasFirstName = this.tripleStore.getValueFactory().createURI(PATIENT_HAS_FIRST_NAME.toString());
			this.tripleStore.removeTriples(patientURI, hasFirstName, null);
			Literal vNameLiteral = this.tripleStore.getValueFactory().createLiteral(patient.getFirstName());
			this.tripleStore.addTriple(patientURI, hasFirstName, vNameLiteral);
		}

		// LastName
		if (patient.getLastName() != null && !patient.getFirstName().isEmpty()) {

			URI hasLastName = this.tripleStore.getValueFactory().createURI(PATIENT_HAS_LAST_NAME.toString());
			this.tripleStore.removeTriples(patientURI, hasLastName, null);
			Literal nNameLiteral = this.tripleStore.getValueFactory().createLiteral(patient.getLastName());
			this.tripleStore.addTriple(patientURI, hasLastName, nNameLiteral);
		}

		// PatientNumber
		if (patient.getPatientNumber() != null && !patient.getPatientNumber().isEmpty()) {

			URI hasPatientNumber = this.tripleStore.getValueFactory().createURI(PATIENT_HAS_PATIENT_NUMBER.toString());
			this.tripleStore.removeTriples(patientURI, hasPatientNumber, null);
			Literal vPatientNumerLiteral = this.tripleStore.getValueFactory().createLiteral(patient.getPatientNumber());
			this.tripleStore.addTriple(patientURI, hasPatientNumber, vPatientNumerLiteral);
		}

		// DateOfBirth
		if (patient.getDateOfBirth() != null) {

			URI hasdateOfBirth = this.tripleStore.getValueFactory().createURI(PATIENT_HAS_DATE_OF_BIRTH.toString());
			this.tripleStore.removeTriples(patientURI, hasdateOfBirth, null);
			Literal dateOfBirthLiteral = this.tripleStore.getValueFactory().createLiteral(
					patient.getDateOfBirth().toString());
			this.tripleStore.addTriple(patientURI, hasdateOfBirth, dateOfBirthLiteral);
		}

		// Address
		if (patient.getAddress() != null) {
			this.updateAddressRDF(patient.getAddress());
		}

		return true;
	}

	// Update Address
	@Override
	public boolean updateAddressRDF(Address address) throws IOException, RepositoryException {

		URI addressURI = this.tripleStore.getValueFactory().createURI(address.getUri());

		// Street
		if (address.getStreet() != null && !address.getStreet().isEmpty()) {

			URI hasStreet = this.tripleStore.getValueFactory().createURI(ADDRESS_HAS_ADDRESS_STREET.toString());
			this.tripleStore.removeTriples(addressURI, hasStreet, null);
			Literal adressStreetLiteral = this.tripleStore.getValueFactory().createLiteral(address.getStreet());
			this.tripleStore.addTriple(addressURI, hasStreet, adressStreetLiteral);
		}

		// StreetNumber
		if (address.getStreetNumber() != null && !address.getStreetNumber().isEmpty()) {

			URI hasStreetNumber = this.tripleStore.getValueFactory().createURI(
					ADDRESS_HAS_ADDRESS_STREET_NUMBER.toString());
			this.tripleStore.removeTriples(addressURI, hasStreetNumber, null);
			Literal adressStreetNumberLiteral = this.tripleStore.getValueFactory().createLiteral(
					address.getStreetNumber());
			this.tripleStore.addTriple(addressURI, hasStreetNumber, adressStreetNumberLiteral);
		}

		// ZIP
		if (address.getZip() != null && !address.getZip().isEmpty()) {

			URI hasZip = this.tripleStore.getValueFactory().createURI(ADDRESS_HAS_ADDRESS_ZIP.toString());
			this.tripleStore.removeTriples(addressURI, hasZip, null);
			Literal adressZipLiteral = this.tripleStore.getValueFactory().createLiteral(address.getZip());
			this.tripleStore.addTriple(addressURI, hasZip, adressZipLiteral);

		}

		// City
		if (address.getCity() != null && !address.getCity().isEmpty()) {

			URI hasCity = this.tripleStore.getValueFactory().createURI(ADDRESS_HAS_ADDRESS_CITY.toString());
			this.tripleStore.removeTriples(addressURI, hasCity, null);
			Literal adressCityLiteral = this.tripleStore.getValueFactory().createLiteral(address.getCity());
			this.tripleStore.addTriple(addressURI, hasCity, adressCityLiteral);
		}

		// Country
		if (address.getCountry() != null && !address.getCountry().isEmpty()) {

			URI hasCountry = this.tripleStore.getValueFactory().createURI(ADDRESS_IN_COUNTRY.toString());
			this.tripleStore.removeTriples(addressURI, hasCountry, null);
			Literal adressCountryLiteral = this.tripleStore.getValueFactory().createLiteral(address.getCountry());
			this.tripleStore.addTriple(addressURI, hasCountry, adressCountryLiteral);
		}

		// Phone
		if (address.getPhone() != null && !address.getPhone().isEmpty()) {

			URI hasPhone = this.tripleStore.getValueFactory().createURI(ADDRESS_HAS_PHONENUMBER.toString());
			this.tripleStore.removeTriples(addressURI, hasPhone, null);
			Literal adressPhoneLiteral = this.tripleStore.getValueFactory().createLiteral(address.getPhone());
			this.tripleStore.addTriple(addressURI, hasPhone, adressPhoneLiteral);
		}

		return true;

	}

	// NOT USED.. TODO: Delete if not needed
	@Override
	public List<Patient> searchPatient(Patient patient) throws TripleStoreException {
		// ReturnList for PatientResults
		List<Patient> returnPatientList = new ArrayList<Patient>();

		// If patientNumber is Set there should be just a single Patient as
		// Re6turnValue in List

		if (patient.getPatientNumber() != null && !patient.getPatientNumber().isEmpty()) {
			// URI for Patient having a PationNumber in RDF
			URI patientHasPatientNumber = this.tripleStore.getValueFactory().createURI(
					PATIENT_HAS_PATIENT_NUMBER.toString());
			// Literals as Value for PatientNumber
			Literal patientNumber = this.tripleStore.getValueFactory().createLiteral(patient.getPatientNumber());

			Model statementList;
			try {
				// Gets all URIs for Patient with PatientNumer
				statementList = this.tripleStore.getStatementList(null, patientHasPatientNumber, patientNumber);

				Set<Resource> patientRessourceList = statementList.subjects();
				for (Resource patientRessource : patientRessourceList) {
					returnPatientList.add(this.getPatientByUri(patientRessource.toString()));
				}

			} catch (RepositoryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return returnPatientList;
		}

		if (patient.getLastName() != null && !patient.getLastName().isEmpty()) {
			// URI for Patient having a PationNumber in RDF
			URI patientHasLastName = this.tripleStore.getValueFactory().createURI(PATIENT_HAS_LAST_NAME.toString());
			// Literals as Value for PatientNumber
			Literal lastname = this.tripleStore.getValueFactory().createLiteral(patient.getLastName());

			Model statementList;
			try {
				// Gets all URIs for Patient with PatientNumer
				statementList = this.tripleStore.getStatementList(null, patientHasLastName, lastname);

				Set<Resource> patientRessourceList = statementList.subjects();
				for (Resource patientRessource : patientRessourceList) {
					returnPatientList.add(this.getPatientByUri(patientRessource.toString()));
				}
			} catch (RepositoryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return returnPatientList;

		}

		// TODO Implementate FirstnameSearch
		if (!patient.getFirstName().isEmpty() || !patient.getFirstName().equals(null)) {
		}

		// TODO Implementate DateOfBirthSearch
		if (!patient.getDateOfBirth().equals(null)) {
		}

		return returnPatientList;
	}

	// Searches Patients with first name , last name, and PatientNumber
	// Uses SPARQL to Query Sesame
	@Override
	public List<Patient> searchPatientSPARQL(Patient patient) throws SpirontoException {
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
	
	/**
	 * adds standard tabConcepts and concepts to patient
	 * @throws IOException 
	 */
	public void createStandardConcepts(Patient patient) throws IOException {

		// creates standard tabConcepts
		for (TabConcepts tc : TabConcepts.values()) {
			Concept tabConcept = new Concept();
			tabConcept.setPatientUri(patient.getUri());
			tabConcept.setLabel(tc.toString());
			this.conceptService.addTabConcept(tabConcept);

			switch (tc) {
			
				case FAMILIE:
					// adds family concepts
					for (FamilyConcepts fc : FamilyConcepts.values()) {
						Concept concept = new Concept();
						concept.setPatientUri(patient.getUri());
						concept.setLabel(fc.toString());
						this.conceptService.addTabConcept(concept);
						this.conceptService.connectSingleConcept(tabConcept,
								concept);
					}
					break;
					
				case KRANKHEITSVERLAUF:
					// adds disease running concepts
					for (DiseaseConcepts dc : DiseaseConcepts.values()) {
						Concept concept = new Concept();
						concept.setPatientUri(patient.getUri());
						concept.setLabel(dc.toString());
						this.conceptService.addConceptToPatient(concept);
						this.conceptService.connectSingleConcept(tabConcept,
								concept);
					}
					break;
					
				case SPIRITUALITAET_RELIGION:					
					// adds spirituality concepts
					for (SpiritualityConcepts sc : SpiritualityConcepts.values()) {
						Concept concept = new Concept();
						concept.setPatientUri(patient.getUri());
						concept.setLabel(sc.toString());
						this.conceptService.addTabConcept(concept);
						this.conceptService.connectSingleConcept(tabConcept,
								concept);
					}
					break;
					
				case HERKUNFT:				
					// adds background concepts
					for (BackgroundConcepts bc : BackgroundConcepts.values()) {
						Concept concept = new Concept();
						concept.setPatientUri(patient.getUri());
						concept.setLabel(bc.toString());
						this.conceptService.addTabConcept(concept);
						this.conceptService.connectSingleConcept(tabConcept,
								concept);
					}
					break;
					
				case SPIRITUAL_CARE_INTERVENTION:
					// adds spiritualy care interventions concepts
					for (SpiritualCareInterventionConcepts sci : SpiritualCareInterventionConcepts
							.values()) {
						Concept concept = new Concept();
						concept.setPatientUri(patient.getUri());
						concept.setLabel(sci.toString());
						this.conceptService.addTabConcept(concept);
						this.conceptService.connectSingleConcept(tabConcept,
								concept);
					}
					break;
				}
		}		
	}
	
	public String getClassName() {
		return this.className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

}
