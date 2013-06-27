package service;

import java.util.List;

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

import de.klinikum.domain.LuceneSearchRequest;
import de.klinikum.domain.Note;
import de.klinikum.lucene.LuceneService;
import de.klinikum.lucene.LuceneServiceImpl;
import de.klinikum.persistence.SesameTripleStore;
import de.klinikum.service.implementation.ConceptServiceImpl;
import de.klinikum.service.implementation.NoteServiceImpl;
import de.klinikum.service.implementation.PatientServiceImpl;

/**
* 
* Testing for LuceneServices
* 
* @author Constantin Treiber
* @version 1.0 20/06/13
*/

@RunWith(Arquillian.class)
public class LuceneServiceTest {
    
    @Inject
    LuceneService luceneService;
    
    private final static String PATIENTURI = "http://spironto.de/spironto#patient-gen11";;
    private final static String NOTEURI_1 = "http://spironto.de/spironto#note-gen1";
    private final static String NOTEURI_2 = "http://spironto.de/spironto#note-gen2";
    private final static String NOTEURI_3 = "http://spironto.de/spironto#note-gen3";
    private final static String NOTETITLE = "endless important";
    private final static String NOTETEXT = "This is a wonderfull endless text about the most important patient N°11";
    private final static int NOTEPRIO = 1;
    private final static String SEARCHSTRING = "endless";
    private DateTime date = new DateTime();
    
    private LuceneSearchRequest request;
    private Note note;
    
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
    
    
    @Before
    public void createNote() {
       //Creates Note
        Note mNote = new Note();
        mNote.setUri(NOTEURI_1);
        mNote.setPatientUri(PATIENTURI);
        mNote.setCreated(date);
        mNote.setPriority(NOTEPRIO);
        mNote.setText(NOTETEXT);
        mNote.setTitle(NOTETITLE);
        this.note  = mNote;
        
       //Create SearchRequeset

        LuceneSearchRequest mRequest = new LuceneSearchRequest();
        mRequest.setPatientUri(PATIENTURI);
        mRequest.setSearchString(SEARCHSTRING);
        this.request = mRequest;
        
    }
    
    
    
        
    @Test 
    public void luceneStoreNoteTest() throws Exception
    {
        boolean stored = this.luceneService.storeNote(this.note);     
        boolean found = false;
        
        Assert.assertEquals(true, stored);
        
        if(stored)
        {
            List<String> returnUriList = this.luceneService.searchNotes(this.request);
            for(String uri : returnUriList)
            {
                 if(uri.equals(NOTEURI_1))
                 {
                     found = true;
                 }
            }
            
            Assert.assertEquals(true, found);
        }
        
    }
    
    @Test
    public void luceneDeleteNoteTest() throws Exception
    {
        //Making Sure that NOTEURI is just once in index
        this.note.setUri(NOTEURI_2);
        boolean stored = this.luceneService.storeNote(this.note);  
        boolean found = false;
        Assert.assertEquals(true, stored);
        
        boolean deleted = this.luceneService.deleteNote(this.note);     

        if(stored && deleted)
        {
            List<String> returnUriList = this.luceneService.searchNotes(this.request);
            for(String uri : returnUriList)
            {
                 if(uri.equals(NOTEURI_2))
                 {
                     found = true;
                 }
            }
            
            Assert.assertEquals(false, found);
        }
       
    }
    
    @Test
    public void luceneSearchTest() throws Exception {
        {
            this.note.setUri(NOTEURI_3);
            boolean stored = this.luceneService.storeNote(this.note);     
            boolean found = false;
            
            Assert.assertEquals(true, stored);
            
            if(stored)
            {
                List<String> returnUriList = this.luceneService.searchNotes(this.request);
                for(String uri : returnUriList)
                {
                     if(uri.equals(NOTEURI_3))
                     {
                         found = true;
                     }
                }
                
                Assert.assertEquals(true, found);
            }
            
        }
        
    }
        
}