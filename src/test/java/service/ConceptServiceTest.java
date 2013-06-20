package service;

import javax.inject.Inject;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.klinikum.service.implementation.ConceptServiceImpl;

@RunWith(Arquillian.class)
public class ConceptServiceTest {

    @Inject
    ConceptServiceImpl conceptService;

    @Ignore
    @Test
    public void test() {

    }

}
