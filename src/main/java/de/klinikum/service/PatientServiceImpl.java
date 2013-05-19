package de.klinikum.service;
import static de.klinikum.domain.NameSpaces.PATIENT_HAS_PATIENT_NUMBER;
import static de.klinikum.domain.NameSpaces.PATIENT_HAS_LAST_NAME;
import static de.klinikum.domain.NameSpaces.PATIENT_HAS_FIRST_NAME;
import static de.klinikum.domain.NameSpaces.PATIENT_HAS_DATE_OF_BIRTH;
import static de.klinikum.domain.NameSpaces.ADDRESS_TYPE;
import static de.klinikum.domain.NameSpaces.PATIENT_HAS_ADDRESS;
import static de.klinikum.domain.NameSpaces.ADDRESS_HAS_ADDRESS_STREET;
import static de.klinikum.domain.NameSpaces.ADDRESS_HAS_ADDRESS_ZIP;
import static de.klinikum.domain.NameSpaces.ADDRESS_HAS_ADDRESS_CITY;
import static de.klinikum.domain.NameSpaces.ADDRESS_IN_COUNTRY;
import static de.klinikum.domain.NameSpaces.ADDRESS_HAS_PHONENUMBER;
import static de.klinikum.domain.NameSpaces.PATIENT_TYPE;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.openrdf.model.Resource;
import org.openrdf.model.Literal;
import org.openrdf.model.Model;
import org.openrdf.model.URI;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.repository.RepositoryException;
import de.klinikum.domain.Address;
import de.klinikum.domain.Patient;

import de.klinikum.persistence.SesameTripleStore;

@Named
public class PatientServiceImpl implements PatientService {
	private String className = "PatientService";	
	
	@Inject
	SesameTripleStore tripleStore;
	
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@PostConstruct
    public void afterCreate() {
        System.out.println("PatientService created");
    }
	
//	public Patient getPatientPatientnumber(String patientNumber) {
//		Patient p1 = new Patient("de.spironto/patient/" + patientNumber, "Max", "Power");
//		return p1;
//	}
	
