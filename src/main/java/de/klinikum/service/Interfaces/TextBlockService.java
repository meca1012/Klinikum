package de.klinikum.service.Interfaces;

import java.io.IOException;
import java.util.List;

import de.klinikum.domain.Concept;
import de.klinikum.domain.Patient;
import de.klinikum.domain.TextBlock;
import de.klinikum.exceptions.SpirontoException;

public interface TextBlockService {

    TextBlock createTextBlock(TextBlock textBlock) throws IOException;

    List<TextBlock> findTextBlocks(Patient patient) throws SpirontoException;

    TextBlock findTextBlock(String uri) throws SpirontoException;

    TextBlock addConceptToTextBlock(TextBlock textBlock, Concept concept) throws IOException;

}
