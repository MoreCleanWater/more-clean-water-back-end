package com.techreturners.mcw.service;

import java.util.Collection;
import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.techreturners.mcw.learning.Handler;

/* Class for learning the service layer */

public class UserServiceMapImpl implements UserService {

	private HashMap<String, UserTest> userMap;
	private static final Logger LOG = LogManager.getLogger(Handler.class);

	public UserServiceMapImpl() {
		userMap = new HashMap<>();
	}

	@Override
	public void addUser(UserTest user) {
		LOG.info("add user name : " + user.getFirstName());
		userMap.put(user.getId(), user);
		LOG.info("userMap size : " + userMap.size());
	}

	@Override
	public Collection<UserTest> getUsers() {
		return userMap.values();
	}

	@Override
	public UserTest getUser(String id) {
		return userMap.get(id);
	}

	@Override
	public UserTest editUser(UserTest forEdit) {
		try {
			if (forEdit.getId() == null)
				LOG.error("Userid is null");

			UserTest toEdit = userMap.get(forEdit.getId());
			if (toEdit == null)
				LOG.error("User not found");

			if (forEdit.getEmail() != null) {
				toEdit.setEmail(forEdit.getEmail());
			}
			if (forEdit.getFirstName() != null) {
				toEdit.setFirstName(forEdit.getFirstName());
			}
			if (forEdit.getLastName() != null) {
				toEdit.setLastName(forEdit.getLastName());
			}
			if (forEdit.getId() != null) {
				toEdit.setId(forEdit.getId());
			}
			return toEdit;

		} catch (Exception ex) {
			LOG.error("Userid is null");
		}
		return forEdit;
	}

	@Override
	public void deleteUser(String id) {
		userMap.remove(id);
	}

	@Override
	public boolean userExist(String id) {
		return userMap.containsKey(id);
	}
}
