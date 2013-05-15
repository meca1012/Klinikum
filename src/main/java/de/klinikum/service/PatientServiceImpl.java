package de.klinikum.service;
import static de.klinikum.domain.NameSpaces.PERSON_TYPE;
import static de.klinikum.domain.NameSpaces.PERSON_HAS_NNAME;
import static de.klinikum.domain.NameSpaces.PERSON_HAS_VNAME;
import static de.klinikum.domain.NameSpaces.ADDRESS_TYPE;
import static de.klinikum.domain.NameSpaces.PERSON_HAS_ADDRESS;
import static de.klinikum.domain.NameSpaces.ADDRESS_HAS_ADDRESS_STREET;
import static de.klinikum.domain.NameSpaces.ADDRESS_HAS_ADDRESS_ZIP;
import static de.klinikum.domain.NameSpaces.ADDRESS_HAS_ADDRESS_CITY;
import static de.klinikum.domain.NameSpaces.ADDRESS_IN_COUNTRY;
import static de.klinikum.domain.NameSpaces.ADDRESS_HAS_PHONENUMBER;



import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.openrdf.model.Literal;
import org.openrdf.model.URI;
import org.openrdf.model.vocabulary.RDF;

import de.klinikum.domain.Patient;
import de.klinikum.domain.util.SesameTripleStore;
import de.klinikum.mock.PatientMock;

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
	

	
	@Override
	public Patient createPatientRDF(Patient patient) throws IOException {
		
		//Get Unique PatientURI Prefix
		URI patientUri = this.tripleStore.getUniqueURI(PERSON_TYPE.toString());
		URI addressUri = this.tripleStore.getUniqueURI(ADDRESS_TYPE.toString());
		
		//Adding URI to Patient E<lement
		patient.setUri(patientUri.toString());
		patient.getAddress().setUri(addressUri.toString());		//		URI patientURI = this.tripleStore.getValueFactory().createURI(pUri);
		
		//Creating Person_TYP URI -> Patient is RDF:TYPE Person
		URI personTypeURI = this.tripleStore.getValueFactory().createURI(PERSON_TYPE.toString());
		URI addressTypeURI = this.tripleStore.getValueFactory().createURI(ADDRESS_TYPE.toString()); 
		
		this.tripleStore.addTriple(patientUri, RDF.TYPE, personTypeURI);
		this.tripleStore.addTriple(addressUri, RDF.TYPE, addressTypeURI);
		
		//Creates URI pointing to Literals
		URI hasNName = this.tripleStore.getValueFactory().createURI(PERSON_HAS_NNAME.toString());
		URI hasVName = this.tripleStore.getValueFactory().createURI(PERSON_HAS_VNAME.toString());
		URI hasAddress = this.tripleStore.getValueFactory().createURI(PERSON_HAS_ADDRESS.toString());
		
		//Create Literal and Add to Sesame for FirstName
		Literal vNameLiteral = this.tripleStore.getValueFactory().createLiteral(patient.getvName());
		this.tripleStore.addTriple(patientUri, hasVName, vNameLiteral);
		
		//Create Literal and Add to Sesame for LastName
		Literal nNameLiteral = this.tripleStore.getValueFactory().createLiteral(patient.getnName());
		this.tripleStore.addTriple(patientUri, hasNName, nNameLiteral);
		
		//Create Address Element Triple
		this.tripleStore.addTriple(patientUri, hasAddress, addressUri);

		//Create Predicate for AddressRessource
		URI hasStreet = this.tripleStore.getValueFactory().createURI(ADDRESS_HAS_ADDRESS_STREET.toString());
		URI hasZip = this.tripleStore.getValueFactory().createURI(ADDRESS_HAS_ADDRESS_ZIP.toString());
		URI hasCity = this.tripleStore.getValueFactory().createURI(ADDRESS_HAS_ADDRESS_CITY.toString());
		URI hasCountry = this.tripleStore.getValueFactory().createURI(ADDRESS_IN_COUNTRY.toString());
		URI hasPhone = this.tripleStore.getValueFactory().createURI(ADDRESS_HAS_PHONENUMBER.toString());
		
		//Creating Address Literals
		Literal adressStreetLiteral = this.tripleStore.getValueFactory().createLiteral(patient.getAddress().getStreet());
		Literal adressZipLiteral = this.tripleStore.getValueFactory().createLiteral(patient.getAddress().getZip());
		Literal adressCityLiteral = this.tripleStore.getValueFactory().createLiteral(patient.getAddress().getCity());
		Literal adressCountryLiteral = this.tripleStore.getValueFactory().createLiteral(patient.getAddress().getCountry());
		Literal adressPhoneLiteral = this.tripleStore.getValueFactory().createLiteral(patient.getAddress().getPhone());
		
		//Adding Literals to Triples
		this.tripleStore.addTriple(addressUri, hasStreet, adressStreetLiteral);
		this.tripleStore.addTriple(addressUri, hasZip, adressZipLiteral);
		this.tripleStore.addTriple(addressUri, hasCity, adressCityLiteral);
		this.tripleStore.addTriple(addressUri, hasCountry, adressCountryLiteral);
		this.tripleStore.addTriple(addressUri, hasPhone, adressPhoneLiteral);
		
		return patient;
	}
	

	@Override
	public List<Patient> searchPatient(Patient patient) {
		// TODO Implement Search- Functionality
		List<Patient> mocklist = PatientMock.getMockPatientList();
		List<Patient> returnlist = new ArrayList<Patient>();
		for(Patient p : mocklist)
		{
			if(p.getnName().equals(patient.getnName()))
			{
				returnlist.add(p);
			}		
		}
		
		return returnlist;
	}

	private  String  createPatientNumber() {
		int rnd = (int) (Math.random() * (9999999 - 1000000) + 1000000);
		return String.valueOf(rnd);
	}

	
}
