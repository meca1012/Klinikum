package de.klinikum.lucene;

import de.klinikum.domain.Note;


public interface LuceneService {
 
    boolean storeTextBlock(Note textBlock);
    }
