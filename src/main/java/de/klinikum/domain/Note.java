package de.klinikum.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

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
    private Date created;
    
    @XmlElement(name = "title")
    private String title;

    @XmlElement(name = "text")
    private String text;
    
    @XmlElement(name = "priority")
    private int priority;

    @XmlElement(name = "patientUri")
    private String patientUri;

    @XmlElement(name = "concepts")
    private List<Concept> concepts;

    public Note() {
    }

    public Note(String uri, Date created, String text) {
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

    public Date getCreated() {
        return this.created;
    }

    public void setCreated(Date created) {
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
        return this.concepts;
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

    public Note addConcept(Concept concept) {
        if (this.concepts == null) {
            this.concepts = new ArrayList<Concept>();
        }
        this.concepts.add(concept);
        return this;
    }
}
