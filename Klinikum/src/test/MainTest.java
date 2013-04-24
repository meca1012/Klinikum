package test;

import org.openrdf.OpenRDFException;
import org.openrdf.model.Literal;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.query.BindingSet;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;

import de.klinikum.server.Connect;

public class MainTest {

	/**
	 * @param args
	 * @throws RepositoryException
	 */
	public static void main(String[] args) throws RepositoryException {

		// Get RepoConnection
		Connect conn = new Connect();
		RepositoryConnection repoCon = conn.GetRepositoryConnection();

		// Daten eintragen
		System.out.println("THIS IS THE STATUS: " + repoCon.isOpen());

		ValueFactory f = repoCon.getValueFactory();
		URI alice = f.createURI("http://LmuKlinikum.de/patient/alice");
		URI bob = f.createURI("http://LmuKlinikum.de/patient/bob");
		URI name = f.createURI("http://LmuKlinikum.de/ontology/name");
		URI person = f.createURI("http://LmuKlinikum.de/ontology/person");
		Literal bobsname = f.createLiteral("Bob");
		Literal alicename = f.createLiteral("Alice");

		try {

			repoCon.add(alice, RDF.TYPE, person);
			repoCon.add(alice, name, alicename);

			repoCon.add(bob, RDF.TYPE, person);
			repoCon.add(bob, name, bobsname);

		} catch (Exception e) {
			e.printStackTrace();
		}

		// Datenauslesen
		try {
			
			try {
//				String queryString = "SELECT * FROM {S} rdf:type {rdfs:Person}";
//				String queryString = "SELECT ?x ?y WHERE { ?x ?p ?y } ";
				String queryString = "SELECT ?name WHERE {<http://LmuKlinikum.de/patient/alice> <http://LmuKlinikum.de/ontology/name> ?name}";
				TupleQuery tupleQuery = repoCon.prepareTupleQuery(
						QueryLanguage.SPARQL, queryString);
				TupleQueryResult result = tupleQuery.evaluate();
				try {
					while (result.hasNext()) {
						BindingSet bindingSet = result.next();
						Value valueOfX = bindingSet.getValue("name");
//						Value valueOfY = bindingSet.getValue("y");
						System.out.println(valueOfX.toString());
//						System.out.println(valueOfY.toString());

					}
				} finally {
					result.close();
				}
			} finally {
				repoCon.close();
			}
		} catch (OpenRDFException e) {
			// handle exception
		}

	}

}
