package de.klinikum.service.implementation;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.openrdf.model.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.klinikum.exceptions.SpirontoException;
import de.klinikum.helper.PropertyLoader;
import de.klinikum.persistence.SesameTripleStore;
import de.klinikum.service.interfaces.SesameService;
/**
 * 
 * @author Constantin Treiber, Ivan Tepeluk, Carsten Meiser
 *
 */
@Named
public class SesameServiceImpl implements SesameService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SesameServiceImpl.class);
    private PropertyLoader propertyLoader;
    
    @Inject
    SesameTripleStore tripleStore;

    @Deprecated
    @Override
    public Set<HashMap<String, Value>> executeSPARQLQuery(String queryString) throws SpirontoException {

        return this.tripleStore.executeSelectSPARQLQuery(queryString);
    }

    @Override
    public boolean changeLanguageSetting(String language) throws IOException {
        this.propertyLoader = new PropertyLoader();
        return this.propertyLoader.changeLanguageSetting(language);

    }

}
