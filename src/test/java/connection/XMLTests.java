package connection;


import java.io.StringWriter;



import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.JAXBContext;
import org.apache.bval.jsr303.xml.ObjectFactory;
import org.junit.Before;
import org.junit.Test;

import de.klinikum.domain.Address;
import de.klinikum.domain.Patient;

public class XMLTests {
	
	private JAXBContext context;
	private ObjectFactory factory;
	private Marshaller marshaller;
	
	@Before
	public void setUp() throws Exception
	{
	    context = JAXBContext.newInstance(new Class[]{Patient.class});
	    factory = new ObjectFactory();
	    marshaller = context.createMarshaller();
	}
	
	@Test
	public void test() throws JAXBException
	{
		Address a1 = new Address("de.spironto/address/" + "432432","Hauptstrass 12","Karlsruhe", "432433", "Deutschland", "081511833");
		Patient p1 = new Patient();
		p1.setAddress(a1);
		p1.setUri("de.spironto/patient/"+ "432432");
		p1.setLastName("Power");
		p1.setFirstName("Max");  
		
		StringWriter test = null;
		this.marshaller.marshal(p1, test);
		String str = null;
		
		test.write(str);
		System.out.println(str);
		
	}
}
