package com.techreturners.mcw.util;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.techreturners.mcw.learning.Handler;
import com.techreturners.mcw.model.Event;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EmailUtil {
	// The configuration set to use for this email.
	private static final String FROM = "techtret.team@gmail.com";
	private static final String TO = "guljana@gmail.com";
	private static final String SUBJECT = "MoreCleanWater has inivited for webinar";
	private static final Logger LOG = LogManager.getLogger(Handler.class);

	public static void sendEmail() {

		String HTMLBODY = "<h1>Amazon SES test (AWS SDK for Java)</h1>"
				+ "<p>This email was sent with <a href='https://aws.amazon.com/ses/'>"
				+ "Amazon SES</a> using the <a href='https://aws.amazon.com/sdk-for-java/'>" + "AWS SDK for Java</a>";
		String TEXTBODY = "This email was sent through Amazon SES " + "using the AWS SDK for Java= ";

		try {
			AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard()
					// London region
					.withRegion(Regions.EU_WEST_2).build();
			SendEmailRequest request = new SendEmailRequest().withDestination(new Destination().withToAddresses(TO))
					.withMessage(new Message()
							.withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(HTMLBODY))
									.withText(new Content().withCharset("UTF-8").withData(TEXTBODY)))
							.withSubject(new Content().withCharset("UTF-8").withData(SUBJECT)))
					.withSource(FROM);

			client.sendEmail(request);
			LOG.debug(" success in sending emails ");
		} catch (Exception ex) {
			ex.printStackTrace();
			LOG.error("Error in sending emails");

		}
	}
}