package de.klinikum.service.interfaces;

import java.util.HashMap;
import java.util.Set;

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
}
