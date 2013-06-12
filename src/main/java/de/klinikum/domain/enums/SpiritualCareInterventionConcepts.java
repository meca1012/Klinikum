package de.klinikum.domain.enums;

public enum SpiritualCareInterventionConcepts {

	SCHWEIGEN("bedeutungsvolles Schweigen"),
	SPIRITUELLE_BEGLEITUNG("spirituelle Begleitung"),
	BERATUNG("Beratung");
	
	private final String value;

	SpiritualCareInterventionConcepts(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return this.value;
	}
	
}
