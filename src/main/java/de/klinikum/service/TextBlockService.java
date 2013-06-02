package de.klinikum.service;

import java.io.IOException;

import de.klinikum.domain.Concept;
import de.klinikum.domain.TextBlock;

public interface TextBlockService {
	
	TextBlock findTextBlock();

	TextBlock createTextBlock(TextBlock textBlock) throws IOException;
	
	TextBlock addConceptToTextBlock(TextBlock textBlock, Concept concept) throws IOException;

}
