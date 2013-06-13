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
import org.apache.lucene.queryparser.classic.ParseException;
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
import de.klinikum.exceptions.SpirontoException;

import de.klinikum.service.Implementation.NoteServiceImpl;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
    private IndexSearcher searcher;
    private IndexReader reader;
    private IndexWriter writer;
    private static Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_43);

    // CDI for NoteServiceClass
    @Inject
    NoteServiceImpl noteService;

    private static final Logger LOGGER = LoggerFactory.getLogger(LuceneServiceImpl.class);

    /**
     * @param indexDir
     * @param createNewIndex
     *            Success(True) if a new index been created.
     * @throws IOException
     */
    public LuceneServiceImpl() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL url = classLoader.getResource(CLASSFOLDER);
        String path = url.getPath();
        boolean success = new File(path + "/index").mkdirs();
        if (success) {
            LOGGER.info("Created IndexFolder  '" + path + "/index '...");
        }
    }

    /**
     * Purpose: Returns current folder Path for index location
     * 
     * @return
     */
    private URL getIndexPath() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return classLoader.getResource(FILEPATH);
    }

    /**
     * initialize Writer for Lucene - Document / Indexfile writing;
     * 
     * @param mode
     *            /Create/CreateOrRead/Read
     * @throws IOException
     * @throws URISyntaxException
     */
    public void initalizeWriter(OpenMode mode) throws IOException, URISyntaxException {
        // System.out.println("Indexing to directory '" + FILEPATH + "'...");
        Directory dir = FSDirectory.open(new File(getIndexPath().getPath()));
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
     * 
     */
    @Override
    public boolean storeNote(Note note) throws IOException, URISyntaxException {

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
            this.closeWriter();
        }
        return true;
    }

    /**
     * 
     * @param searchString
     *            Text that will be searched in index
     * @return List of URI which texts includes searchString
     * @throws ParseException
     *             -> If Query is not parseable
     * @throws IOException
     * @throws SpirontoException
     */
    public List<Note> searchNotes(LuceneSearchRequest request) throws ParseException, IOException, SpirontoException {

        this.reader = DirectoryReader.open(FSDirectory.open(new File(getIndexPath().getPath()))); // only searching, so
                                                                                                  // read-only=true
        this.searcher = new IndexSearcher(reader);

        List<Note> returnUriList = new ArrayList();
        String queryString = URIPATIENT + ": \"" + request.getPatientUri() + "\" AND text: " + request.getSearchString();
  
        Query query = new QueryParser(Version.LUCENE_43, NOTETEXT, analyzer).parse(queryString);

        int hitsPerPage = 10;

        this.searcher = new IndexSearcher(this.reader);
        TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
        searcher.search(query, collector);
        ScoreDoc[] hits = collector.topDocs().scoreDocs;

        for (ScoreDoc hit : hits) {
            int docId = hit.doc;
            Document d = searcher.doc(docId);
            Note sesameNote = noteService.getNoteByUri(d.get(URINOTE));

            if (sesameNote != null) {
                returnUriList.add(sesameNote);
            }
        }
        return returnUriList;
    }
}
