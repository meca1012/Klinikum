package de.klinikum.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "patientDTO")
@XmlAccessorType(XmlAccessType.FIELD)
public class PatientDTO implements Serializable {

    private static final long serialVersionUID = 3411870587425227543L;

    @XmlElement(name = "patient")
    private Patient patient;

    @XmlElement(name = "concept")
    private Concept concept;

    // @XmlTransient
    @XmlElement(name = "tabConcept")
    private boolean tabConcept;

    public PatientDTO() {
    }

    public Patient getPatient() {
        return this.patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Concept getConcept() {
        return this.concept;
    }

    public void setConcept(Concept concept) {
        this.concept = concept;
    }

    public boolean isTabConcept() {
        return this.tabConcept;
    }

    public void setTabConcept(boolean tabConcept) {
        this.tabConcept = tabConcept;
    }

}
