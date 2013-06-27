package de.klinikum.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * 
 * @author Andreas Schillinger
 *
 */
@XmlRootElement(name = "concepts")
@XmlAccessorType(XmlAccessType.FIELD)
public class ConceptList {

    @XmlElement(name = "concept")
    private List<Concept> concepts;
    
    public ConceptList() {
        this.concepts = new ArrayList<Concept>();
    }
    
    public ConceptList(List<Concept> concepts) {
        this.concepts = concepts;
    }

    public boolean isEmpty() {
        if (this.concepts.size() == 0)
            return true;
        else
            return false;
    }

    public int size() {
        return this.concepts.size();
    }

    public List<Concept> getConceptList() {
        return concepts;
    }
    public void setConceptList(List<Concept> concepts) {
        this.concepts = concepts;
    }
}
