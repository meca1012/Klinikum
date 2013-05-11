package de.klinikum.service;

import java.util.List;

import de.klinikum.domain.Patient;

public interface PatientService {

	public abstract Patient createPatient(Patient patient);

	public abstract List<Patient> searchPatient(Patient patient);

}