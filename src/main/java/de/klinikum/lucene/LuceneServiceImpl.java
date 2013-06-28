package de.klinikum.lucene;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.inject.Named;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.klinikum.domain.LuceneSearchRequest;
import de.klinikum.domain.Note;
import de.klinikum.helper.PropertyLoader;

/**
 * 
 * LuceneServiceImpl.java Purpose: Lucene-Search -> Supports a search through all indexed elements. Stores Index to Path
 * defined by lucene.properties.
 * 
 * @author Constantin Treiber
 * @version 2.0 27/06/13
 */
@Named
public class LuceneServiceImpl implements LuceneService {

    private final static String URIPATIENT = "uriPatient";
    private final static String URINOTE = "uriNote";
    private final static String NOTETITLE = "noteTitle";
    private final static String NOTETEXT = "noteText";
    private static final String luceneIndexPath = "lucene.indexPath";

    private IndexSearcher indexSearcher;
    private IndexReader indexReader;
    private IndexWriter indexWriter;
    private final String indexPath;

    private static final String configName = "lucene.properties";
    private static Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_43);
    private final PropertyLoader propertyLoader;

    private static final Logger LOGGER = LoggerFactory.getLogger(LuceneServiceImpl.class);

    /**
     * Purpose: Initializes the LuceneService Sets indexPath from lucene.properties Creates indexFolder if folder does
     * not exist Success(True) if a new index been created. Path to Index is configurable through lucene.propterties
     * 
     * @throws Exception
     */
    public LuceneServiceImpl() throws Exception {

        this.propertyLoader = new PropertyLoader();
        Properties propFile = this.propertyLoader.load(configName);
        this.indexPath = propFile.getProperty(luceneIndexPath);
        boolean success = new File(this.indexPath).mkdirs();
        if (success) {
            LOGGER.info("Created IndexFolder  '" + this.indexPath + "....");
        }
    }

    /**
     * Purpose: Returns current folder Path for Index-Location Reloading just if class was rebuild by CDI- container
     * 
     * @return current folder path for index
     * 
     * @throws Exception
     *             if file is not present
     */
    private String getIndexPath() throws Exception {
        if (this.indexPath == null || this.indexPath.isEmpty()) {
            Properties propFile = this.propertyLoader.load(configName);
            return propFile.getProperty(luceneIndexPath);
        }
        else {
            return this.indexPath;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.klinikum.lucene.LuceneService#initalizeWriter(org.apache.lucene.index.IndexWriterConfig.OpenMode)
     */
    @Override
    public void initalizeWriter(OpenMode mode) throws Exception {
        Directory dir = FSDirectory.open(new File(this.getIndexPath()));
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_43);
        IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_43, analyzer);
        iwc.setOpenMode(mode);
        this.indexWriter = new IndexWriter(dir, iwc);

        if (mode == OpenMode.CREATE) {
            this.indexWriter.close();
        }

    }

    /**
     * Purpose: Closes indexWriter. Index is locked while indexWriter is open
     * 
     * @throws IOException
     *             if indexFolder is corrupt or not present
     */
    private void closeWriter() throws IOException {
        this.indexWriter.close();
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.klinikum.lucene.LuceneService#storeNote(de.klinikum.domain.Note)
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

        try {
            this.indexWriter.addDocument(doc);
        }
        catch (Exception e) {
            LOGGER.info("DocSave Failed \n" + e.getMessage());
            return false;
        }
        finally {
            // Ensure that index is closed. Open indexFiles are locked!
            this.closeWriter();
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.klinikum.lucene.LuceneService#deleteNote(de.klinikum.domain.Note)
     */
    @Override
    public boolean deleteNote(Note note) throws Exception {
        if (note == null || note.getUri() == null || note.getUri().isEmpty())
            return false;

        this.indexReader = DirectoryReader.open(FSDirectory.open(new File(this.getIndexPath())));

        String queryString = URINOTE + ":\"" + note.getUri() + "\"";
        Query query = new QueryParser(Version.LUCENE_43, NOTETEXT, analyzer).parse(queryString);

        try {
            this.initalizeWriter(OpenMode.CREATE_OR_APPEND);
            this.indexWriter.deleteDocuments(query);
            this.indexWriter.commit();
            LOGGER.info("Deleting for '" + note.getUri() + "done");
        }
        catch (Exception e) {
            this.indexWriter.rollback();
            LOGGER.info("Rollback for '" + note.getPatientUri() + "done \n");
            LOGGER.info("Check Consitenzy");
        }
        finally {
            this.closeWriter();
        }

        // Searching for deleted document -> Consistency check
        TopScoreDocCollector collector = TopScoreDocCollector.create(1, true);
        this.indexReader = DirectoryReader.open(FSDirectory.open(new File(this.getIndexPath())));
        this.indexSearcher = new IndexSearcher(this.indexReader);
        this.indexSearcher.search(query, collector);

        ScoreDoc[] hits = collector.topDocs().scoreDocs;
        if (hits.length > 0) {
            int docId = hits[0].doc;
            Document document = this.indexSearcher.doc(docId);
            String URI = document.get(URINOTE);

            if (URI == note.getUri()) {

                return false;
            }
            LOGGER.info("Note '" + note.getUri() + " deleted");
        }
        return true;

    }

    /*
     * (non-Javadoc)
     * 
     * @see de.klinikum.lucene.LuceneService#searchNotes(de.klinikum.domain.LuceneSearchRequest)
     */
    @Override
    public List<String> searchNotes(LuceneSearchRequest request) throws Exception {

        this.indexReader = DirectoryReader.open(FSDirectory.open(new File(this.getIndexPath())));
        // only searching, so
        // read-only=true
        this.indexSearcher = new IndexSearcher(this.indexReader);

        List<String> returnUriList = new ArrayList<String>();
        String queryString = URIPATIENT + ":\"" + request.getPatientUri() + "\" AND (" + NOTETEXT + ":"
                + request.getSearchString() + " OR " + NOTETITLE + ":" + request.getSearchString() + ")";

        // Parse String to Query
        Query query = new QueryParser(Version.LUCENE_43, NOTETEXT, analyzer).parse(queryString);

        // Sets maximum of returnHits
        int hitsPerPage = 10;

        TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
        this.indexSearcher.search(query, collector);

        ScoreDoc[] hits = collector.topDocs().scoreDocs;

        // Create returnUrIList from Lucene- Hits
        for (ScoreDoc hit : hits) {
            int docId = hit.doc;
            Document d = this.indexSearcher.doc(docId);
            String URI = d.get(URINOTE);

            if (URI != null) {
                returnUriList.add(URI);
            }
        }
        return returnUriList;
    }

    @Override
    public boolean updateNote(Note note) {
        try {
            this.deleteNote(note);
            this.storeNote(note);
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
