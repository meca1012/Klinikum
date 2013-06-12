package service;

import java.util.Date;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.klinikum.domain.Address;
import de.klinikum.domain.Concept;
import de.klinikum.domain.Patient;
import de.klinikum.domain.enums.TabConcepts;
import de.klinikum.exceptions.TripleStoreException;

@RunWith(Arquillian.class)
public class EnumTest {

	Patient patient;

	@Before
	public void before() {
		this.patient = new Patient();
		this.patient.setFirstName("Alice");
		this.patient.setLastName("Smith");
		this.patient.setDateOfBirth(new Date());
		this.patient.setUri("http://spironto.de/spironto#patient-gen1");
		this.patient.setPatientNumber("112233");
		Address address = new Address(null, "Musterstr.", "1", "Musterstadt",
				"76123", "D", "110");
		this.patient.setAddress(address);

	}

	@Test
	public void ttt() {
		for (TabConcepts tc : TabConcepts.values()) {
			Concept tabConcept = new Concept();
			tabConcept.setPatientUri(this.patient.getUri());
			tabConcept.setLabel(tc.toString());
			System.out.println(tc.toString());
		}
	}
}
