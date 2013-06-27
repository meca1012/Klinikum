package service;

import java.util.HashMap;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

import javax.inject.Inject;

import junit.framework.Assert;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openrdf.model.Value;

import de.klinikum.domain.Address;
import de.klinikum.domain.Patient;
import de.klinikum.helper.PropertyLoader;
import de.klinikum.lucene.LuceneServiceImpl;
import de.klinikum.persistence.SesameTripleStore;
import de.klinikum.service.implementation.ConceptServiceImpl;
import de.klinikum.service.implementation.NoteServiceImpl;
import de.klinikum.service.implementation.PatientServiceImpl;
import de.klinikum.service.implementation.SesameServiceImpl;
import de.klinikum.service.interfaces.PatientService;
import de.klinikum.service.interfaces.SesameService;

/**
* 
* Testing for SesameServce
* 
* @author Constantin Treiber
* @version 1.0 20/06/13
*/

@RunWith(Arquillian.class)
    public class SesameServiceTest {
        
    private final static String GERMAN = "de";
    private final static String ENGLISH = "en";
    private PropertyLoader  propertyLoader;
    
    private static final String CONFIGNAME = "sesame.properties";
    private static final String CONFIGLANGUAGE = "spironto.language";
    Random generator = new Random(System.currentTimeMillis());
    
    @Inject
    SesameService sesameService;
    
    @Inject
    PatientService patientService;
    
    private Patient generateNewPatientWithAddress() {
        Patient patient = new Patient();
        patient.setFirstName("Alice");
        patient.setLastName("Smith");
        patient.setDateOfBirth(new DateTime());
        patient.setPatientNumber(String.valueOf(this.generator.nextInt()));
        Address address = new Address(null, "Musterstr.", "1", "Musterstadt", "76123", "D", "110");
        patient.setAddress(address);
        return patient;
    }
    
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(PatientServiceImpl.class)
                .addClass(ConceptServiceImpl.class)
                .addClass(NoteServiceImpl.class)
                .addClass(SesameServiceImpl.class)
                .addClass(LuceneServiceImpl.class)
                .addClass(SesameTripleStore.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    /**
     * Purpose Testing for changing the language settings
     * @throws Exception if PropertyLoader can not open property-files
     */
    @Test
    public void changeLanguageSettingTest() throws Exception {
    
        boolean languangeChanged = this.sesameService.changeLanguageSetting(ENGLISH);
        
        Assert.assertEquals(true, languangeChanged);
       
        if(languangeChanged) {   
            this.propertyLoader = new PropertyLoader();
            Properties propFile = propertyLoader.load(CONFIGNAME);
            String languageFromProperty = propFile.getProperty(CONFIGLANGUAGE);
            
            Assert.assertEquals(ENGLISH, languageFromProperty);
        
        }
        
        languangeChanged = this.sesameService.changeLanguageSetting(GERMAN);
        
        Assert.assertEquals(true, languangeChanged);
       
        if(languangeChanged) {   
            this.propertyLoader = new PropertyLoader();
            Properties propFile = propertyLoader.load(CONFIGNAME);
            String languageFromProperty = propFile.getProperty(CONFIGLANGUAGE);
            
            Assert.assertEquals(GERMAN, languageFromProperty);
        
        }
        
        
    }
    
    @Test
    public void executeSPARQLQueryTest() throws Exception { 
        Patient patient = this.generateNewPatientWithAddress();
        patient = this.patientService.createPatientRDF(patient);

        String sparqlQuery = "SELECT ?Uri WHERE {?Uri <http://spironto.de/spironto#has-patient-number>" + patient.getPatientNumber() + "}";
        Set<HashMap<String, Value>> result = this.sesameService.executeSPARQLQuery(sparqlQuery);
        
        Assert.assertNotNull(result);

    }
    
}