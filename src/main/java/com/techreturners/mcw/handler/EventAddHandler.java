package com.techreturners.mcw.handler;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

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
			
			statement = connection.prepareStatement("Select * from user");
			resultset = statement.executeQuery();

			while (resultset.next()) {
				User user = new User(resultset.getLong("user_id"),resultset.getString("first_name"), resultset.getString("last_name"),
						 resultset.getString("email"), resultset.getBoolean("is_subscriber"));
				users.add(user);
			}
        	sendEmail();

			//sendEventNotification( users);
               
		} catch (Exception e) {
			LOG.error("Unable to open database connection in Event User", e);
		} finally {
			closeConnection();
		}
		return response;
	}

	private void sendEventNotification(List<User> users) {
		LOG.info("sendEventNotification=");

		if(users.size()>0) {
			LOG.info("users.size()>0");
			users.forEach(user -> {
		            try {
		                Boolean is_subscriber = user.getIsSubscriber();
		                if(is_subscriber) {
		                	//sendEmail(user);
		                }
		            } catch (Exception ex) {
		    			LOG.error("Error in sending emails", ex.getMessage());
		            }
		        });
			
		}
	}
	
	private void sendEmail() {
		LOG.info("sendEmail=");
//guljana@gmail.com
	
		 // Recipient's email ID needs to be mentioned.
	      String to = "guljana@gmail.com";

	      // Sender's email ID needs to be mentioned
	      String from = "guljana@gmail.com";

	      // Assuming you are sending email from localhost
	      String host = "localhost";

	      // Get system properties
	      Properties properties = System.getProperties();

	      // Setup mail server
	      properties.setProperty("mail.smtp.host", host);

	      // Get the default Session object.
	      Session session = Session.getDefaultInstance(properties);


        try {
        	 // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("This is the Subject Line!");

            // Now set the actual message
            message.setText("This is actual message");

            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
			LOG.error("Error in sending emails= ", mex.toString());
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
