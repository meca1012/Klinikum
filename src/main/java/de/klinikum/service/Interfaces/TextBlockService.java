package de.klinikum.service.Interfaces;

import java.util.List;

import de.klinikum.domain.Patient;

import java.io.IOException;

import de.klinikum.domain.Concept;
import de.klinikum.domain.TextBlock;

public interface TextBlockService {

	TextBlock createTextBlock(TextBlock textBlock) throws IOException;

	List<TextBlock> findTextBlocks(Patient patient) throws IOException;

	TextBlock findTextBlock(String uri) throws IOException;

	TextBlock addConceptToTextBlock(TextBlock textBlock, Concept concept)
			throws IOException;

}
