package de.klinikum.service;

import java.util.List;

import de.klinikum.domain.Patient;

import java.io.IOException;

import de.klinikum.domain.Concept;
import de.klinikum.domain.TextBlock;

public interface TextBlockService {

	TextBlock createTextBlock(TextBlock textBlock) throws IOException;

	List<TextBlock> findTextBlocks(Patient patient);

	TextBlock findTextBlock(String uri);

	TextBlock addConceptToTextBlock(TextBlock textBlock, Concept concept)
			throws IOException;

}
