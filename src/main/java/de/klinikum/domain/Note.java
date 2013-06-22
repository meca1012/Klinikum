package de.klinikum.domain;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.joda.time.DateTime;

import de.klinikum.helper.XmlDateTimeAdapter;

/**
 * 
 * Note.java Purpose: DomainObject to Store Sesame- Data and Exchange Stores Patient- Documentation and can be linked to
 * concepts
 * 
 * @author Spironto Team 1
 * @version 1.0 08/06/13
 */

@XmlRootElement(name = "note")
@XmlAccessorType(XmlAccessType.FIELD)
public class Note implements Serializable {

    private static final long serialVersionUID = 1051914267061275536L;

    @XmlElement(name = "uri")
    private String uri;

    @XmlElement(name = "created")
    @XmlJavaTypeAdapter(XmlDateTimeAdapter.class)
    private DateTime created;
    
    @XmlElement(name = "title")
    private String title;

    @XmlElement(name = "text")
    private String text;
    
    @XmlElement(name = "priority")
    private int priority;

    @XmlElement(name = "patientUri")
    private String patientUri;

    @XmlElement(name = "concepts")
    private ConceptList concepts;

    public Note() {
    }

    public Note(String uri, DateTime created, String text) {
        this.uri = uri;
        this.created = created;
        this.text = text;
    }

    public String getUri() {
        return this.uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public DateTime getCreated() {
        return this.created;
    }

    public void setCreated(DateTime created) {
        this.created = created;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getPatientUri() {
        return this.patientUri;
    }

    public void setPatientUri(String patientUri) {
        this.patientUri = patientUri;
    }

    public List<Concept> getConcepts() {
        if (this.concepts == null) {
            return null;
        }
        return this.concepts.getConceptList();
    }

    public void setConcepts(List<Concept> concepts) {
        this.concepts = new ConceptList(concepts);
    }    
    
    public Note addConcept(Concept concept) {
        if (this.concepts == null) {
            this.concepts = new ConceptList();
        }
        this.concepts.getConceptList().add(concept);
        return this;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((concepts == null) ? 0 : concepts.hashCode());
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result
				+ ((patientUri == null) ? 0 : patientUri.hashCode());
		result = prime * result + priority;
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		Note other = (Note) obj;
		if (concepts == null) {
			if (other.concepts != null)
				return false;
		} else if (!concepts.equals(other.concepts))
			return false;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (patientUri == null) {
			if (other.patientUri != null)
				return false;
		} else if (!patientUri.equals(other.patientUri))
			return false;
		if (priority != other.priority)
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (uri == null) {
			if (other.uri != null)
				return false;
		} else if (!uri.equals(other.uri))
			return false;
		return true;
	}
}
