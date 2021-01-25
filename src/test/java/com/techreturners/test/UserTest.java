package com.techreturners.test;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Ignore;
import org.junit.Test;
public class UserTest {

	/*Needs to be implemented with more tests*/
	
	@Ignore	
	@Test
	public void givenUserDoesNotExists_whenUserInfoIsRetrieved_then404IsReceived()
	  throws ClientProtocolException, IOException {
	
	    // Given
	    String id = "1";
	    HttpUriRequest  request = new HttpGet( "https://w0u1g2x3tc.execute-api.eu-west-2.amazonaws.com/dev/users/" + id );
	    // When
	    HttpResponse httpResponse  = HttpClientBuilder.create().build().execute( request );
        //Check
	    int statusCode = httpResponse.getStatusLine().getStatusCode();
	    assertEquals(statusCode, HttpStatus.SC_NOT_FOUND);
	}
	@Ignore
	@Test
	public void checkSignUp() {
		/*
		User user = new User("WattFirst","WattLast","watt.wolff@gmail.com",mySecurePassword,salt,1,false,false);
		assertTrue(repository.save(user));
		*/
	}
}
