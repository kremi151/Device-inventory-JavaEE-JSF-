package lu.mkremer.javaeega.managers;

import java.util.List;

import javax.ejb.Local;

import lu.mkremer.javaeega.users.User;
import lu.mkremer.javaeega.users.UserGroup;

@Local
public interface UserManager {

	User createUser(String userId, String firstName, String lastName, String hashedPassword, UserGroup group);
	void removeUser(User user);
	UserGroup getDefaultGroup();
	UserGroup createGroup(String name);
	UserGroup getGroupById(long id);
	boolean doesUsernameExist(String username);
	User findUser(String username);
	List<User> listAllUsers();
	List<User> listMatchingUsers(String filter);
	void update(UserGroup group);
	List<UserGroup> getUserGroups();
	
}
