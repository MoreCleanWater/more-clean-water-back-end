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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techreturners.mcw.learning.Handler;
import com.techreturners.mcw.model.User;
import com.techreturners.mcw.util.PasswordUtils;

public class UserLoginHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	private static final Logger LOG = LogManager.getLogger(Handler.class);
	private Connection connection = null;
	private PreparedStatement statement = null;
	private ResultSet resultset = null;
	APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
		String userbody = request.getBody();
		ObjectMapper userObj = new ObjectMapper();
		List<User> users = new ArrayList<>();
		User validUser = new User();
		Boolean isValid = false;
		try {
			User user = userObj.readValue(userbody, User.class);

			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager
					.getConnection(String.format("jdbc:mysql://%s/%s?user=%s&password=%s", System.getenv("DB_HOST"),
							System.getenv("DB_NAME"), System.getenv("DB_USER"), System.getenv("DB_PASSWORD")));
			statement = connection.prepareStatement("Select * from user where user_name=?");
			statement.setString(1, user.getUserName());
			resultset = statement.executeQuery();
			if (!resultset.next()) {
			} else {
				validUser = new User(resultset.getLong("user_id"), resultset.getString("user_name"),
						resultset.getString("password"), resultset.getString("salt_value"),
						resultset.getBoolean("is_active"));
				users.add(validUser);
				isValid = PasswordUtils.verifyUserPassword(user.getPassword(), validUser.getPassword(),
						validUser.getSaltValue());
			}
			response.setStatusCode(200);

			Map<String, String> headers = new HashMap<>();
			headers.put("Access-Control-Allow-Origin", "*");
			headers.put("Access-Control-Allow-Credentials", "true");
			response.setHeaders(headers);
			if (isValid) {
				response.setBody(validUser.getUserId().toString());
			} else {
				response.setBody("Invalid email or password");
			}
			LOG.debug("authenticating Login user = " + user.getUserName());

		} catch (Exception e) {
			LOG.error("Unable to open database connection in login User", e);
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
			LOG.error("Unable to close database connection in User login", ex.getMessage());
		}
	}
}
