package de.klinikum.mock;

import java.util.ArrayList;
import java.util.List;

import de.klinikum.domain.Address;
import de.klinikum.domain.Patient;

public class PatientMock {
	public static List<Patient> getMockPatientList()
	{
		String uriHelper = "de.spironto/patient/";
		Address a1 = new Address("Hauptstrass 12","Karlsruhe", "432433", "Deutschland", "081511833");
		
		List<Patient> mockList = new ArrayList<Patient>();
		mockList.add(new Patient(uriHelper + "1111111", "Hanna", "Schmitt",a1));
		mockList.add(new Patient(uriHelper + "2222222", "Peter", "Becker",a1));
		mockList.add(new Patient(uriHelper + "3333333", "Linda", "Becker",a1));
		mockList.add(new Patient(uriHelper + "4444444", "Simon", "Schröder",a1));
		mockList.add(new Patient(uriHelper + "5555555", "Nadja", "Maurer",a1));
		mockList.add(new Patient(uriHelper + "6666666", "Christian", "Becker",a1));
		mockList.add(new Patient(uriHelper + "7777777", "Lisa", "Becker",a1));
		mockList.add(new Patient(uriHelper + "8888888", "Julian", "Seitz",a1));
		
		return mockList;
	}
}
