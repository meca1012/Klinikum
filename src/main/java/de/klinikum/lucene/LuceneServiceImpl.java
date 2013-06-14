package de.klinikum.lucene;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.klinikum.domain.LuceneSearchRequest;
import de.klinikum.domain.Note;
import de.klinikum.exceptions.SpirontoException;
import de.klinikum.helper.PropertyLoader;

import de.klinikum.service.Implementation.NoteServiceImpl;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * 
 * LuceneServiceImpl.java Purpose: Lucene Textsearch Supports a search through all indexed Elements Stores index in
 * /de/klinikum/lucene/index in deployed Filestructure
 * 
 * @author Spironto Team 1
 * @version 1.0 08/06/13
 */
@Named
public class LuceneServiceImpl implements LuceneService {

    private final static String FILEPATH = "de/klinikum/lucene/index";
    private final static String CLASSFOLDER = "de/klinikum/lucene";
    private final static String URIPATIENT = "uriPatient";
    private final static String URINOTE = "uriNote";
    private final static String NOTETITLE = "noteTitle";
    private final static String NOTETEXT = "text";
    private static final String luceneIndexPath = "lucene.indexPath";

    private IndexSearcher searcher;
    private IndexReader reader;
    private IndexWriter writer;
    private String lucenePath;
    
    private static final String configName = "lucene.properties";
    private static Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_43);
    private PropertyLoader propertyLoader;

    private static final Logger LOGGER = LoggerFactory.getLogger(LuceneServiceImpl.class);

    /**
     * @param indexDir
     * @param createNewIndex
     *            Success(True) if a new index been created.
     *            Path to Index is configurable through lucene.propterties
     * @throws Exception 
     */
    public LuceneServiceImpl () throws Exception {
        
        propertyLoader = new PropertyLoader();
        Properties propFile = propertyLoader.load(configName);
        this.lucenePath = propFile.getProperty(luceneIndexPath);
        boolean success = new File(this.lucenePath).mkdirs();
        if (success) {
            LOGGER.info("Created IndexFolder  '" + this.lucenePath + "....");
        }
    }

    /**
     * Purpose: Returns current folder Path for index location
     * Reloading of Path just if class was rebuild by the Container
     * Should never happen
     * @return
     * @throws Exception 
     */
    private String getIndexPath() throws Exception {
       
        if(this.lucenePath == null || this.lucenePath.isEmpty()) {
            Properties propFile = propertyLoader.load(configName);
            return propFile.getProperty(luceneIndexPath);
        }
        else
        {
            return this.lucenePath;
        }
    }

    /**
     * initialize Writer for Lucene - Document / Indexfile writing;
     * 
     * @param mode
     *            /Create/CreateOrRead/Read
     * @throws Exception 
     */
    public void initalizeWriter(OpenMode mode) throws Exception {
        // System.out.println("Indexing to directory '" + FILEPATH + "'...");
        Directory dir = FSDirectory.open(new File(getIndexPath()));
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_43);
        IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_43, analyzer);
        iwc.setOpenMode(mode);
        this.writer = new IndexWriter(dir, iwc);

        if (mode == OpenMode.CREATE) {
            this.writer.close();
        }

    }

    /**
     * Purpose: Closes Writer after Writing -> File is not locked
     * 
     * @throws IOException
     */
    private void closeWriter() throws IOException {
        this.writer.close();
    }

    /**
     * Purpose -> Mapping from Note to Doc and Storing to Index
     * @throws Exception 
     * 
     */
    @Override
    public boolean storeNote(Note note) throws Exception {

        Document doc = new Document();

        this.initalizeWriter(OpenMode.CREATE_OR_APPEND);
        if (note != null && note.getUri() != null && note.getPatientUri() != null && !note.getUri().isEmpty()
                && !note.getPatientUri().isEmpty()) {

            doc.add(new TextField(URINOTE, note.getUri(), Field.Store.YES));
            doc.add(new TextField(URIPATIENT, note.getPatientUri(), Field.Store.YES));

            if (note.getTitle() != null && !note.getTitle().isEmpty()) {
                doc.add(new TextField(NOTETITLE, note.getTitle(), Field.Store.YES));
            }

            if (note.getText() != null && !note.getText().isEmpty()) {
                doc.add(new TextField(NOTETEXT, note.getText(), Field.Store.YES));
            }

        }
        
        try 
        {
        this.writer.addDocument(doc);
        }
        catch(Exception e)
        {
            LOGGER.info("DocSave Failed \n" + e.getMessage());
            return false;
        }
        finally
        {
            // Ensure that index is closed. Open indexFiles are locked!
            this.closeWriter();
        }
        return true;
    }

    /**
     * 
     * @param note -> Consumes Note.class
     * Purpose: Delete Notes from Index 
     * @return true or false depending on delete- state
     * @throws IOException
     */
    public boolean deleteNote(Note note) throws IOException
    {
        if(note == null || note .getUri() == null || note.getUri().isEmpty() )   
                return false;
        
            LOGGER.info("Deleting Notes with URI '" + note.getPatientUri());

            TermQuery deleteTerm = new TermQuery(new Term(URINOTE, note.getUri()));
                 
            try
            {
                this.initalizeWriter(OpenMode.CREATE_OR_APPEND);
                this.writer.deleteDocuments(deleteTerm);      
                this.writer.commit();
                LOGGER.info("Deleting for '" + note.getUri() + "done");
            }
            catch(Exception e)
            {
                this.writer.rollback();
                LOGGER.info("Rollback for '" + note.getPatientUri() + "done \n");
                LOGGER.info("Check Consitenzy");
            }
            finally
            {
               this.closeWriter();
            }
            return true;
    }
    
    /**
     * 
     * @param searchString
     *            Text that will be searched in index
     * @return List of URI which texts includes searchString
     * @throws Exception 
     */
    public List<String> searchNotes(LuceneSearchRequest request) throws Exception {

        this.reader = DirectoryReader.open(FSDirectory.open(new File(getIndexPath()))); // only searching, so
                                                                                                  // read-only=true
        this.searcher = new IndexSearcher(reader);
        //TODO: CHECK OR PART OF QUERY 
        List<String> returnUriList = new ArrayList();
        String queryString = URIPATIENT + ": \"" + request.getPatientUri() 
                                        + "\" AND ( text: " 
                                        + request.getSearchString()
                                        + " OR title: " 
                                        + request.getSearchString() + ")";
        
        Query query = new QueryParser(Version.LUCENE_43, NOTETEXT, analyzer).parse(queryString);

        //Sets maximum of returnHits
        int hitsPerPage = 10;

        this.searcher = new IndexSearcher(this.reader);
        TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
        searcher.search(query, collector);
        ScoreDoc[] hits = collector.topDocs().scoreDocs;

        for (ScoreDoc hit : hits) {
            int docId = hit.doc;
            Document d = searcher.doc(docId);
            String URI = d.get(URINOTE);

            if (URI != null) {
                returnUriList.add(URI);
            }
        }
        return returnUriList;
    }
}
