package com.techreturners.mcw.handler;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techreturners.mcw.model.User;

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

			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager
					.getConnection(String.format("jdbc:mysql://%s/%s?user=%s&password=%s", System.getenv("DB_HOST"),
							System.getenv("DB_NAME"), System.getenv("DB_USER"), System.getenv("DB_PASSWORD")));
			String query = " insert into user ( postcode_id, first_name, last_name, password,salt_value,email,is_admin,is_remove)"
					+ " values ( ?, ?, ?, ?,?,?,?,?)";
			statement = connection.prepareStatement(query);
			statement.setInt(1, user.getPostcode_id());
			statement.setString(2, user.getFirst_name());
			statement.setString(3, user.getLast_name());
			statement.setString(4, user.getPassword());
			statement.setString(5, user.getSalt_value());
			statement.setString(6, user.getEmail());
			statement.setBoolean(7, false);
			statement.setBoolean(8, false);
			statement.executeUpdate();
			response.setStatusCode(200);

			LOG.debug("saving user = " + user.getFirst_name());
			LOG.info("user info=", user);

			response.setBody("User is created successfully");

		} catch (Exception e) {
			LOG.error("Unable to open database connection in Create User", e);

			// LOG.error(String.format("unable to get query databse for users list %s",
			// userid), e);
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
