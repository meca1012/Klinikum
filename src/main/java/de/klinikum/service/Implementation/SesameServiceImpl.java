package de.klinikum.service.Implementation;

import java.util.HashMap;
import java.util.Set;

import javax.inject.Inject;

import org.openrdf.model.Value;

import de.klinikum.exceptions.SpirontoException;
import de.klinikum.persistence.SesameTripleStore;
import de.klinikum.service.Interfaces.SesameService;

public class SesameServiceImpl implements SesameService {

    @Inject
    SesameTripleStore tripleStore;

    @Override
    public Set<HashMap<String, Value>> executeSPARQLQuery(String queryString) throws SpirontoException {

        return this.tripleStore.executeSelectSPARQLQuery(queryString);
    }

}