	public Patient getPatientByUri(Patient patient) throws RepositoryException, IOException {
		URI patientURI = this.tripleStore.getValueFactory().createURI(patient.getUri().toString());
		String firstName = this.tripleStore.getObjectString(patientURI.toString(), PATIENT_HAS_FIRST_NAME.toString());
		String lastName = this.tripleStore.getObjectString(patientURI.toString(), PATIENT_HAS_FIRST_NAME.toString());	
		String patientNumber = this.tripleStore.getObjectString(patientURI.toString(), PATIENT_HAS_PATIENT_NUMBER.toString());	
		//if(patient.getAddress() != null) {
			patient.setAddress(new Address());
			String addressUri = this.tripleStore.getObjectString(patientURI.toString(), PATIENT_HAS_ADDRESS.toString());
			patient.getAddress().setUri(addressUri);
			patient.setAddress(getAddressByUri(patient.getAddress()));
		//}		
		patient.setPatientNumber(patientNumber);
		patient.setFirstName(firstName);
		patient.setLastName(lastName);
		return patient;		
	}
	
	
	public Patient getPatientByPatientNumber(String patientNumber) throws RepositoryException, IOException {
	
		URI patientHasPatientNUmber = this.tripleStore.getValueFactory().createURI(PATIENT_HAS_PATIENT_NUMBER.toString());
		Literal patientNumer = this.tripleStore.getValueFactory().createLiteral(patientNumber);
		
		Model statementList = this.tripleStore.getStatementList(null, patientHasPatientNUmber , patientNumer);
		Set<Resource> patientRessourceList = statementList.subjects();

		Patient p = new Patient();
		
		for (Resource patientRessource : patientRessourceList) {
			String pUri = patientRessource.toString();
			
			p.setUri(pUri);
			p = this.getPatientByUri(p);
			}
		return p;
		}

	
	public Address getAddressByUri(Address address) throws RepositoryException, IOException {
		URI addressURI = this.tripleStore.getValueFactory().createURI(address.getUri().toString());
		String addressStreet = this.tripleStore.getObjectString(addressURI.toString(), ADDRESS_HAS_ADDRESS_STREET.toString());
		String addressCity = this.tripleStore.getObjectString(addressURI.toString(), ADDRESS_HAS_ADDRESS_CITY.toString());
		String addressZip = this.tripleStore.getObjectString(addressURI.toString(), ADDRESS_HAS_ADDRESS_ZIP.toString());
		String addressCountry = this.tripleStore.getObjectString(addressURI.toString(), ADDRESS_IN_COUNTRY.toString());
		String addressPhone = this.tripleStore.getObjectString(addressURI.toString(), ADDRESS_HAS_PHONENUMBER.toString());
		address.setStreet(addressStreet);
		address.setCity(addressCity);
		address.setZip(addressZip);
		address.setCountry(addressCountry);
		address.setPhone(addressPhone);
		return address;
	}


	
	@Override
	public Patient createPatientRDF(Patient patient) throws IOException {
		
		//Get Unique PatientURI Prefix
		URI patientUri = this.tripleStore.getUniqueURI(PATIENT_TYPE.toString());		
		
		//Adding URI to Patient Element
		patient.setUri(patientUri.toString());		
		
		//Creating Person_TYP URI -> Patient is RDF:TYPE Person
		URI personTypeURI = this.tripleStore.getValueFactory().createURI(PATIENT_TYPE.toString());
		
		this.tripleStore.addTriple(patientUri, RDF.TYPE, personTypeURI);
		
		//Creates URI pointing to Literals
		URI hasPatientNumber = this.tripleStore.getValueFactory().createURI(PATIENT_HAS_PATIENT_NUMBER.toString());
		URI hasLastName = this.tripleStore.getValueFactory().createURI(PATIENT_HAS_LAST_NAME.toString());
		URI hasFirstName = this.tripleStore.getValueFactory().createURI(PATIENT_HAS_FIRST_NAME.toString());
		URI hasdateOfBirth = this.tripleStore.getValueFactory().createURI(PATIENT_HAS_DATE_OF_BIRTH.toString());		
		
		//Create Literal and Add to Sesame for PatientNumber
		Literal vPatientNumerLiteral = this.tripleStore.getValueFactory().createLiteral(patient.getPatientNumber());
		this.tripleStore.addTriple(patientUri, hasPatientNumber, vPatientNumerLiteral);
		
		//Create Literal and Add to Sesame for FirstName
		Literal vNameLiteral = this.tripleStore.getValueFactory().createLiteral(patient.getFirstName());
		this.tripleStore.addTriple(patientUri, hasFirstName, vNameLiteral);
		
		//Create Literal and Add to Sesame for LastName
		Literal nNameLiteral = this.tripleStore.getValueFactory().createLiteral(patient.getLastName());
		this.tripleStore.addTriple(patientUri, hasLastName, nNameLiteral);
		
		
		//Create Literal an Add to Sesame for DateOfBirth
		Literal dateOfBirthLiteral = this.tripleStore.getValueFactory().createLiteral(patient.getDateOfBirth().toString());
		this.tripleStore.addTriple(patientUri, hasdateOfBirth, dateOfBirthLiteral);
		
		//check if patient has address, if true call createAddress
		if(patient.getAddress() != null) {
			patient.setAddress(createAddressRDF(patient.getAddress()));
			URI hasAddress = this.tripleStore.getValueFactory().createURI(PATIENT_HAS_ADDRESS.toString());
			URI addressURI = this.tripleStore.getValueFactory().createURI(patient.getAddress().getUri().toString());
			
			//Create Address Element Triple
			this.tripleStore.addTriple(patientUri, hasAddress, addressURI);
		}		
		return patient;
	}
	
