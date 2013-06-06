package de.klinikum.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import org.openrdf.model.Value;

public interface SesameService {

    Set<HashMap<String, Value>> executeSPARQLQuery(String queryString) throws IOException;
}
