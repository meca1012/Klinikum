package connection;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;

public class RestTest {
	
	@Test
	public void testConnection () throws ClientProtocolException, IOException {
		
	
	DefaultHttpClient httpclient = new DefaultHttpClient();
	HttpGet httpget = new HttpGet("http://localhost:8080/KlinikumServer-0.0.1-SNAPSHOT/rest/server/restInfo");
	HttpResponse response = httpclient.execute(httpget);
	HttpEntity entity = response.getEntity();
	if (entity != null) {
	    InputStream instream = entity.getContent();
	    byte[] content = IOUtils.toByteArray(instream);
	    System.out.println(response.getStatusLine());
	    System.out.println(content.toString());
	    try {	    	
	    	System.out.println();  	        
	    } finally {
	        instream.close();
	    }
	}
	}
	}



	
