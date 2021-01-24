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
import com.techreturners.mcw.learning.Handler;
import com.techreturners.mcw.util.DatabaseUtil;

public class StationDisableHandler
		implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	private static final Logger LOG = LogManager.getLogger(Handler.class);
	private Connection connection = null;
	private PreparedStatement statement = null;
	private ResultSet resultset = null;
	APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
		String station_id = request.getPathParameters().get("station_id");

		try {
			connection = DatabaseUtil.getConnection();
			String query = "update water_station set is_working= ? where station_id = ?";
			statement = connection.prepareStatement(query);
			statement.setBoolean(1, false);
			statement.setString(2, station_id);
			statement.executeUpdate();
			response.setStatusCode(200);

			LOG.debug("deactivating station = " + station_id);
			LOG.info("station info=", station_id);

			response.setBody("Station is deactivated successfully");
		} catch (Exception e) {
			LOG.error("Unable to open database connection in Deactivate Station", e);
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
			LOG.error("Unable to close database connection in Station deactivate", ex.getMessage());
		}
	}
}
