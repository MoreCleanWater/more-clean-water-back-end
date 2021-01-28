package com.techreturners.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import java.io.IOException;
import java.util.HashMap;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import com.techreturners.mcw.handler.UserAddHandler;
import com.techreturners.mcw.handler.UserLoginHandler;
/*Needs to be implemented with more tests*/

public class UserTest {

	private static Object input;

	@BeforeClass
	public static void createInput() throws IOException {
		// TODO: set up your sample input object here.
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("userName", "testeruser");
		hashMap.put("firstName", "testfirstname");
		hashMap.put("lastName", "testlastname");
		hashMap.put("postcode", "CB1 0BP");
		hashMap.put("county", "21");
		hashMap.put("email", "test@gmail.com");
		input = hashMap;
	}

	@Ignore
	@Test
	public void testhandleRequest() {
		UserLoginHandler handler = new UserLoginHandler();
		Context ctx = createContext();

		APIGatewayProxyResponseEvent output = handler.handleRequest((APIGatewayProxyRequestEvent) input, ctx);

		// TODO: validate output here if needed.
		if (output.getStatusCode().equals(200)) {
			System.out.println("AuthenticateUser JUnit Test Passed");
		} else {
			assertFalse("AuthenticateUser JUnit Test Failed", false);
		}
	}

	@Ignore
	@Test
	public void testLambdaFunctionHandler() {
		UserAddHandler handler = new UserAddHandler();
		Context ctx = createContext();
		APIGatewayProxyResponseEvent output = handler.handleRequest((APIGatewayProxyRequestEvent) input, ctx);
		if (output != null) {
			System.out.println(output.toString());
		}
		assertEquals("testingfunction", output);
	}

	@Ignore
	@Test
	public void givenUserDoesNotExists_whenUserInfoIsRetrieved_then404IsReceived()
			throws ClientProtocolException, IOException {

		// Given
		String id = "1";
		HttpUriRequest request = new HttpGet("https://w0u1g2x3tc.execute-api.eu-west-2.amazonaws.com/dev/users/" + id);
		// When
		HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
		// Check
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		assertEquals(statusCode, HttpStatus.SC_NOT_FOUND);
	}

	@Ignore
	@Test
	public void checkSignUp() {
		/*
		 * User user = new
		 * User("WattFirst","WattLast","watt.wolff@gmail.com",mySecurePassword,salt,1,
		 * false,false); assertTrue(repository.save(user));
		 */
	}

	private Context createContext() {
		TestContext ctx = new TestContext();

		// TODO: customize your context here if needed.
		ctx.setFunctionName("LambdaForm");

		return ctx;
	}
}
