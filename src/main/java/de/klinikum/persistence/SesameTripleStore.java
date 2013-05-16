package de.klinikum.persistence;

import static de.klinikum.domain.NameSpaces.LAST_ID;
import static de.klinikum.domain.NameSpaces.TYPE_DATASTORE;
import info.aduna.iteration.Iterations;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Named;

import org.openrdf.model.Literal;
import org.openrdf.model.Model;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.LinkedHashModel;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.query.BindingSet;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;
import org.openrdf.repository.http.HTTPRepository;




@Named
public class SesameTripleStore {
	
	private RepositoryConnection con;
	private ValueFactory valueFactory;
	private String sesameServer = "http://localhost:8080/openrdf-sesame";
	private URI datastoreURI;
	//TODO: verschiedene store arten evaluieren
	private String repositoryID = "TestNative";
	public static final String SPIRONTO_NS = "http://spironto.de/ns/1.0#";
	
	public SesameTripleStore() {		
	}
	
	@PostConstruct
	public void initSesameTripleStore() throws IOException {
		Repository repository;
		repository = new HTTPRepository(this.sesameServer, this.repositoryID);
		try {
			repository.initialize();
			//TODO: check Valuefactory aus connection oder repository
			this.valueFactory = repository.getValueFactory();
			this.con = repository.getConnection();
			initRepository(this.con);
		} catch(RepositoryException e) {
			throw new IOException(e);
		}		
	}
	
	@PreDestroy
	public void closeConnection() throws RepositoryException {
		this.con.close();
	}
	
	//Methode zur Initialisierung eines Triples vom NS zur LAST_ID, welche zur generierung einer uniqueUri 
	public void initRepository(RepositoryConnection con) throws RepositoryException, IOException {
		URI typeDatastore = this.valueFactory.createURI(TYPE_DATASTORE.toString());
		RepositoryResult<Statement> statements = this.con.getStatements(null, RDF.TYPE, typeDatastore, false);
		if(statements.hasNext()) {
			Statement stmt = statements.next();
			this.datastoreURI = (URI) stmt.getSubject();
			statements.close();
		} else {
			statements.close();
			this.datastoreURI = this.valueFactory.createURI(SPIRONTO_NS);
			addTriple(this.datastoreURI.toString(), RDF.TYPE.toString(), typeDatastore.toString());
			setValue(this.datastoreURI.toString(), LAST_ID.toString(), 0);
		}
	}
	
	public ValueFactory getValueFactory() {
		return this.valueFactory;
	}
	
	//Triple als Resource hinzufügen
	public void addTriple(Resource subject, URI predicate, Value object) throws IOException {
		try {
			this.con.add(subject, predicate, object);
		} catch(RepositoryException re) {
			throw new IOException(re);
		}
	}
	
	public void addTriple(String subjectURI, String predicateURI, String objectURI) throws IOException {
		URI subject = this.valueFactory.createURI(subjectURI);
		URI predicate = this.valueFactory.createURI(predicateURI);
		URI object = this.valueFactory.createURI(objectURI);
		addTriple(subject, predicate, object);
	}
	
	public void addTriple(String subjectURI, String predicateURI, int objectValue) throws IOException {
		URI subject = this.valueFactory.createURI(subjectURI);
		URI predicate = this.valueFactory.createURI(predicateURI);
		Value object = this.valueFactory.createLiteral(objectValue);
		addTriple(subject, predicate, object);
	}
	
	public void removeTriples(Resource subject, URI predicate, Value object) throws IOException {
		try {
			this.con.remove(subject, predicate, object);
		} catch(RepositoryException re) {
			System.out.println(re.getMessage());
			throw new IOException(re);
		}
	}
	
	public void removeTriples(String subjectURI, String predicateURI, String objectURI) throws IOException {
		URI subject = null;
		if(subjectURI != null) subject = this.valueFactory.createURI(subjectURI);
		URI predicate = null;
		if(predicateURI != null) predicate = this.valueFactory.createURI(predicateURI);
		URI object = null;
		if(objectURI != null) object = this.valueFactory.createURI(objectURI);
		removeTriples(subject, predicate, object);
	}
	
