package de.klinikum.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "patientDTO")
@XmlAccessorType(XmlAccessType.FIELD)
public class PatientDTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3411870587425227543L;

	@XmlElement(name = "uri")
	private Patient patient;
	
	@XmlElement(name = "uri")
	private Concept concept;
	
//	@XmlTransient
	@XmlElement(name = "uri")
	private boolean tabConcept;
	
	public PatientDTO(){
	}
	
	public Patient getPatient() {
		return patient;
	}
	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	public Concept getConcept() {
		return concept;
	}
	public void setConcept(Concept concept) {
		this.concept = concept;
	}
	public boolean isTabConcept() {
		return tabConcept;
	}
	public void setTabConcept(boolean tabConcept) {
		this.tabConcept = tabConcept;
	}

}
