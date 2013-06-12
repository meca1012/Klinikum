package de.klinikum.domain.enums;

public enum FamilyConcepts {

	MUTTER("Mutter"), 
	VATER("Vater"), 
	TANTE("Tante"), 
	ONKEL("Onkel"), 
	GROSSVATER("Gro�vater"), 
	GROSSMUTTER("Gro�mutter");

	private final String value;

	FamilyConcepts(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return this.value;
	}

}
