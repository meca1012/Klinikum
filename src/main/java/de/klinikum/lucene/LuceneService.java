package de.klinikum.lucene;

import de.klinikum.domain.TextBlock;


public interface LuceneService {
 
    boolean storeTextBlock(TextBlock textBlock);
    }
