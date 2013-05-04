package de.klinikum.service;

import de.klinikum.domain.Patient;

public class PatientService {

	public PatientService() {
	}

	public Patient getPatient(String patientNumber) {
		Patient p1 = new Patient(patientNumber, "Max", "Power");
		return p1;
	}

}
