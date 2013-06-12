package de.klinikum.domain.enums;

public enum SpiritualityConcepts {

	KATHOLISCH("katholisch"),
	EVANGELISCH("evangelisch"),
	JUEDISCH("j�disch"),
	MUSLIMISCH("muslimisch");

	private final String value;

	SpiritualityConcepts(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return this.value;
	}
}
