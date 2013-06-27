package de.klinikum.lucene;

import java.util.List;

import org.apache.lucene.index.IndexWriterConfig.OpenMode;

import de.klinikum.domain.LuceneSearchRequest;
import de.klinikum.domain.Note;

/**
 * 
 * LuceneServiceImpl.java 
 * Purpose: Lucene Textsearch -> Supports a search through all indexed elements
 * Stores Index to Path defined by lucene.propertiesstores 
 * /de/klinikum/lucene/index in deployed filestructure
 * 
 * @author Constantin Treiber
 * @version 1.0 08/06/13
 */

public interface LuceneService {

    /**
     * initialize Writer for Lucene - Document / Indexfile writing;
     * 
     * @param mode
     *            /Create/CreateOrRead/Read
     * @throws Exception
     */
    public abstract void initalizeWriter(OpenMode mode) throws Exception;

    /**
     * Purpose -> Mapping from Note to Doc and Storing to Index
     * 
     * @throws Exception
     * 
     */
    public abstract boolean storeNote(Note note) throws Exception;

    /**
     * 
     * @param note
     *            -> Consumes Note.class Purpose: Delete Notes from Index
     * @return true or false depending on delete- state
     * @throws Exception
     */
    public abstract boolean deleteNote(Note note) throws Exception;

    /**
     * 
     * @param searchString
     *            Text that will be searched in index
     * @return List of URI which texts includes searchString
     * @throws Exception
     */
    public abstract List<String> searchNotes(LuceneSearchRequest request) throws Exception;

}
