package de.klinikum.service;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import de.klinikum.domain.Patient;

@Named
public class PatientService {
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
	
	public Patient getPatient(String patientNumber) {
		Patient p1 = new Patient("http://spironto.de/patient/" + patientNumber, "Max", "Power");
		return p1;
	}

}
