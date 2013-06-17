package de.klinikum.persistence;

import static de.klinikum.domain.NameSpaces.LAST_ID;
import static de.klinikum.domain.NameSpaces.TYPE_DATASTORE;
import info.aduna.iteration.Iterations;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;
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
import org.openrdf.repository.config.RepositoryConfig;
import org.openrdf.repository.config.RepositoryConfigException;
import org.openrdf.repository.config.RepositoryImplConfig;
import org.openrdf.repository.http.HTTPRepository;
import org.openrdf.repository.manager.RemoteRepositoryManager;
import org.openrdf.repository.manager.RepositoryManager;
import org.openrdf.repository.sail.config.SailRepositoryConfig;
import org.openrdf.sail.config.SailImplConfig;
import org.openrdf.sail.nativerdf.config.NativeStoreConfig;

import de.klinikum.exceptions.SpirontoException;
import de.klinikum.helper.PropertyLoader;

/**
 * 
 * SesameTripleStore.java
 * Purpose: Supports all needed TripleStore Methods to use Sesame as a RDF Storage Framework
 * Implementation of Reading and Creating Data as Triples, as well execute SPARQL Querys 
 * Store Configuration comes with the configuration file in properties package 
 * Implementation for Sesame 2.7.0       
 *   
 * @author  Spironto Team 1
 * @version 1.0 08/06/13
 */


@Named
public class SesameTripleStore {


	private static final String configName = "sesame.properties";
	private static final String serverLinkKey = "server.link";
	private static final String repositoryIdKey = "server.repositoryId";
	private static final String repositoryTextKey = "server.repositoryText";
	private static final String spirontoNsKey = "spironto.namespace";
	
	private PropertyLoader propertyLoader;
	private RepositoryManager repoManager;
	private RepositoryConnection con;
	private ValueFactory valueFactory;

	private String sesameServer;
	private URI datastoreURI;
	private String repositoryID;
	private String repositoryText;
	private String spirontoNs;
	
