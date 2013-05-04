package de.klinikum.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "patient")
public class Patient implements Serializable {

	private static final long serialVersionUID = 1199647317278849602L;

	private String uri;

	private String vName;

	private String nName;

	public Patient() {
	}

	public Patient(String uri, String vName, String nName) {

		this.uri = uri;
		this.vName = vName;
		this.nName = nName;
	}

	@XmlElement
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	@XmlElement
	public String getvName() {
		return vName;
	}

	public void setvName(String vName) {
		this.vName = vName;
	}

	@XmlElement
	public String getnName() {
		return nName;
	}

	public void setnName(String nName) {
		this.nName = nName;
	}
}
