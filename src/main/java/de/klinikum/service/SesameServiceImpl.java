package de.klinikum.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import javax.inject.Inject;

import org.openrdf.model.Value;

import de.klinikum.persistence.SesameTripleStore;

public class SesameServiceImpl implements SesameService {

    @Inject
    private SesameTripleStore tripleStore;

    @Override
    public Set<HashMap<String, Value>> executeSPARQLQuery(String queryString) throws IOException {

        return this.tripleStore.executeSelectSPARQLQuery(queryString);
    }

}
