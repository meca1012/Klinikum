package connection;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.junit.Before;
import org.junit.Test;

import de.klinikum.domain.Address;
import de.klinikum.domain.Patient;

public class XMLTests {

	private JAXBContext context;
	private Marshaller marshaller;

	@Before
	public void setUp() throws Exception {
		this.context = JAXBContext.newInstance(new Class[] { Patient.class });
		this.marshaller = context.createMarshaller();
	}

	@Test
	public void generateXML() throws JAXBException {
		Address address = new Address("de.spironto/address/" + "432432",
				"Hauptstrass", "12", "Karlsruhe", "432433", "Deutschland",
				"081511833");	
		Patient patient = new Patient();
		patient.setAddress(address);
		patient.setUri("de.spironto/patient/" + "432432");
		patient.setLastName("Power");
		patient.setFirstName("Max");

		StringWriter sw = new StringWriter();
		this.marshaller.marshal(patient, sw);
		String result = sw.toString();
		System.out.println(result);

	}

}
