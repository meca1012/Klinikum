package service;

import javax.inject.Inject;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.klinikum.service.Interfaces.ConceptService;

@RunWith(Arquillian.class)
public class ConceptServiceTest {

	@Inject
	ConceptService conceptService;
	
	@Test
	public void test(){
		
	}
	
}
