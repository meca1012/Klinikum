package de.klinikum.service;
import static de.klinikum.domain.NameSpaces.PERSON_TYPE;
import static de.klinikum.domain.NameSpaces.PERSON_HAS_NNAME;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
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
	
	public Patient getPatientPatientnumber(String patientNumber) {
		Patient p1 = new Patient("de.spironto/patient/" + patientNumber, "Max", "Power");
		return p1;
	}


	@Override
	public Patient createPatient(Patient patient) {
		// TODO Implement Search- Functionality
		Patient p1 = new Patient("de.spironto/patient/" + createPatientNumber() , patient.getvName(), patient.getnName());
		return p1;
	}
	
	@Override
	public Patient createPatientRDF(Patient patient) throws IOException {
		String pUri = PERSON_TYPE + this.tripleStore.getUniqueURI().toString();
		patient.setUri(pUri);
		URI patientURI = this.tripleStore.getValueFactory().createURI(pUri);
		URI patientTypeURI = this.tripleStore.getValueFactory().createURI(PERSON_TYPE.toString());
		this.tripleStore.addTriple(patientURI, RDF.TYPE, patientTypeURI);
		URI hasName = this.tripleStore.getValueFactory().createURI(PERSON_HAS_NNAME.toString());
		Literal patientName = this.tripleStore.getValueFactory().createLiteral(patient.getvName() + ", " + patient.getnName());
		this.tripleStore.addTriple(patientURI, hasName, patientName);		
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
