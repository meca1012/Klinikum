package de.klinikum.domain.enums;

public enum DiseaseConcepts {

	DIAGNOSE("Diagnose"), 
	MEDIKATION("Medikation"), 
	SYMPTOME("Symptome"), 
	SONSTIGES("Sonstiges");

	private final String value;

	DiseaseConcepts(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return this.value;
	}

}
