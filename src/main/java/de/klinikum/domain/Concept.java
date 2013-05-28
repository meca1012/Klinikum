package de.klinikum.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "concept")
@XmlAccessorType(XmlAccessType.FIELD)
public class Concept implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8473789769254404263L;

	@XmlElement(name = "uri")
	private String uri; 
	
	@XmlElement(name = "label")
	private String label;
	
	public Concept(){
	}
	
	public Concept (String uri, String label){
		this.uri = uri;
		this.label = label;
	}
	
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}

}
