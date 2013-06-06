package de.klinikum.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import edu.emory.mathcs.backport.java.util.Collections;

@XmlRootElement(name = "textBlock")
@XmlAccessorType(XmlAccessType.FIELD)
public class TextBlock implements Serializable {

	private static final long serialVersionUID = 1051914267061275536L;

	@XmlElement(name = "uri")
	private String uri;
	
	@XmlElement(name = "created")
	private Date created;
	
	@XmlElement(name = "text")
	private String text;
	
	@XmlElement(name = "patientUri")
	private String patientUri;
	
	@XmlElement(name = "concepts")
	private List<Concept> concepts;
	
	public TextBlock() {
	}
	
	public TextBlock(String uri, Date created, String text) {
		this.uri = uri;
		this.created = created;
		this.text = text;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getPatientUri() {
		return patientUri;
	}

	public void setPatientUri(String patientUri) {
		this.patientUri = patientUri;
	}

	public List<Concept> getConcepts() {
		if (this.concepts == null) {
			return null;
		}
		return Collections.unmodifiableList(this.concepts);
	}

	public void setConcepts(List<Concept> concepts) {
		if (this.concepts == null) {
			this.concepts = concepts;
			return;
		}
		this.concepts.clear();
		if (this.concepts != null) {
			this.concepts.addAll(concepts);
		}
	}
	
	public TextBlock addConcept(Concept concept) {
		if (this.concepts == null) {
			this.concepts = new ArrayList<Concept>();
		}
		this.concepts.add(concept);
		return this;
	}
}
