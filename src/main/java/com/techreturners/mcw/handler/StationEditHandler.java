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

public class StationEditHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	private static final Logger LOG = LogManager.getLogger(Handler.class);
	private Connection connection = null;
	private PreparedStatement statement = null;
	private ResultSet resultset = null;
	APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
		String stationbody = request.getBody();
		ObjectMapper stationObj = new ObjectMapper();
		String station_id = request.getPathParameters().get("station_id");

		try {
			Station station = stationObj.readValue(stationbody, Station.class);
			connection = DatabaseUtil.getConnection();
			String query = "update water_station set size = ?, capacity = ?, installation_date = ? where station_id = ?";
			
			statement = connection.prepareStatement(query);
			statement.setInt(1, station.getSize());
			statement.setString(2, station.getCapacity());
			statement.setDate(3, java.sql.Date.valueOf(java.time.LocalDate.now()));
			statement.setString(4, station_id);
			statement.executeUpdate();
			response.setStatusCode(200);

			LOG.debug("updating station  ");
			LOG.info("station info=");

			response.setBody("Station is updated successfully");

		} catch (Exception e) {
			LOG.error("Unable to open database connection in Update Station", e);
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
			LOG.error("Unable to close database connection in Station detail", ex.getMessage());
		}
	}
}
