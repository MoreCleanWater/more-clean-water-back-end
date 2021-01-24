package com.techreturners.mcw;
import java.util.Collection;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.techreturners.mcw.handler.Handler;
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
	    public UserTest editUser(UserTest forEdit) throws UserException {
	        try {
	            if (forEdit.getId() == null)
	                throw new UserException("ID cannot be blank");

	            UserTest toEdit = userMap.get(forEdit.getId());
	            if (toEdit == null)
	                throw new UserException("User not found");

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
	            throw new UserException(ex.getMessage());
	        }
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
