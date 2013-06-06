package de.klinikum.lucene;


import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import java.io.File;

import de.klinikum.domain.TextBlock;

public class LuceneServiceImpl implements LuceneService{

    private final static String FILEPATH = "/de/klinikum/lucence/";
    private IndexSearcher searcher;
    private IndexWriter writer;
    private Directory indexDir;
    private static Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_43);
    private static final String filePath = "/de/klinikum/properties/";
    /**
     * @param indexDir
     * @param createNewIndex True if a new index should be created. If an index already exists it is overwritten. 
     * @throws IOException
     */
    public LuceneServiceImpl() throws IOException {

        }

    public void createIndex() throws IOException
    {
        System.out.println("Indexing to directory '" + FILEPATH + "'...");
        getClass().getResourceAsStream(FILEPATH);
        
        Directory dir = FSDirectory.open(new File(FILEPATH));
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_43);
        IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_43, analyzer);
        final File docDir = new File(FILEPATH);
        iwc.setOpenMode(OpenMode.CREATE);
        IndexWriter writer = new IndexWriter(dir, iwc);
        writer.close();
    }
    
    @Override
    public boolean storeTextBlock(TextBlock textBlock) {
        // TODO Auto-generated method stub
        return false;
    }
    

}
