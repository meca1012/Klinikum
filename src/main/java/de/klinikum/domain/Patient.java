package de.klinikum.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.joda.time.DateTime;

import de.klinikum.helper.XmlBirthDateAdapter;

/**
 * 
 * Concept.java
 * Purpose: DomainObject to Store Sesame- Data and Exchange 
 * Represents a Patient with his basic personal Data         
 * 
 * @author  Ivan Tepeluk, Matthias Schwarzenbach
 * @version 1.0 08/06/13
 */

@XmlRootElement(name = "patient")
@XmlAccessorType(XmlAccessType.FIELD)
public class Patient implements Serializable {

	private static final long serialVersionUID = 1199647317278849602L;
	@XmlElement(name = "uri")
	private String uri;
	
	@XmlElement(name = "patientNumber")
	private String patientNumber;
	
	@XmlElement(name = "firstName")
	private String firstName;

	@XmlElement(name = "lastName")
	private String lastName;
	
	@XmlElement(name = "dateOfBirth")
	@XmlJavaTypeAdapter(XmlBirthDateAdapter.class)  
	private DateTime dateOfBirth;
	
	@XmlElement(name = "address")
	private Address address;

	public Patient() {
	}

	public Patient(String uri, String patientNumber, String vName, String nName, Address address) {

		this.uri = uri;
		this.patientNumber = patientNumber;
		this.firstName = vName;
		this.lastName = nName;
		this.address = address;
	}
	
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
	
	
	public String getPatientNumber() {
		return patientNumber;
	}

	public void setPatientNumber(String patientNumber) {
		this.patientNumber = patientNumber;
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
	
	public DateTime getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(DateTime dayOfBirth) {
		this.dateOfBirth = dayOfBirth;
	}
	
	public boolean isValid() {	
		if (this.uri != null || !this.uri.isEmpty())
			return false;
		
		if (this.patientNumber == null || this.patientNumber.isEmpty())
			return false;
		
		if (this.firstName == null || this.firstName.isEmpty())
			return false;
		
		if (this.lastName == null || this.lastName.isEmpty())
			return false;
	
		if (this.dateOfBirth == null)
			return false;
		
		return true;
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result
				+ ((dateOfBirth == null) ? 0 : dateOfBirth.hashCode());
		result = prime * result
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result
				+ ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result
				+ ((patientNumber == null) ? 0 : patientNumber.hashCode());
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
		Patient other = (Patient) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (dateOfBirth == null) {
			if (other.dateOfBirth != null)
				return false;
		} else if (!dateOfBirth.equals(other.dateOfBirth))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (patientNumber == null) {
			if (other.patientNumber != null)
				return false;
		} else if (!patientNumber.equals(other.patientNumber))
			return false;
		if (uri == null) {
			if (other.uri != null)
				return false;
		} else if (!uri.equals(other.uri))
			return false;
		return true;
	}
	
}
