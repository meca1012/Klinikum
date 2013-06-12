package de.klinikum.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * Concept.java
 * Purpose: DomainObject to Store Sesame- Data and Exchange 
 *          Concepts represents Ontology- Data 
 * @author  Spironto Team 1
 * @version 1.0 08/06/13
 */

@XmlRootElement(name = "concept")
@XmlAccessorType(XmlAccessType.FIELD)
public class Concept implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 8473789769254404263L;

    @XmlElement(name = "uri")
    private String uri;

    @XmlElement(name = "label")
    private String label;
    
    @XmlElement(name = "patientUri")
    private String patientUri;
    
    @XmlElement(name = "connectedConcepts")
    private List<Concept> connectedConcepts;

    public Concept() {
    }

    public Concept(String uri, String label, String patientUri) {
        this.uri = uri;
        this.label = label;
        this.patientUri = patientUri;
    }

    public String getUri() {
        return this.uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

	public String getPatientUri() {
		return patientUri;
	}

	public void setPatientUri(String patientUri) {
		this.patientUri = patientUri;
	}

	public List<Concept> getConnectedConcepts() {
		if (this.connectedConcepts == null) {
			return null;
		}
		return this.connectedConcepts;
	}

	public void setConnectedConcepts(List<Concept> connectedConcepts) {
		if (this.connectedConcepts == null) {
			this.connectedConcepts = connectedConcepts;
			return;
		}
		this.connectedConcepts.clear();
		if (this.connectedConcepts != null) {
			this.connectedConcepts.addAll(connectedConcepts);
		}		
	}
	
	public Concept addConnectedConcepts(Concept concept) {
		if (this.connectedConcepts == null) {
			this.connectedConcepts = new ArrayList<Concept>();
		}
		this.connectedConcepts.add(concept);
		return this;
	}	
}
