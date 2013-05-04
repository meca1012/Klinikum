package de.klinikum.domain;

import java.io.Serializable;

public class Patient implements Serializable {
	
	private static final long serialVersionUID = 1199647317278849602L;

	private String uri;
	
	private String vName;
	private String nName;
	
	public Patient(String uri, String vName, String nName) {
		
		this.uri = uri;
		this.vName = vName;
		this.nName = nName;
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
}
