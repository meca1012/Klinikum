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

	@XmlElement(name = "firstName")
	private String firstName;

	@XmlElement(name = "lastName")
	private String lastName;
	
	@XmlElement(name = "dateOfBirth")
	@XmlJavaTypeAdapter(XmlDateAdapter.class)  
	private Date dateOfBirth;
	
	@XmlElement(name="address")
	private Address address;


	public Patient() {
	}

	public Patient(String uri, String firstName, String lastName, Address address) {

		this.uri = uri;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
	}
	
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
	
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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
