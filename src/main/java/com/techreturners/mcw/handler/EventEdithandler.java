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
import com.techreturners.mcw.model.Event;

public class EventEdithandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	private static final Logger LOG = LogManager.getLogger(Handler.class);
	private Connection connection = null;
	private PreparedStatement statement = null;
	private ResultSet resultset = null;
	APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
		String eventbody = request.getBody();
		ObjectMapper eventObj = new ObjectMapper();
		String event_id = request.getPathParameters().get("eventId");

		try {
			Event event = eventObj.readValue(eventbody, Event.class);
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager
					.getConnection(String.format("jdbc:mysql://%s/%s?user=%s&password=%s", System.getenv("DB_HOST"),
							System.getenv("DB_NAME"), System.getenv("DB_USER"), System.getenv("DB_PASSWORD")));
			String query = "update event set link = ?, title = ?, description = ? , event_date = ?, event_time = ? where event_id = ?";

			statement = connection.prepareStatement(query);
			statement.setString(1, event.getLink());
			statement.setString(2, event.getTitle());
			statement.setString(3, event.getDescription());
			statement.setString(4, event.getEventDate());
			statement.setString(5, event.getEventTime());
			statement.setString(6, event_id);
			statement.executeUpdate();
			response.setStatusCode(200);
			Map<String, String> headers = new HashMap<>();
			headers.put("Access-Control-Allow-Origin", "*");
			headers.put("Access-Control-Allow-Credentials", "true");
			response.setHeaders(headers);
			LOG.debug("updating event  "+event.getTitle());
			LOG.info("event info=");

			response.setBody("Event is postponed successfully");

		} catch (Exception e) {
			LOG.error("Unable to open database connection in Update Event", e);
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
			LOG.error("Unable to close database connection in Event update", ex.getMessage());
		}
	}
}
