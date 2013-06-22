package service;

import javax.inject.Inject;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.klinikum.service.interfaces.NoteService;

@RunWith(Arquillian.class)
public class NoteServiceTest {
	
	@Inject 
	NoteService noteService;

	@Test
	@Ignore
	private void createNoteTest() {
	};

	@Test
	@Ignore
	private void addConceptToNote() {
	};

	@Test
	@Ignore
	private void getNoteByUriTest() {
	};

	@Test
	@Ignore
	private void addConceptToNoteTest() {
	};

	@Test
	@Ignore
	private void getConceptsToNoteTest() {
	};

	@Test
	@Ignore
	private void updateNoteTest(){};
	
}
