package de.klinikum.service;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import de.klinikum.domain.Patient;
import de.klinikum.mock.PatientMock;

@Named
@RequestScoped
public class PatientServiceImpl implements PatientService {
	private String className = "PatientService";
	
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