	public int getValue(String subjectURI, String predicateURI) throws IOException {
		try {
			URI subject = this.valueFactory.createURI(subjectURI);
			URI predicate = this.valueFactory.createURI(predicateURI);
			RepositoryResult<Statement> statements = this.con.getStatements(subject, predicate, null, false);
			Statement stmt = statements.next();
			Literal literal = (Literal) stmt.getObject();
			statements.close();
			return literal.intValue();
		} catch(RepositoryException re) {
			throw new IOException(re);
		}
	}
	
	public void setValue(String subjectURI, String predicateURI, int value) throws IOException {
		addTriple(subjectURI, predicateURI, value);
	}
	
	//Liefert eine eindeutige URI bestehend aus "namespace -gen sequenz"
	public URI getUniqueURI(String objectURI) throws IOException {
		int value = getValue(this.datastoreURI.toString(), LAST_ID.toString());
		removeTriples(this.datastoreURI.toString(), LAST_ID.toString(), null);
		value++;
		setValue(this.datastoreURI.toString(), LAST_ID.toString(), value);
		return this.valueFactory.createURI(objectURI + "-gen" + value);
//		return this.valueFactory.createURI("/" + value);
	}	

	public URI getDatastoreURI() {
		return datastoreURI;
	}	

	//Führt eine SELECT SPARQL-Abfrage aus
    public Set<HashMap<String,Value>> executeSelectSPARQLQuery(String queryString) throws IOException{
    	try {
    		TupleQuery query = this.con.prepareTupleQuery(org.openrdf.query.QueryLanguage.SPARQL, queryString);
    		TupleQueryResult result = query.evaluate();
    		Set<HashMap<String,Value>> resultList = new HashSet<HashMap<String,Value>>();
    		while (result.hasNext()) {
    			BindingSet bindingSet = result.next();
    			Set<String> names = bindingSet.getBindingNames();
    			HashMap<String,Value> map = new HashMap<String,Value>();
    			for (String n : names) map.put(n, bindingSet.getValue(n));
    			resultList.add(map);
    		}
    		result.close();
    		return resultList;
    	}
    	catch (RepositoryException re) {
    		throw new IOException(re);
    	} catch (MalformedQueryException e) {
    		throw new IOException(e);
		} catch (QueryEvaluationException e) {
			throw new IOException(e);
		}
    }
    
    public Model getStatementList(Resource subject, URI predicate, Value object) throws IOException, RepositoryException {
    	RepositoryResult<Statement> statements = this.con.getStatements(subject, predicate,object, false);
    	try {			
			Model statementList = Iterations.addAll(statements, new LinkedHashModel());			
			return statementList;
		} catch (RepositoryException re) {
			throw new IOException(re);
		} finally {
			statements.close();
		}
	}
	
	public Model getStatementList(String subject, String predicate, String object) throws IOException, RepositoryException {
		URI subjectURI = null;
		if (subject != null) subjectURI = valueFactory.createURI(subject);
		URI predicateURI = null;
		if (predicate != null) predicateURI = valueFactory.createURI(predicate);
		URI objectURI = null;
		if (object != null) objectURI = valueFactory.createURI(object);
		return getStatementList(subjectURI, predicateURI, objectURI);
	}
	
	public String getObjectString(String subjectUri, String predicateUri) throws RepositoryException, IOException {
		URI subjectURI = null;
		if (subjectUri != null) subjectURI = valueFactory.createURI(subjectUri);
		URI predicateURI = null;
		if (predicateUri != null) predicateURI = valueFactory.createURI(predicateUri);
		URI objectURI = null;
		Model toReturn = getStatementList(subjectURI, predicateURI, objectURI);
		if(toReturn.size() == 1) {
			return toReturn.objectString();
		} else {
			return null;
		}
	}

}
