package com.techreturners.mcw;
import java.util.Collection;

public interface UserService {
	   public void addUser(UserTest user);

	    public Collection<UserTest> getUsers();

	    public UserTest getUser(String id);

	    public UserTest editUser(UserTest user) throws UserException;

	    public void deleteUser(String id);

	    public boolean userExist(String id);
}
