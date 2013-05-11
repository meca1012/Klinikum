package de.klinikum.service;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import de.klinikum.domain.Patient;

@Named
//public class PatientServiceImpl implements PatientService, Serializable {
public class PatientServiceImpl implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 858804068195499626L;

	public PatientServiceImpl() {
	}
	
	@PostConstruct
	public void afterCreate() {
		System.out.println("PatientService created");
	}

	private String className = "PatientServiceImpl";

//	@Override
	public Patient getPatient(String patientNumber) {
		Patient p1 = new Patient("http://spironto.de/patient/" + patientNumber,
				"Max", "Power");
		return p1;
	}
	
//	@Override
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}


}
