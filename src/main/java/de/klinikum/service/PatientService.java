package de.klinikum.service;
import de.klinikum.domain.Patient;

public class PatientService {
	public static Patient getPatient(String patientNumber)
	{
		Patient p1 = new Patient(patientNumber, "Max", "Power");	
		return p1;
		
	}
}
