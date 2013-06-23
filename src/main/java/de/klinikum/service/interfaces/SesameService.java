package de.klinikum.service.interfaces;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import javax.ws.rs.core.Response;

import org.openrdf.model.Value;

import de.klinikum.exceptions.SpirontoException;

public interface SesameService {
    /**
     * Execute a SELECT SPARQL query.
     * 
     * @param queryString
     * @return Hashmap with results
     * @throws SpirontoException
     */
    Set<HashMap<String, Value>> executeSPARQLQuery(String queryString) throws SpirontoException;
    
    /**
     * 
     * @param language String to defines Language Setting 
     * @return boolean Returns True if changed / false if not
     * @throws IOException
     */
    boolean  changeLanguageSetting(String language) throws IOException;
}
