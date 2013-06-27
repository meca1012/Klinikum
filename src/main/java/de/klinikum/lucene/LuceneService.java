package de.klinikum.lucene;

import java.util.List;

import org.apache.lucene.index.IndexWriterConfig.OpenMode;

import de.klinikum.domain.LuceneSearchRequest;
import de.klinikum.domain.Note;

/**
 * 
 * @author Constantin Treiber
 *
 */
public interface LuceneService {

    /**
     * Purpose: Initializes Index-Writer in OpenMode Open Index-Directory Sets StandardAnalyser for Lucene-Version 4.3
     * Constructs indexWriter with given configuration
     * 
     * @param mode
     *            OpenMode Configuration / Read / Write / ReadWrite
     * @throws Exception
     *             if index opening fails
     */
    public abstract void initalizeWriter(OpenMode mode) throws Exception;

    /**
     * Purpose: Stores note to Lucene-Index. Creates searchable Lucene-Document from Note
     * 
     * @param   note
     *              Consumes Note to store
     * @return  boolean
     *               Return boolean true -> if stored / false -> if note could not been stored
     * @throws Exception
     *              Throwed if document can not be added to the index
     */
    public abstract boolean storeNote(Note note) throws Exception;

    /**
     * Purpose: Deletes note from Lucene-Index. Uses uri for identification
     * 
     * @param Cosumes
     *            Note to delete
     *            
     * @return boolean
     *          Return boolean true -> if deleted / false -> if note could not been deleted
     * 
     * @throws Exception
     *           Throwed if index not been initialized 
     */
    public abstract boolean deleteNote(Note note) throws Exception;

    /**
     * Purpose: Searches for Notes in Lucene-Index Opens Index Build searchQuery from LuceneSearchRequest-Object Fires
     * Query -> Max results is cut to 10 Items per search Stores Note-URI to returnUriList
     * 
     * @param request
     *              LuceneSearchRequest with includes patientUri searchString
     *            
     * @return List<String>
     *              Return List of uri found by Lucene
     * 
     * @throws Exception
     *             Throwed if index not been initialized 
     */
    public abstract List<String> searchNotes(LuceneSearchRequest request) throws Exception;

}
