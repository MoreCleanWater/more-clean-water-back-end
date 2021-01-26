package com.techreturners.mcw.handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techreturners.mcw.learning.Handler;
import com.techreturners.mcw.model.User;

public class UserInfoHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	private static final Logger LOG = LogManager.getLogger(Handler.class);
	private Connection connection = null;
	private PreparedStatement statement = null;
	private ResultSet resultset = null;

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
		String userid = request.getPathParameters().get("userId");
		List<User> users = new ArrayList<>();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager
					.getConnection(String.format("jdbc:mysql://%s/%s?user=%s&password=%s", System.getenv("DB_HOST"),
							System.getenv("DB_NAME"), System.getenv("DB_USER"), System.getenv("DB_PASSWORD")));
			statement = connection.prepareStatement("Select * from user where user_id=?");
			statement.setString(1, userid);
			resultset = statement.executeQuery();

			while (resultset.next()) {
				User user = new User(resultset.getLong("user_id"), resultset.getString("user_name"),
						resultset.getString("first_name"), resultset.getString("last_name"),
						resultset.getString("password"),resultset.getString("postcode"),
						resultset.getString("email"), resultset.getBoolean("is_active"), resultset.getBoolean("is_subscriber"));
				users.add(user);
			}
			LOG.info("end= ");
		} catch (Exception e) {
			LOG.error("Unable to open database connection in User detail", e);
			// LOG.error(String.format("unable to get query databse for users list %s",
			// userid), e);
		} finally {
			closeConnection();
		}

		APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
		response.setStatusCode(200);
		
		Map<String, String> headers = new HashMap<>();
        headers.put( "Access-Control-Allow-Origin", "*");
        headers.put( "Access-Control-Allow-Credentials", "true" );
		response.setHeaders(headers);

		ObjectMapper Objmapper = new ObjectMapper();
		try {
			String responseBody = Objmapper.writeValueAsString(users);
			response.setBody(responseBody);

		} catch (JsonProcessingException e) {
			e.printStackTrace();
			LOG.info("unable to get specific user details", e);
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
