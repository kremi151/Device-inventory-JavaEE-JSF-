package lu.mkremer.javaeega.managers;

import javax.ejb.Local;

import lu.mkremer.javaeega.events.UserGroupModifiedListener;
import lu.mkremer.javaeega.events.UserModifiedListener;
import lu.mkremer.javaeega.users.User;
import lu.mkremer.javaeega.users.UserGroup;

@Local
public interface EventManager {

	long addOnUserModifiedListener(UserModifiedListener listener);
	boolean removeOnUserModifiedListener(long id);
	void dispatchUserModified(User user);
	
	long addOnUserGroupModifiedListener(UserGroupModifiedListener listener);
	boolean removeOnUserGroupModifiedListener(long id);
	void dispatchUserGroupModified(UserGroup group);
	
}
