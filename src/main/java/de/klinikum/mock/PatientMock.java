package de.klinikum.mock;

import java.util.ArrayList;
import java.util.List;

import de.klinikum.domain.Patient;

public class PatientMock {
	public static List<Patient> getMockPatientList()
	{
		String uriHelper = "de.spironto/patient/";
				
		List<Patient> mockList = new ArrayList<Patient>();
		mockList.add(new Patient(uriHelper + "1111111", "Hanna", "Schmitt"));
		mockList.add(new Patient(uriHelper + "2222222", "Peter", "Becker"));
		mockList.add(new Patient(uriHelper + "3333333", "Linda", "Becker"));
		mockList.add(new Patient(uriHelper + "4444444", "Simon", "Schröder"));
		mockList.add(new Patient(uriHelper + "5555555", "Nadja", "Maurer"));
		mockList.add(new Patient(uriHelper + "6666666", "Christian", "Becker"));
		mockList.add(new Patient(uriHelper + "7777777", "Lisa", "Becker"));
		mockList.add(new Patient(uriHelper + "8888888", "Julian", "Seitz"));
		
		return mockList;
	}
}
