package rest;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Ignore;
import org.junit.Test;

/**
 * 
 * @author Carsten Meiser, Matthias Schwarzenbach
 * 
 *         This test was implemented to find mistakes in XML resources which are incoming via rest.
 *         The tests are @Ignored, because you can't run them while building because the server
 *         doesn't have the resources yet.
 *         
 * 
 */
public class RestTest {

    @Test
    @Ignore
    public void testRestInfo() throws ClientProtocolException, IOException {

        System.out.println("testRestInfo");
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet("http://localhost:8080/KlinikumServer-0.0.1-SNAPSHOT/rest/server/restInfo");
        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            InputStream instream = entity.getContent();
            byte[] content = IOUtils.toByteArray(instream);
            System.out.println(response.getStatusLine());
            System.out.println(new String(content));
            try {
                System.out.println();
            }
            finally {
                instream.close();
            }
        }
    }

    @Test
    @Ignore
    public void testCreatePatient() throws ClientProtocolException, IOException {

        System.out.println("testCreatePatient");
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(
                "http://localhost:8080/KlinikumServer-0.0.1-SNAPSHOT/rest/patient/createPatient");

        String patient = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><patient><patientNumber>123456</patientNumber><firstName>Max</firstName><lastName>Power</lastName><dateOfBirth>08-01-1985</dateOfBirth><address><city>Karlsruhe</city><country>Deutschland</country><phone>081511833</phone><street>Hauptstrasse</street><streetNumber>12</streetNumber><uri>de.spironto/address/432432</uri><zip>432433</zip></address></patient>";
        httpPost.setHeader("content-type", "application/xml");
        httpPost.setEntity(new StringEntity(patient));

        HttpResponse response = httpclient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            InputStream instream = entity.getContent();
            byte[] content = IOUtils.toByteArray(instream);
            System.out.println(response.getStatusLine());
            System.out.println(new String(content));
            try {
                System.out.println();
            }
            finally {
                instream.close();
            }
        }
    }

    @Test
    @Ignore
    public void testAddConceptToPatient() throws ClientProtocolException, IOException {

        System.out.println("testAddConceptToPatient");
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(
                "http://localhost:8080/KlinikumServer-0.0.1-SNAPSHOT/rest/concept/addConceptToPatient");

        String concept = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><patientDTO><patient><uri>http://spironto.de/spironto#patient-gen2</uri></patient><concept><label>Spiritualitaet</label></concept><tabConcept>true</tabConcept></patientDTO>";
        httpPost.setHeader("content-type", "application/xml");
        httpPost.setEntity(new StringEntity(concept));

        HttpResponse response = httpclient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            InputStream instream = entity.getContent();
            byte[] content = IOUtils.toByteArray(instream);
            System.out.println(response.getStatusLine());
            System.out.println(new String(content));
            try {
                System.out.println();
            }
            finally {
                instream.close();
            }
        }
    }

    @Test
    @Ignore
    public void testGetTabConcepts() throws ClientProtocolException, IOException {

        System.out.println("testGetTabConcepts");
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(
                "http://localhost:8080/KlinikumServer-0.0.1-SNAPSHOT/rest/concept/getTabConcepts");

        String concept = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><patient><uri>http://spironto.de/spironto#patient-gen2</uri></patient>";
        httpPost.setHeader("content-type", "application/xml");
        httpPost.setEntity(new StringEntity(concept));

        HttpResponse response = httpclient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            InputStream instream = entity.getContent();
            byte[] content = IOUtils.toByteArray(instream);
            System.out.println(response.getStatusLine());
            System.out.println(new String(content));
            try {
                System.out.println();
            }
            finally {
                instream.close();
            }
        }
    }

}
