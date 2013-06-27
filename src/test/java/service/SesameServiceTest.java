package service;

import java.io.IOException;
import java.util.Properties;

import javax.inject.Inject;

import junit.framework.Assert;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.klinikum.helper.PropertyLoader;
import de.klinikum.lucene.LuceneService;
import de.klinikum.lucene.LuceneServiceImpl;
import de.klinikum.persistence.SesameTripleStore;
import de.klinikum.service.implementation.ConceptServiceImpl;
import de.klinikum.service.implementation.NoteServiceImpl;
import de.klinikum.service.implementation.PatientServiceImpl;
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
    private static final String configName = "sesame.properties";
    private static final String configLanguage = "spironto.language";
    
    
    @Inject
    SesameService sesameService;
    
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(PatientServiceImpl.class)
                .addClass(ConceptServiceImpl.class)
                .addClass(NoteServiceImpl.class)
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
            Properties propFile = propertyLoader.load(configName);
            String languageFromProperty = propFile.getProperty(configLanguage);
            
            Assert.assertEquals(ENGLISH, languageFromProperty);
        
        }
        
        languangeChanged = this.sesameService.changeLanguageSetting(GERMAN);
        
        Assert.assertEquals(true, languangeChanged);
       
        if(languangeChanged) {   
            this.propertyLoader = new PropertyLoader();
            Properties propFile = propertyLoader.load(configName);
            String languageFromProperty = propFile.getProperty(configLanguage);
            
            Assert.assertEquals(GERMAN, languageFromProperty);
        
        }
        
        
    }
    
}