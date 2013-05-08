package connection;

import org.openrdf.model.URI;
import org.openrdf.model.Value;

import de.klinikum.domain.util.SimpleGraph;

public class SimpleGraphTest {

    public static void main(String[] args) {
	    // a test of graph operations
	    SimpleGraph g = new SimpleGraph(true);
	    
	    // get LOD from a URI -  Jamie's FOAF profile from Hi5
	    g.addURI("http://api.hi5.com/rest/profile/foaf/241087912");
	    
	    // manually add a triple/statement with a URIref object
	    URI s1 = g.URIref("http://semprog.com/people/toby");
	    URI p1 = g.URIref(SimpleGraph.RDFTYPE);
	    URI o1 = g.URIref("http://xmlns.com/foaf/0.1/person");
	    g.add(s1, p1, o1);
	    
	    // manually add with an object literal
	    URI s2 = g.URIref("http://semprog.com/people/toby");
	    URI p2 = g.URIref("http://xmlns.com/foaf/0.1/nick");
	    Value o2 = g.Literal("kiwitobes");
	    g.add(s2, p2, o2);	    
    }
}
