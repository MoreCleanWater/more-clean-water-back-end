package com.techreturners.mcw.service;
import java.util.Collection;
/* Interface for service classes...For learning purpose */

public interface UserService {
	   public void addUser(UserTest user);

	    public Collection<UserTest> getUsers();

	    public UserTest getUser(String id);

	    public UserTest editUser(UserTest user);

	    public void deleteUser(String id);

	    public boolean userExist(String id);
}
