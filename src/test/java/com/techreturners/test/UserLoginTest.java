package com.techreturners.test;

import com.techreturners.mcw.model.User;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserLoginTest {

	@Test
	@DisplayName("Check valid username")
	public void testLoginUser() {
		User validUser = new User((long) 1, "devuser", "devpass", "saltvalue", true);
		
	    assertEquals("devuser", validUser.getUserName(), "Valid username is entered");
	}

}
