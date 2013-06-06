package de.klinikum.lucene;

import java.io.IOException;
import java.util.Iterator;

import de.klinikum.domain.Concept;
import de.klinikum.domain.TextBlock;


public interface LuceneService {
 
    boolean storeTextBlock(TextBlock textBlock);
    }
