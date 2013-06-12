package de.klinikum.domain.enums;

public enum BackgroundConcepts {

	ZUHAUSE("zu Hause"),
	MIGRATIONSHINTERGRUND("Migrationshintergrund");
	
	private final String value;

	BackgroundConcepts(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return this.value;
	}
	
}