	/**
	 * Creates SesameTripleStore Object and reads the ConfigData from /de/klinikum/properties/sesame.properties
	 */
	public SesameTripleStore() {
		try {
			propertyLoader = new PropertyLoader();
			Properties propFile = propertyLoader.load(configName);
			this.sesameServer = propFile.getProperty(serverLinkKey);
			this.repositoryID = propFile.getProperty(repositoryIdKey);
			this.repositoryText = propFile.getProperty(repositoryTextKey);
			this.spirontoNs = propFile.getProperty(spirontoNsKey);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
/**
 * Initiats TripeStoreClass / starts connection to SesameServer
 * If no Repository could be found with the SesameId given by the ConfigFile method will try to create a new Store with
 * the given properties.
 * @throws IOException
 */
    @PostConstruct
    public void initSesameTripleStore() throws IOException {

    	Repository repository;
        repository = new HTTPRepository(this.sesameServer, this.repositoryID);
        
        try {
            repository.initialize();
            this.valueFactory = repository.getValueFactory();
            this.con = repository.getConnection();
            this.initRepository(this.con);
        }
        
        catch (RepositoryException e) {
        	
        	SailImplConfig sailImplConfig = new NativeStoreConfig();
       	 	RepositoryImplConfig repImplConfig = new SailRepositoryConfig(sailImplConfig);
        	RepositoryConfig repConfig = new RepositoryConfig(this.repositoryID, this.repositoryText, repImplConfig);
       	        	
        	try {
        		repoManager = new RemoteRepositoryManager(this.sesameServer);
        		repoManager.initialize();
        		repoManager.addRepositoryConfig(repConfig);    		
				initSesameTripleStore();			
			}     	
        	catch (RepositoryException e1) {
				e1.printStackTrace();
			} 
        	catch (RepositoryConfigException e1) {
				e1.printStackTrace();
			}   	  	 
        }
    }

    @PreDestroy
    public void closeConnection() throws RepositoryException {
        this.con.close();
    }

    public void initRepository(RepositoryConnection con) throws RepositoryException, IOException {
        URI typeDatastore = this.valueFactory.createURI(TYPE_DATASTORE.toString());
        RepositoryResult<Statement> statements = this.con.getStatements(null, RDF.TYPE, typeDatastore, false);
        if (statements.hasNext()) {
            Statement stmt = statements.next();
            this.datastoreURI = (URI) stmt.getSubject();
            statements.close();
        }
        else {
            statements.close();
            this.datastoreURI = this.valueFactory.createURI(spirontoNs);
            this.addTriple(this.datastoreURI.toString(), RDF.TYPE.toString(), typeDatastore.toString());
            this.setValue(this.datastoreURI.toString(), LAST_ID.toString(), 0);
        }
    }
    
    /**
     * Helper method for TestData creation. Creates the DataStore Triple for uri generation.
     */    
    public void setDatastoreTriple() {
        try {
            URI typeDatastore = this.valueFactory.createURI(TYPE_DATASTORE.toString());
            this.datastoreURI = this.valueFactory.createURI(spirontoNs);
            this.addTriple(this.datastoreURI.toString(), RDF.TYPE.toString(), typeDatastore.toString());
            this.setValue(this.datastoreURI.toString(), LAST_ID.toString(), 0);
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public ValueFactory getValueFactory() {
        return this.valueFactory;
    }

    /**
     * Adds Triple as ResourceElement
     * @param subject -> Subject as Resource
     * @param predicate -> Predicate URI of Predicate- Element for triple
     * @param object -> Object as Value for triple
     * @throws IOException
     */
    public void addTriple(Resource subject, URI predicate, Value object) throws IOException {
        try {
            this.con.add(subject, predicate, object);
        }
        catch (RepositoryException re) {
            throw new IOException(re);
        }
    }

    /**
     * 
     * @param subjectURI -> Subject URI of Subject- Element for triple
     * @param predicateURI -> Predicate URI of Predicate- Element for triple
     * @param objectURI -> -> Object URI of Object- Element for triple
     * @throws IOException
     * Adds Triple to Sesame- Store
     */
    public void addTriple(String subjectURI, String predicateURI, String objectURI) throws IOException {
        URI subject = this.valueFactory.createURI(subjectURI);
        URI predicate = this.valueFactory.createURI(predicateURI);
        URI object = this.valueFactory.createURI(objectURI);
        this.addTriple(subject, predicate, object);
    }

    /**
     * Creates triple with Literals for string data
     * @param subjectURI  -> Subject URI of Subject- Element for triple
     * @param predicateURI -> Predicate URI of Predicate- Element for triple
     * @param objectValue -> Value for Literal
     * @throws IOException
     */
    public void addTriple(String subjectURI, String predicateURI, int objectValue) throws IOException {
        URI subject = this.valueFactory.createURI(subjectURI);
        URI predicate = this.valueFactory.createURI(predicateURI);
        Value object = this.valueFactory.createLiteral(objectValue);
        this.addTriple(subject, predicate, object);
    }

    /**
     * Removes triple from SesameStore
     * @param subject Predicate URI of Predicate- Element for triple
     * @param predicate Predicate URI of Predicate- Element for triple
     * @param object -> object- Value of object- Element for triple
     * @throws IOException
     */
    public void removeTriples(Resource subject, URI predicate, Value object) throws IOException {
        try {
            this.con.remove(subject, predicate, object);
        }
        catch (RepositoryException re) {
            throw new IOException(re);
        }
    }

    /**
     * Remove Triples with null as wildcard
     * @param subjectURI -> Subject- URI of Subject- Element for triple
     * @param predicateURI -> Predicate- URI of Predicate- Element for triple
     * @param objectURI -> Object- URI of Object- Element for triple
     * @throws IOException
     */
    public void removeTriples(String subjectURI, String predicateURI, String objectURI) throws IOException {
        URI subject = null;
        if (subjectURI != null)
            subject = this.valueFactory.createURI(subjectURI);
        URI predicate = null;
        if (predicateURI != null)
            predicate = this.valueFactory.createURI(predicateURI);
        URI object = null;
        if (objectURI != null)
            object = this.valueFactory.createURI(objectURI);
        this.removeTriples(subject, predicate, object);
    }

    /**
     * 
     * @param subjectURI -> Subject- URI of Subject- Element for triple
     * @param predicateURI -> Predicate- URI of Predicate- Element for triple
     * @return Returns literal value converted back to integer
     * @throws IOException
     */
    public int getValue(String subjectURI, String predicateURI) throws IOException {
        try {
            URI subject = this.valueFactory.createURI(subjectURI);
            URI predicate = this.valueFactory.createURI(predicateURI);
            RepositoryResult<Statement> statements = this.con.getStatements(subject, predicate, null, false);
            Statement stmt = statements.next();
            Literal literal = (Literal) stmt.getObject();
            statements.close();
            return literal.intValue();
        }
        catch (RepositoryException re) {
            throw new IOException(re);
        }
    }

    /**
     * Stores integer values to Triple- Store
     * @param subjectURI -> Subject- URI of Subject- Element for triple
     * @param predicateURI -> Predicate- URI of Predicate- Element for triple
     * @param value -> Integer value to be stored in triple
     * @throws IOException
     */
    public void setValue(String subjectURI, String predicateURI, int value) throws IOException {
        this.addTriple(subjectURI, predicateURI, value);
    }

    /**
     * Creates a unique URR for Triple- Identification
     * @param objectURI -> Object uri for identification of the uniqueID Element in Sesame- Store
     * @return returns -> Unique URI for Sesame
     * @throws IOException
     */
    public URI getUniqueURI(String objectURI) throws IOException {
        int value = this.getValue(this.datastoreURI.toString(), LAST_ID.toString());
        this.removeTriples(this.datastoreURI.toString(), LAST_ID.toString(), null);
        value++;
        this.setValue(this.datastoreURI.toString(), LAST_ID.toString(), value);
        return this.valueFactory.createURI(objectURI + "-gen" + value);
        // return this.valueFactory.createURI("/" + value);
    }

    public URI getDatastoreURI() {
        return this.datastoreURI;
    }

    /**
     * Executes string query SPARQL
     * @param queryString -> SPARQL Query
     * @return Hashmap with results
     * @throws SpirontoException
     */
    public Set<HashMap<String, Value>> executeSelectSPARQLQuery(String queryString) throws SpirontoException {
        try {
            TupleQuery query = this.con.prepareTupleQuery(org.openrdf.query.QueryLanguage.SPARQL, queryString);
            TupleQueryResult result = query.evaluate();
            Set<HashMap<String, Value>> resultList = new HashSet<HashMap<String, Value>>();
            while (result.hasNext()) {
                BindingSet bindingSet = result.next();
                Set<String> names = bindingSet.getBindingNames();
                HashMap<String, Value> map = new HashMap<String, Value>();
                for (String n : names) {
                    map.put(n, bindingSet.getValue(n));
                    resultList.add(map);
                }
            }
            result.close();
            return resultList;
        }
        catch (RepositoryException re) {
        	re.printStackTrace();
            throw new SpirontoException(re);
        }
        catch (MalformedQueryException mqe) {
            throw new SpirontoException(mqe);
        }
        catch (QueryEvaluationException qee) {
            throw new SpirontoException(qee);
        }
    }

    /**
     * Returns multiple results to triple- Query
     * @param subject -> Subject- URI of Subject- Element for triple
     * @param predicate -> Predicate- URI of Predicate- Element for triple
     * @param object -> Object- URI of Predicate- Element for triple
     * @return returns Statement- List
     * @throws IOException
     * @throws RepositoryException
     */
    public Model getStatementList(Resource subject, URI predicate, Value object) throws IOException,
            RepositoryException {
        RepositoryResult<Statement> statements = this.con.getStatements(subject, predicate, object, false);
        try {
            Model statementList = Iterations.addAll(statements, new LinkedHashModel());
            return statementList;
        }
        catch (RepositoryException re) {
            throw new IOException(re);
        }
        finally {
            statements.close();
        }
    }
    /**
     * Returns multiple results to triple- Query
     * @param subject -> Subject- URI of Subject- Element for triple
     * @param predicate -> Predicate- URI of Predicate- Element for triple
     * @param object -> Object- String of Predicate- Element for triple
     * @return returns Statement- List
     * @throws IOException
     * @throws RepositoryException
     */  
    public Model getStatementList(String subject, String predicate, String object) throws IOException,
            RepositoryException {
        URI subjectURI = null;
        if (subject != null)
            subjectURI = this.valueFactory.createURI(subject);
        URI predicateURI = null;
        if (predicate != null)
            predicateURI = this.valueFactory.createURI(predicate);
        URI objectURI = null;
        if (object != null)
            objectURI = this.valueFactory.createURI(object);
        return this.getStatementList(subjectURI, predicateURI, objectURI);
    }

    /**
     * Returns Object- Strings 
     * @param subjectUri -> Subject- URI of Subject- Element for triple
     * @param predicateUri -> Predicate -URI of Predicate- Element for triple
     * @return returns ObjectValue of Subject-> Object -> NULL
     * @throws RepositoryException
     * @throws IOException
     */
    public String getObjectString(String subjectUri, String predicateUri) throws RepositoryException, IOException {
        URI subjectURI = null;
        if (subjectUri != null)
            subjectURI = this.valueFactory.createURI(subjectUri);
        URI predicateURI = null;
        if (predicateUri != null)
            predicateURI = this.valueFactory.createURI(predicateUri);
        URI objectURI = null;
        Model toReturn = this.getStatementList(subjectURI, predicateURI, objectURI);
        if (toReturn.size() == 1) {
            return toReturn.objectString();
        }
        else {
            return null;
        }
    }

    /**
     * Return -> true / fals -> if Triple exists
     * @param subject -> Subject as Resource
     * @param predicate -> Predicate as Uri
     * @param object -> Object as Values
     * @return true / false 
     * @throws IOException
     */
    public boolean repositoryHasStatement(Resource subject, URI predicate, Value object) throws IOException {
    	boolean toReturn = false;
    	try {
    		toReturn = this.con.hasStatement(subject, predicate, object, false);
    		return toReturn;
        }
        catch (RepositoryException re) {
        	throw new IOException(re);
        }    	
    }
    
    /**
     * Retuns true / fals -> if Triple exists
     * Checks for existing Statement
     * @param subjectUri -> Subject as Uri
     * @param predicateUri -> Predicate as Uri
     * @param objectUri -> Object as Uri
     * @return true / false 
     * @throws IOException
     */
    public boolean repositoryHasStatement(String subjectUri, String predicateUri, String objectUri) throws IOException {
    	URI subjectURI = null;
        if (subjectUri != null)
            subjectURI = this.valueFactory.createURI(subjectUri);
        URI predicateURI = null;
        if (predicateUri != null)
            predicateURI = this.valueFactory.createURI(predicateUri);
        URI objectURI = null;
        if (objectUri != null)
            objectURI = this.valueFactory.createURI(objectUri);
        return repositoryHasStatement(subjectURI, predicateURI, objectURI);
    }

}
