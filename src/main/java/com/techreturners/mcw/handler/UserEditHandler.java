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

public class UserEditHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	private static final Logger LOG = LogManager.getLogger(Handler.class);
	private Connection connection = null;
	private PreparedStatement statement = null;
	private ResultSet resultset = null;
	APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
		String userbody = request.getBody();
		ObjectMapper userObj = new ObjectMapper();
		String userid = request.getPathParameters().get("userId");

		try {
			User user = userObj.readValue(userbody, User.class);
			
			String salt = PasswordUtils.getSalt(30);
			String securePassword = PasswordUtils.generateSecurePassword(user.getPassword(), salt);
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager
					.getConnection(String.format("jdbc:mysql://%s/%s?user=%s&password=%s", System.getenv("DB_HOST"),
							System.getenv("DB_NAME"), System.getenv("DB_USER"), System.getenv("DB_PASSWORD")));
			String query = "update user set first_name = ?, last_name = ?,county_id= ?,postcode= ?,password = ? , salt_value = ?,email = ? where user_id = ?";
			statement = connection.prepareStatement(query);

			statement.setString(1, user.getFirstName());
			statement.setString(2, user.getLastName());
			statement.setInt(3, user.getCountyId());
			statement.setString(4, user.getPostcode());
			statement.setString(5, securePassword);
			statement.setString(6, salt);
			statement.setString(7, user.getEmail());
			statement.setString(8, userid);
			statement.executeUpdate();
			response.setStatusCode(200);
			
			Map<String, String> headers = new HashMap<>();
	        headers.put( "Access-Control-Allow-Origin", "*");
	        headers.put( "Access-Control-Allow-Credentials", "true" );
			response.setHeaders(headers);
			

			LOG.debug("updating user = " + user.getFirstName());
			LOG.info("user info=", user);

			response.setBody("User is updated successfully");

		} catch (Exception e) {
			LOG.error("Unable to open database connection in Update User", e);
		} finally {
			closeConnection();
		}
		return response;
	}

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
			LOG.error("Unable to close database connection in User detail", ex.getMessage());
		}
	}
}
