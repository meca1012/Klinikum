package de.klinikum.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Address implements Serializable {

    private static final long serialVersionUID = -773853139216571017L;

    private String uri;

    private String street;

    private String streetNumber;

    private String city;

    private String zip;

    private String country;

    private String phone;

    public Address() {
    }

    public Address(String uri, String street, String streetNumber, String city, 
            String zip, String country, String phone) {
        this.uri = uri;
        this.street = street;
        this.streetNumber = streetNumber;
        this.city = city;
        this.zip = zip;
        this.country = country;
        this.phone = phone;

    }

    public String getUri() {
        return this.uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getStreet() {
        return this.street;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getStreetNumber() {
        return this.streetNumber;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return this.zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
