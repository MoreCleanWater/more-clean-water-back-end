package com.techreturners.mcw.learning;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.techreturners.mcw.ApiGatewayResponse;
import com.techreturners.mcw.model.Task;
import com.techreturners.mcw.service.UserService;
import com.techreturners.mcw.service.UserServiceMapImpl;
import com.techreturners.mcw.service.UserTest;

/* Test Class for learning the architecture */

public class Handler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private static final Logger LOG = LogManager.getLogger(Handler.class);
	final UserService userService = new UserServiceMapImpl();
	Gson gson = new GsonBuilder().setPrettyPrinting().create();

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

		Task t1 = new Task("abc123", "first task", false);
		Task t2 = new Task("abc456", "Enjoy java", false);
		List<Task> tasks = new ArrayList<>();
		tasks.add(t1);
		tasks.add(t2);
		JSONObject object = new JSONObject(input);

		String path = object.get("path").toString();

		if (path.equals("/tasks/getall")) {
			LOG.info("EVENT path is getall : " + path);
			LOG.info("size : " + userService.getUsers().size());
			/*
			 * Iterator<UserTest> iterator = userService.getUsers().iterator(); while
			 * (iterator.hasNext()) { LOG.info("value= " + iterator.next().getFirstName());
			 * }
			 */
			return ApiGatewayResponse.builder().setStatusCode(200).setObjectBody(userService.getUsers())
					.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless")).build();
		}
		if (path.equals("/tasks/create")) {
			LOG.info("EVENT path is crate : " + path);
			String body = object.get("body").toString();
			UserTest user = new Gson().fromJson(body, UserTest.class);
			userService.addUser(user);
			return ApiGatewayResponse.builder().setStatusCode(200).setObjectBody(user)
					.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless")).build();
		} else {
			String parameter = object.get("pathParameters").toString();
			parameter = parameter.replaceAll("\\p{P}", "");

			String[] parametersStr = parameter.split("[=]", 0);

			LOG.info("parameter name: " + parametersStr[0]);
			LOG.info("parameter value: " + parametersStr[1]);

			return ApiGatewayResponse.builder().setStatusCode(200).setObjectBody(tasks)
					.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless")).build();
		}
	}

}
