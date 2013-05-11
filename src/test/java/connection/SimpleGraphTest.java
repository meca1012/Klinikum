package connection;

import java.util.List;

import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;

import de.klinikum.domain.util.SimpleGraph;

public class SimpleGraphTest {

    public static void main(String[] args) {
	    // a test of graph operations
	    SimpleGraph g = new SimpleGraph();
	    
	    // get LOD from a URI -  Jamie's FOAF profile from Hi5
	    g.addURI("http://api.hi5.com/rest/profile/foaf/241087912");
	    
	    // manually add a triple/statement with a URIref object
	    URI bob = g.URIref("http://LmuKlinikum.de/patient/bob");
	    URI predicate = g.URIref(SimpleGraph.RDFTYPE);
	    URI person = g.URIref("http://LmuKlinikum.de/ontology/person");
	    g.add(bob, predicate, person);	    
	    
	    // manually add with an object literal
	    URI name = g.URIref("http://LmuKlinikum.de/ontology/name");
	    Value object = g.Literal("Bob");
	    g.add(bob, name, object);
	    
	    List<Statement> result = g.tuplePattern(null, predicate, person);
	    for(int i = 0; i < result.size(); i++) {
	    	System.out.println("this is the result: " + result.get(i).getSubject().toString());
	    	
	    }
    }
}
