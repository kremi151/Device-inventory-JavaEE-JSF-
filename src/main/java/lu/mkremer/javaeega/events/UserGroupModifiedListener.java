package lu.mkremer.javaeega.events;

import lu.mkremer.javaeega.users.UserGroup;

public interface UserGroupModifiedListener {
	
	void onUserGroupModified(UserGroup group);
	
}
