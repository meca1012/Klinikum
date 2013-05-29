package connection;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.bval.jsr303.xml.ObjectFactory;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.klinikum.domain.Address;
import de.klinikum.domain.Concept;
import de.klinikum.domain.Patient;
import de.klinikum.domain.PatientDTO;

public class XMLTests {

	private JAXBContext context;
	private ObjectFactory factory;
	private Marshaller marshaller;

	@Before
	public void setUp() throws Exception {
		context = JAXBContext.newInstance(new Class[] { Patient.class, PatientDTO.class });
		factory = new ObjectFactory();
		marshaller = context.createMarshaller();
	}

	@Ignore
	@Test
	public void test() throws JAXBException {
		Address a1 = new Address("de.spironto/address/" + "432432",
				"Hauptstrass", "12", "Karlsruhe", "432433", "Deutschland",
				"081511833");
		Patient p1 = new Patient();
		p1.setAddress(a1);
		p1.setUri("de.spironto/patient/" + "432432");
		p1.setLastName("Power");
		p1.setFirstName("Max");

		StringWriter test = new StringWriter();
		this.marshaller.marshal(p1, test);
		String result = test.toString();
		System.out.println(result);

	}
	
	
	@Test
	public void generateXML() throws JAXBException {
		
		Patient p1 = new Patient();
		p1.setUri("de.spironto/patient/" + "12345");
		p1.setLastName("Power");
		p1.setFirstName("Max");
		
		Concept c1 = new Concept("http://spironto.de/spironto#concept-gen13","Spiritualitaet", "http://spironto.de/spironto#patient-gen11");
		
		PatientDTO pdto = new PatientDTO();
		pdto.setPatient(p1);
		pdto.setConcept(c1);
		pdto.setTabConcept(true);
		
		StringWriter sw = new StringWriter();
		this.marshaller.marshal(pdto, sw);
		String result = sw.toString();
		System.out.println(result);

	}
}