	public Address createAddressRDF(Address address) throws IOException {
		
		URI addressUri = this.tripleStore.getUniqueURI(ADDRESS_TYPE.toString());
		
		URI addressTypeURI = this.tripleStore.getValueFactory().createURI(ADDRESS_TYPE.toString());
		
		this.tripleStore.addTriple(addressUri, RDF.TYPE, addressTypeURI);
		
		URI hasStreet = this.tripleStore.getValueFactory().createURI(ADDRESS_HAS_ADDRESS_STREET.toString());
		URI hasZip = this.tripleStore.getValueFactory().createURI(ADDRESS_HAS_ADDRESS_ZIP.toString());
		URI hasCity = this.tripleStore.getValueFactory().createURI(ADDRESS_HAS_ADDRESS_CITY.toString());
		URI hasCountry = this.tripleStore.getValueFactory().createURI(ADDRESS_IN_COUNTRY.toString());
		URI hasPhone = this.tripleStore.getValueFactory().createURI(ADDRESS_HAS_PHONENUMBER.toString());
		
		//Creating Address Literals
		Literal adressStreetLiteral = this.tripleStore.getValueFactory().createLiteral(address.getStreet());
		Literal adressZipLiteral = this.tripleStore.getValueFactory().createLiteral(address.getZip());
		Literal adressCityLiteral = this.tripleStore.getValueFactory().createLiteral(address.getCity());
		Literal adressCountryLiteral = this.tripleStore.getValueFactory().createLiteral(address.getCountry());
		Literal adressPhoneLiteral = this.tripleStore.getValueFactory().createLiteral(address.getPhone());
		
		//Adding Literals to Triples
		this.tripleStore.addTriple(addressUri, hasStreet, adressStreetLiteral);
		this.tripleStore.addTriple(addressUri, hasZip, adressZipLiteral);
		this.tripleStore.addTriple(addressUri, hasCity, adressCityLiteral);
		this.tripleStore.addTriple(addressUri, hasCountry, adressCountryLiteral);
		this.tripleStore.addTriple(addressUri, hasPhone, adressPhoneLiteral);
		
		address.setUri(addressUri.toString());
		
		return address;
	}
	

	@Override
	public List<Patient> searchPatient(Patient patient) {
		//ReturnList for PatientResults
		List<Patient> returnPatientList = new ArrayList<Patient>();
		
		//If patientNumber is Set there should be just a single Patient as ReturnValue in List
		if(!patient.getPatientNumber().isEmpty() || !patient.getPatientNumber().equals(null)) {
			//URI for Patient having a PationNumber in RDF
			URI patientHasPatientNumber = this.tripleStore.getValueFactory().createURI(PATIENT_HAS_PATIENT_NUMBER.toString());
			//Literals as Value for PatientNumber
			Literal patientNumber = this.tripleStore.getValueFactory().createLiteral(patient.getPatientNumber());
						
			Model statementList;
			try {
				//Gets als URIs for Patient with PatientNumer
				statementList = this.tripleStore.getStatementList(null, patientHasPatientNumber , patientNumber);
				Set<Resource> patientRessourceList = statementList.subjects();
				for (Resource patientRessource : patientRessourceList) {
					Patient p = new Patient();
					p.setUri(patientRessource.toString());
					returnPatientList.add(this.getPatientByUri(p));
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
				
		//TODO: Implementate LastNameSearch
		if(!patient.getLastName().isEmpty() || !patient.getLastName().equals(null)) {}
		
		//TODO Implementate FirstnameSearch
		if(!patient.getFirstName().isEmpty() || !patient.getFirstName().equals(null)) {}
		
		//TODO Implementate DateOfBirthSearch
		if(!patient.getDateOfBirth().equals(null)) {}
		
		return returnPatientList;
	}
	
	public List<Patient> searchPatientSPARQL(Patient patient) {
		String queryString = "";
		queryString = "SELECT ?patient WHERE {?patient " + RDF.TYPE + "" + "}";
		return null;
	}
	
	public List<Patient> searchPatientByName(Patient patient) {
		
		return null;
	}
	
}
