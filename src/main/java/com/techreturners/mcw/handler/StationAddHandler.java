package com.techreturners.mcw.handler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techreturners.mcw.learning.Handler;
import com.techreturners.mcw.model.Station;
import com.techreturners.mcw.util.DatabaseUtil;

public class StationAddHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	private static final Logger LOG = LogManager.getLogger(Handler.class);
	private Connection connection = null;
	private PreparedStatement statement = null;
	private ResultSet resultset = null;
	APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
		String stationbody = request.getBody();
		ObjectMapper stationObj = new ObjectMapper();
		try {
			Station station = stationObj.readValue(stationbody, Station.class);
			connection = DatabaseUtil.getConnection();
			String query = " insert into water_station ( postcode_id, size, capacity, installation_date,is_working)"
					+ " values ( ?, ?, ?, ?,?)";
			statement = connection.prepareStatement(query);
			statement.setInt(1, station.getPostcode_id());
			statement.setInt(2, station.getSize());
			statement.setString(3, station.getCapacity());
			statement.setDate(4, java.sql.Date.valueOf(java.time.LocalDate.now()));
			statement.setBoolean(5, true);
			statement.executeUpdate();
			response.setStatusCode(200);
			LOG.debug("saving station = ");
			LOG.info("station info=", station);

			response.setBody("Stations is created successfully");

		} catch (Exception e) {
			LOG.error("Unable to open database connection in Create Station", e);
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
			LOG.error("Unable to close database connection in Create Station", ex.getMessage());
		}
	}
}
