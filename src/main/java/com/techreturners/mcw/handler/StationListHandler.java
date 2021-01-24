package com.techreturners.mcw.handler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techreturners.mcw.learning.Handler;
import com.techreturners.mcw.model.Station;
import com.techreturners.mcw.util.DatabaseUtil;

public class StationListHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	private static final Logger LOG = LogManager.getLogger(Handler.class);
	private Connection connection = null;
	private PreparedStatement statement = null;
	private ResultSet resultset = null;

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
		List<Station> stations = new ArrayList<>();
		try {
			connection = DatabaseUtil.getConnection();

			statement = connection.prepareStatement("Select * from water_station");
			resultset = statement.executeQuery();

			while (resultset.next()) {
				Station water_stat = new Station(resultset.getLong("station_id"), resultset.getInt("station_id"),
						resultset.getInt("size"), resultset.getString("capacity"),
						resultset.getDate("installation_date"), resultset.getBoolean("is_working"));
				stations.add(water_stat);

			}
		} catch (Exception e) {
			LOG.error("Unable to open database connection in Station List= ", e);
		} finally {
			closeConnection();
		}

		APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
		response.setStatusCode(200);
		ObjectMapper stationMapper = new ObjectMapper();

		try {
			String stationList = stationMapper.writeValueAsString(stations);
			response.setBody(stationList);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			LOG.info("error in getting Station List Json= ", e.getMessage());
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
			LOG.error("Unable to close database connection in Station List= ", ex.getMessage());
		}
	}

}
