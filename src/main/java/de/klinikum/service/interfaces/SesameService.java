package de.klinikum.service.interfaces;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import org.openrdf.model.Value;

import de.klinikum.exceptions.SpirontoException;
/**
 * 
 * @author Constantin Treiber, Ivan Tepeluk, Carsten Meiser
 *
 */
public interface SesameService {
    /**
     * Execute a SELECT SPARQL query.
     * 
     * This function is like Forrest Gumps mum always said...
     * As.. Life is like a box of chocolates - you never know what you're gonna get...
     * This function returns a construct Set<Hashmap<String,Value> from Sesame and you never now that’s inside..
     * So it cannot passed through the REST -Service.

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
