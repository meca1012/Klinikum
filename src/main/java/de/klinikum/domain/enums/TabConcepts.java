package de.klinikum.domain.enums;

public enum TabConcepts {
	
	KRANKHEITSVERLAUF ("Krankheitsverlauf"),
	FAMILIE ("Familie"),
	SPIRITUALITAET_RELIGION ("Spiritualitaet/Religion"),
	HERKUNFT ("Herkunft"),
	SPIRITUAL_CARE_INTERVENTION ("Spiritual Care Intervention");
	
	private final String value;

	TabConcepts(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return this.value;
	}
	
}
