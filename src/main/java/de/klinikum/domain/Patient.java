package de.klinikum.domain;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import de.klinikum.helper.XmlDateAdapter;
@XmlRootElement(name = "patient")
@XmlAccessorType(XmlAccessType.FIELD)
public class Patient implements Serializable {

	private static final long serialVersionUID = 1199647317278849602L;
	@XmlElement(name = "uri")
	private String uri;

	@XmlElement(name = "vName")
	private String vName;

	@XmlElement(name = "nName")
	private String nName;
	
	@XmlElement(name = "dateOfBirth")
	@XmlJavaTypeAdapter(XmlDateAdapter.class)  
	private Date dateOfBirth;
	
	@XmlElement(name="address")
	private Address address;


	public Patient() {
	}

	public Patient(String uri, String vName, String nName, Address address) {

		this.uri = uri;
		this.vName = vName;
		this.nName = nName;
		this.address = address;
	}
	
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
	
	
	public String getvName() {
		return vName;
	}

	public void setvName(String vName) {
		this.vName = vName;
	}
	
	public String getnName() {
		return nName;
	}

	public void setnName(String nName) {
		this.nName = nName;
	}
	
	public Address getAddress() {
		return address;
	}
	
	public void setAddress(Address address) {
		this.address = address;
	}
	
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(Date dayOfBirth) {
		this.dateOfBirth = dayOfBirth;
	}
	
}
