package com.techreturners.mcw.handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techreturners.mcw.learning.Handler;
import com.techreturners.mcw.model.User;
import com.techreturners.mcw.util.PasswordUtils;

/*
 * User Sign Up Class
 */
public class UserAddHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	private static final Logger LOG = LogManager.getLogger(Handler.class);
	private Connection connection = null;
	private PreparedStatement statement = null;
	private ResultSet resultset = null;
	APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
		String userbody = request.getBody();
		ObjectMapper userObj = new ObjectMapper();
		try {
			User user = userObj.readValue(userbody, User.class);
			String salt = PasswordUtils.getSalt(30);
			String securePassword = PasswordUtils.generateSecurePassword(user.getPassword(), salt);
	
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager
					.getConnection(String.format("jdbc:mysql://%s/%s?user=%s&password=%s", System.getenv("DB_HOST"),
							System.getenv("DB_NAME"), System.getenv("DB_USER"), System.getenv("DB_PASSWORD")));
			String query = " insert into user (user_name, postcode,county_id, first_name, last_name, password,salt_value,email,is_subscriber)"
					+ " values ( ?, ?, ?, ?,?,?,?,?,?)";
			statement = connection.prepareStatement(query);
			statement.setString(1, user.getUserName());
			statement.setString(2, user.getPostcode());
			statement.setInt(3, user.getCountyId());
			statement.setString(4, user.getFirstName());
			statement.setString(5, user.getLastName());
			statement.setString(6, securePassword);
			statement.setString(7, salt);
			statement.setString(8, user.getEmail());
			statement.setBoolean(9, user.getIsSubscriber());
			statement.executeUpdate();
			response.setStatusCode(200);
			
			Map<String, String> headers = new HashMap<>();
	        headers.put( "Access-Control-Allow-Origin", "*");
	        headers.put( "Access-Control-Allow-Credentials", "true" );
			response.setHeaders(headers);
			
			LOG.debug("saving user = " + user.getFirstName());
			LOG.info("user info=", user);

			response.setBody("User is created successfully");

		} catch (Exception e) {
			LOG.error("Unable to open database connection in Create User", e);
			// LOG.error(String.format("unable to get query databse for users list
			// %s",userid), e);
		} finally {
			closeConnection();
		}
		return response;
	}

	/*
	 * Function for closing database connections
	 * 
	 */
	private void closeConnection() {
		try {
			if (resultset != null) {
				resultset.close();
			}
			if (statement != null) {
				statement.close();
			}
			if (connection != null) {
				connection.close();
			}
		} catch (Exception ex) {
			LOG.error("Unable to close database connection in Create User", ex.getMessage());
		}
	}
}
