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
import com.techreturners.mcw.model.Event;
import com.techreturners.mcw.model.User;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.Message;

public class EventAddHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	private static final Logger LOG = LogManager.getLogger(Handler.class);
	private Connection connection = null;
	private PreparedStatement statement = null;
	private ResultSet resultset = null;
	APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
		String eventbody = request.getBody();
		List<User> users = new ArrayList<>();
		ObjectMapper eventObj = new ObjectMapper();
		try {
			Event event = eventObj.readValue(eventbody, Event.class);

			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager
					.getConnection(String.format("jdbc:mysql://%s/%s?user=%s&password=%s", System.getenv("DB_HOST"),
							System.getenv("DB_NAME"), System.getenv("DB_USER"), System.getenv("DB_PASSWORD")));
			String query = " insert into event (link, title,description, event_date, event_time, is_cancelled)"
					+ " values ( ?, ?, ?, ?,?,?)";
			statement = connection.prepareStatement(query);
			statement.setString(1, event.getLink());
			statement.setString(2, event.getTitle());
			statement.setString(3, event.getDescription());
			statement.setString(4, event.getEventDate());
			statement.setString(5, event.getEventTime());
			statement.setBoolean(6, event.getIsCancelled());
			statement.executeUpdate();
			response.setStatusCode(200);

			Map<String, String> headers = new HashMap<>();
			headers.put("Access-Control-Allow-Origin", "*");
			headers.put("Access-Control-Allow-Credentials", "true");
			response.setHeaders(headers);

			LOG.debug("saving event = " + event.getTitle());
			LOG.info("event info");

			response.setBody("Event is created successfully");

			statement = connection.prepareStatement("Select * from user where is_subscriber = 1 ");
			resultset = statement.executeQuery();

			while (resultset.next()) {
				User user = new User(resultset.getString("first_name"), resultset.getString("last_name"),
						resultset.getString("email"), resultset.getBoolean("is_subscriber"));
				users.add(user);
			}
			sendEventNotification(users, event);

		} catch (Exception e) {
			LOG.error("Unable to open database connection in Event User", e);
		} finally {
			closeConnection();
		}
		return response;
	}

	private void sendEventNotification(List<User> users, Event event) {
		List<String> recipients = new ArrayList<String>();
		users.forEach(user -> {
			try {
				Boolean is_subscriber = user.getIsSubscriber();
				if (is_subscriber) {
					recipients.add(user.getEmail());
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				LOG.error("Error in getting user email addresses", ex.getMessage());
			}
		});
		LOG.debug("email - " + recipients.size());
		sendEmail(recipients, event);
	}

	private void sendEmail(List<String> recipients, Event event) {

		String FROM = "techtret.team@gmail.com";
		String SUBJECT = "MoreCleanWater invited for webinar";
		String HTMLBODY = "<h1>" + event.getTitle() + "</h1>" + "<p> A webinar on this topic is going to held on "
				+ event.getEventDate() + " at " + event.getEventTime() + " <br> The Zoom's invitation is: "
				+ event.getLink() + "  <br> Please join us to listen about " + event.getDescription();
		LOG.debug(" HTMLBODY " + HTMLBODY);

		String TEXTBODY = "This email was sent through Amazon SES " + "using the AWS SDK for Java.";

		try {
			AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard()
					.withRegion(Regions.EU_WEST_2).build();
			SendEmailRequest request = new SendEmailRequest()
					.withDestination(new Destination().withToAddresses(recipients))
					.withMessage(new Message()
							.withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(HTMLBODY))
									.withText(new Content().withCharset("UTF-8").withData(TEXTBODY)))
							.withSubject(new Content().withCharset("UTF-8").withData(SUBJECT)))
					.withSource(FROM);
			LOG.debug(" before send email");

			client.sendEmail(request);
			LOG.debug(" sending emails last");
		} catch (Exception ex) {
			ex.printStackTrace();
			LOG.error("Error in sending emails");
		}
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
			LOG.error("Unable to close database connection in Create Event", ex.getMessage());
		}
	}
}
