package de.klinikum.service.interfaces;

import java.util.HashMap;
import java.util.Set;

import org.openrdf.model.Value;

import de.klinikum.exceptions.SpirontoException;

public interface SesameService {

    Set<HashMap<String, Value>> executeSPARQLQuery(String queryString) throws SpirontoException;
}
