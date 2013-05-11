package de.klinikum.domain.util;

import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.openrdf.model.BNode;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.RDFFormat;
import org.openrdf.sail.inferencer.fc.ForwardChainingRDFSInferencer;
import org.openrdf.sail.nativerdf.NativeStore;

//CRUD
public class SimpleGraph {
	
	Repository theRepository = null;
	String repositoryPath = "/c/dev/sesameStore";
	
	// useful -local- constants
	public static RDFFormat NTRIPLES = RDFFormat.NTRIPLES;
    public static RDFFormat N3 = RDFFormat.N3;
    public static RDFFormat RDFXML = RDFFormat.RDFXML;
    public static String RDFTYPE =  RDF.TYPE.toString();
    
    public SimpleGraph(boolean inferencing) {
    	try {
    		if (inferencing) {
    			theRepository = 
    					new SailRepository(new ForwardChainingRDFSInferencer(
    							new NativeStore(new File(repositoryPath))));
    		} else {
    			theRepository = new SailRepository(new NativeStore(new File(repositoryPath)));
    		}
    		theRepository.initialize();
    	} catch (RepositoryException e) {
    		e.printStackTrace();
    	}
    }
    
    public org.openrdf.model.Literal Literal (String literalValue, URI typeUri) {
    	try {
    		RepositoryConnection con = theRepository.getConnection();
    		try {
    			ValueFactory vf = con.getValueFactory();
    			if (typeUri == null) {
    				return vf.createLiteral(literalValue);
    			} else {
    				return vf.createLiteral(literalValue, typeUri);
    			}
    		} finally {
    			con.close();
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    		return null;
    	}
    }
    
    public org.openrdf.model.Literal Literal(String literal) {
        return Literal(literal, null);
    }
    
    public URI URIref(String uri) {
    	try {
    		RepositoryConnection con = theRepository.getConnection();
    		try {
    			ValueFactory vf = con.getValueFactory();
    			return vf.createURI(uri);
    		} finally {
    			con.close();
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    		return null;
    	}
    }
    
    public BNode bnode() {
        try{
            RepositoryConnection con = theRepository.getConnection();
            try {
                ValueFactory vf = con.getValueFactory();
                return vf.createBNode();
            } finally {
                con.close();
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
    
    //Insert Triple/Statement into graph 
    
    public void add(URI subject, URI predicate, Value object) {
        try {
               RepositoryConnection con = theRepository.getConnection();
               try {
                    ValueFactory myFactory = con.getValueFactory();
                    Statement statement = myFactory.createStatement((Resource) 
                    		subject, predicate, (Value) object);
                    con.add(statement);
               } finally {
                  con.close();
               }
            }
            catch (Exception e) {
               e.printStackTrace();
            }
    }
    
    public void addURI(String urlString, RDFFormat format) {
        try {
            RepositoryConnection con = theRepository.getConnection();
            try {
                URL url = new URL(urlString);
                URLConnection uricon = (URLConnection) url.openConnection();
                uricon.addRequestProperty("accept", format.getDefaultMIMEType());
                InputStream instream = uricon.getInputStream();
                con.add(instream, urlString, format);
            } finally {
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void addURI(String urlstring) {
        addURI(urlstring, RDFFormat.RDFXML);
    }
    
    //Import RDF data from a string
    
    public void addString(String rdfString,  RDFFormat format) {
        try {
            RepositoryConnection con = theRepository.getConnection();
            try {
                StringReader sr = new StringReader(rdfString);
                con.add(sr, "", format);
            } finally {
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //Tuple pattern query - find all statements with the pattern,
    //where null is a wildcard    
    
    public List<Statement> tuplePattern(URI subject, URI predicate, Value object) {
        try{
            RepositoryConnection con = theRepository.getConnection();
            try {
                RepositoryResult<Statement> repres = con.getStatements(subject, predicate, object, true);
                ArrayList<Statement> reslist = new ArrayList<Statement>();
                while (repres.hasNext()) {
                    reslist.add(repres.next());
                }
                return reslist;
            } finally {
                con.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
