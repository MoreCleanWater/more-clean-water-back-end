package com.techreturners.mcw.handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.techreturners.mcw.learning.Handler;
import com.techreturners.mcw.model.User;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.Message;

public class ChangePasswordHandler
		implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	private static final Logger LOG = LogManager.getLogger(Handler.class);
	private Connection connection = null;
	private PreparedStatement statement = null;
	private ResultSet resultset = null;
	APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
		String userid = request.getPathParameters().get("userId");
		List<User> users = new ArrayList<>();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager
					.getConnection(String.format("jdbc:mysql://%s/%s?user=%s&password=%s", System.getenv("DB_HOST"),
							System.getenv("DB_NAME"), System.getenv("DB_USER"), System.getenv("DB_PASSWORD")));
			statement = connection.prepareStatement("SELECT * FROM user where user_id=?");
			statement.setString(1, userid);
			resultset = statement.executeQuery();

			while (resultset.next()) {
				User user = new User(resultset.getLong("user_id"), resultset.getString("first_name"),
						resultset.getString("last_name"), resultset.getString("email"));
				users.add(user);
			}
			LOG.info("Getting user details= " + userid);
			LOG.info("Getting user email= " + users.get(0).getEmail());

			response.setStatusCode(200);

			Map<String, String> headers = new HashMap<>();
			headers.put("Access-Control-Allow-Origin", "*");
			headers.put("Access-Control-Allow-Credentials", "true");
			response.setHeaders(headers);

			response.setBody("Password change request has sent successfully");
			requestPassword(users.get(0).getEmail(), userid);
		} catch (Exception e) {
			LOG.error("Unable to open database connection in User detail", e);
		} finally {
			closeConnection();
		}
		return response;
	}

	private void requestPassword(String TO, String userid) {

		Calendar cal = Calendar.getInstance();
		String token = UUID.randomUUID().toString().toUpperCase() + "|" + userid + "|" + cal.getTimeInMillis();
		String FROM = "techtret.team@gmail.com";
		String SUBJECT = "Change the password for MoreCleanWater";
		String HTMLBODY = "<h1>Hello User,You have requested to change the password. Please click the follwing link to change it</h1>"
				+ " Please Click https://morecleanwater.github.io/" + token+" Thanks,MoreCleanWater Team";

		String TEXTBODY = "This email was sent through Amazon SES " + "using the AWS SDK for Java.";

		try {
			AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard()

					.withRegion(Regions.EU_WEST_2).build();
			SendEmailRequest request = new SendEmailRequest().withDestination(new Destination().withToAddresses(TO))
					.withMessage(new Message()
							.withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(HTMLBODY))
									.withText(new Content().withCharset("UTF-8").withData(TEXTBODY)))
							.withSubject(new Content().withCharset("UTF-8").withData(SUBJECT)))
					.withSource(FROM);
			client.sendEmail(request);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

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
