package de.klinikum.service;

import static de.klinikum.domain.NameSpaces.PATIENT_HAS_TEXTBLOCK;
import static de.klinikum.domain.NameSpaces.TEXTBLOCK_HAS_DATE;
import static de.klinikum.domain.NameSpaces.TEXTBLOCK_HAS_TEXT;
import static de.klinikum.domain.NameSpaces.TEXTBLOCK_POINTS_TO_CONCEPT;
import static de.klinikum.domain.NameSpaces.TEXTBLOCK_TYPE;

import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;

import org.openrdf.model.Literal;
import org.openrdf.model.URI;
import org.openrdf.model.vocabulary.RDF;

import de.klinikum.domain.Concept;
import de.klinikum.domain.TextBlock;
import de.klinikum.persistence.SesameTripleStore;

public class TextBlockServiceImpl implements TextBlockService{
	
	@Inject
	SesameTripleStore tripleStore;
	
	@Override
	public TextBlock createTextBlock(TextBlock textBlock) throws IOException {
		
		URI textBlockUri = this.tripleStore.getUniqueURI(TEXTBLOCK_TYPE.toString());
		URI textBlockTypeUri = this.tripleStore.getValueFactory().createURI(TEXTBLOCK_TYPE.toString());
		
		textBlock.setUri(textBlockUri.toString());
		
		this.tripleStore.addTriple(textBlockUri, RDF.TYPE, textBlockTypeUri);
		this.tripleStore.addTriple(textBlock.getPatientUri(), PATIENT_HAS_TEXTBLOCK.toString(), textBlock.getUri());
		
		Literal createdLiteral = this.tripleStore.getValueFactory().createLiteral(textBlock.getCreated());
		Literal textLiteral = this.tripleStore.getValueFactory().createLiteral(textBlock.getText());
		
		URI hasDateUri = this.tripleStore.getValueFactory().createURI(TEXTBLOCK_HAS_DATE.toString());
		URI hasTextUri = this.tripleStore.getValueFactory().createURI(TEXTBLOCK_HAS_TEXT.toString());
		
		this.tripleStore.addTriple(textBlockUri, hasDateUri , createdLiteral);
		this.tripleStore.addTriple(textBlockUri, hasTextUri , textLiteral);
		
		if (textBlock.getConcepts() != null || !textBlock.getConcepts().isEmpty()) {
			for (Concept c : textBlock.getConcepts()) {
				addConceptToTextBlock(textBlock, c);
			}
		}
		return textBlock;
	}
	
	@Override
	public TextBlock addConceptToTextBlock(TextBlock textBlock, Concept concept) throws IOException {
		if (this.tripleStore.repositoryHasStatement(textBlock.getUri(), TEXTBLOCK_POINTS_TO_CONCEPT.toString(), concept.getUri())) {
			return null;
		}
		if (textBlock.getConcepts() == null) {
			textBlock.setConcepts(new ArrayList<Concept>());
		}
		textBlock.addConcept(concept);
		this.tripleStore.addTriple(textBlock.getUri(), TEXTBLOCK_POINTS_TO_CONCEPT.toString(), concept.getUri());
		return textBlock;
	}
	
	@Override
	public TextBlock findTextBlock(){
		return null;
		
	};

}
