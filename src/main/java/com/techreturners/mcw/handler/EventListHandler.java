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
import com.techreturners.mcw.model.Event;

public class EventListHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	private static final Logger LOG = LogManager.getLogger(Handler.class);
	private Connection connection = null;
	private PreparedStatement statement = null;
	private ResultSet resultset = null;

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
		List<Event> events = new ArrayList<>();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager
					.getConnection(String.format("jdbc:mysql://%s/%s?user=%s&password=%s", System.getenv("DB_HOST"),
							System.getenv("DB_NAME"), System.getenv("DB_USER"), System.getenv("DB_PASSWORD")));
			statement = connection.prepareStatement("Select * from event");
			resultset = statement.executeQuery();

			while (resultset.next()) {
				Event event = new Event(resultset.getInt("event_id"), resultset.getString("link"),
						resultset.getString("title"), resultset.getString("description"),
						resultset.getString("event_date"), resultset.getString("event_time"),
						resultset.getBoolean("is_cancelled"));
				events.add(event);

			}
		} catch (Exception e) {
			LOG.error("Unable to open database connection in Events List= ", e);
		} finally {
			closeConnection();
		}

		APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
		response.setStatusCode(200);

		Map<String, String> headers = new HashMap<>();
		headers.put("Access-Control-Allow-Origin", "*");
		headers.put("Access-Control-Allow-Credentials", "true");
		response.setHeaders(headers);

		ObjectMapper userMapper = new ObjectMapper();

		try {
			String eventlist = userMapper.writeValueAsString(events);
			response.setBody(eventlist);
		} catch (JsonProcessingException e) {
			LOG.info("error in getting events List Json= ", e.getMessage());
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
			LOG.error("Unable to close database connection in Event List= ", ex.getMessage());
		}
	}

}
