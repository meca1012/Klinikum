package de.klinikum.service;

import java.util.List;

import de.klinikum.domain.Patient;
import de.klinikum.domain.TextBlock;

public interface TextBlockService {

	TextBlock createTextBlock();
	
	List<TextBlock> findTextBlocks(Patient patient);
	
	TextBlock findTextBlock(String uri);

}
