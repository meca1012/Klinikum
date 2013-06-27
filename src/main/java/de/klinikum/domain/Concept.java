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
 * @author  Ivan Tepeluk, Matthias Schwarzenbach
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
    
    @XmlElement(name = "isEditable")
    private boolean isEditable = true;

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

	public boolean isEditable() {
		return isEditable;
	}

	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((connectedConcepts == null) ? 0 : connectedConcepts
						.hashCode());
		result = prime * result + (isEditable ? 1231 : 1237);
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result
				+ ((patientUri == null) ? 0 : patientUri.hashCode());
		result = prime * result + ((uri == null) ? 0 : uri.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Concept other = (Concept) obj;
		if (connectedConcepts == null) {
			if (other.connectedConcepts != null)
				return false;
		} else if (!connectedConcepts.equals(other.connectedConcepts))
			return false;
		if (isEditable != other.isEditable)
			return false;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		if (patientUri == null) {
			if (other.patientUri != null)
				return false;
		} else if (!patientUri.equals(other.patientUri))
			return false;
		if (uri == null) {
			if (other.uri != null)
				return false;
		} else if (!uri.equals(other.uri))
			return false;
		return true;
	}	
}
