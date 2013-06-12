package de.klinikum.domain.enums;

public enum FamilyConcepts {

	MUTTER("Mutter"), 
	VATER("Vater"), 
	TANTE("Tante"), 
	ONKEL("Onkel"), 
	GROSSVATER("Groﬂvater"), 
	GROSSMUTTER("Groﬂmutter");

	private final String value;

	FamilyConcepts(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return this.value;
	}

}
