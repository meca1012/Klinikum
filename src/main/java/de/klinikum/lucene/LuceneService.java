package de.klinikum.lucene;

import java.io.IOException;
import java.net.URISyntaxException;

import de.klinikum.domain.Note;


public interface LuceneService {
 
    boolean storeNote(Note note) throws IOException, URISyntaxException;
    }
