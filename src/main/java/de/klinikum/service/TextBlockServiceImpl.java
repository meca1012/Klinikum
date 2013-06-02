package de.klinikum.service;

import static de.klinikum.domain.NameSpaces.ONTOLOGIE_CONCEPT_HAS_LABEL;
import static de.klinikum.domain.NameSpaces.PATIENT_HAS_CONCEPT;
import static de.klinikum.domain.NameSpaces.PATIENT_HAS_TEXTBLOCK;
import static de.klinikum.domain.NameSpaces.TEXTBLOCK_HAS_DATE;
import static de.klinikum.domain.NameSpaces.TEXTBLOCK_HAS_TEXT;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.openrdf.model.Literal;
import org.openrdf.model.Model;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.repository.RepositoryException;

import de.klinikum.domain.Concept;
import de.klinikum.domain.Patient;
import de.klinikum.persistence.SesameTripleStore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.openrdf.model.Value;
import org.openrdf.model.vocabulary.RDF;

import de.klinikum.domain.Concept;
import de.klinikum.domain.Patient;
import de.klinikum.domain.TextBlock;
import de.klinikum.persistence.SesameTripleStore;

public class TextBlockServiceImpl implements TextBlockService {

	@Inject
	SesameTripleStore tripleStore;

	@Override
	public TextBlock createTextBlock() {
		return null;

	};

	@Override
	public List<TextBlock> findTextBlocks(Patient patient) {

		List<TextBlock> textBlocks = new ArrayList<TextBlock>();

		// String sparqlQuery = "SELECT ?Uri ?Label WHERE {";
		//
		// sparqlQuery += "<" + patient.getUri().toString() + "> <" +
		// PATIENT_HAS_CONCEPT + "> ?Uri . ";
		//
		// sparqlQuery += "?Uri <" + RDF.TYPE + "> <" + ONTOLOGIE_CONCEPT_TYPE +
		// "> . ";
		//
		// sparqlQuery += "?Uri <" + ONTOLOGIE_CONCEPT_HAS_LABEL + "> ?Label}";
		//
		// Set<HashMap<String, Value>> queryResult =
		// this.tripleStore.executeSelectSPARQLQuery(sparqlQuery);
		//
		// for (HashMap<String, Value> item : queryResult) {
		// Concept concept = new Concept();
		// concept.setUri(item.get("Uri").toString());
		// concept.setLabel(item.get("Label").stringValue());
		// concept.setPatientUri(patient.getUri());
		// concepts.add(concept);
		// }
		return textBlocks;
	}

	@Override
	public TextBlock findTextBlock(String textBlockUri) {

		TextBlock textBlock = new TextBlock();
		textBlock.setUri(textBlockUri);

		String sparqlQuery = "SELECT ?text ?patientUri ?created WHERE {";

		sparqlQuery += "<" + textBlockUri + "> <" + TEXTBLOCK_HAS_TEXT
				+ "> ?text . ";
		sparqlQuery += "<" + textBlockUri + "> <" + TEXTBLOCK_HAS_DATE
				+ "> ?created . ";
		sparqlQuery += "?patientUri <" + PATIENT_HAS_TEXTBLOCK + "> <"
				+ textBlockUri + ">}";

		Set<HashMap<String, Value>> queryResult = this.tripleStore
				.executeSelectSPARQLQuery(sparqlQuery);
		for (HashMap<String, Value> item : queryResult) {
			textBlock.setText(item.get("text").stringValue());
			textBlock.setCreated(item.get("created").stringValue());
			textBlock.setPatientUri(item.get("patientUri").toString());
		}
		return textBlock;
	}

}
