package de.klinikum.service;

import java.util.HashMap;
import java.util.Set;

import javax.inject.Inject;

import org.openrdf.model.Value;

import de.klinikum.exceptions.SpirontoException;
import de.klinikum.persistence.SesameTripleStore;

public class SesameServiceImpl implements SesameService {

    @Inject
    SesameTripleStore tripleStore;

    @Override
    public Set<HashMap<String, Value>> executeSPARQLQuery(String queryString) throws SpirontoException {

        return this.tripleStore.executeSelectSPARQLQuery(queryString);
    }

}
