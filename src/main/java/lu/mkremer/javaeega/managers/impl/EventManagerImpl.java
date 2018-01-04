package lu.mkremer.javaeega.managers.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.ejb.Singleton;

import lu.mkremer.javaeega.events.UserGroupModifiedListener;
import lu.mkremer.javaeega.events.UserModifiedListener;
import lu.mkremer.javaeega.managers.EventManager;
import lu.mkremer.javaeega.users.User;
import lu.mkremer.javaeega.users.UserGroup;

@Singleton
public class EventManagerImpl implements EventManager{

	private final HashMap<Long, UserModifiedListener> userModlisteners = new HashMap<>();
	private final HashMap<Long, UserGroupModifiedListener> groupModlisteners = new HashMap<>();
	private final Random rand = new Random(System.currentTimeMillis());

	@Override
	public synchronized long addOnUserModifiedListener(UserModifiedListener listener) {
		while(true) {
			Long randomId = rand.nextLong();
			if(!userModlisteners.containsKey(randomId)) {
				userModlisteners.put(randomId, listener);
				return randomId;//Perhaps inefficient but it is working for a small amount of listeners
			}
		}
	}

	@Override
	public synchronized boolean removeOnUserModifiedListener(long id) {
		Long packedId = id;
		return userModlisteners.remove(packedId) != null;
	}

	@Override
	public synchronized void dispatchUserModified(User user) {
		for(Map.Entry<Long, UserModifiedListener> e : userModlisteners.entrySet()) {
			UserModifiedListener listener = e.getValue();
			synchronized(listener) {
				listener.onUserModified(user);
			}
		}
	}

	@Override
	public synchronized long addOnUserGroupModifiedListener(UserGroupModifiedListener listener) {
		while(true) {
			Long randomId = rand.nextLong();
			if(!groupModlisteners.containsKey(randomId)) {
				groupModlisteners.put(randomId, listener);
				return randomId;//Perhaps inefficient but it is working for a small amount of listeners
			}
		}
	}

	@Override
	public synchronized boolean removeOnUserGroupModifiedListener(long id) {
		Long packedId = id;
		return groupModlisteners.remove(packedId) != null;
	}

	@Override
	public synchronized void dispatchUserGroupModified(UserGroup group) {
		for(Map.Entry<Long, UserGroupModifiedListener> e : groupModlisteners.entrySet()) {
			UserGroupModifiedListener listener = e.getValue();
			synchronized(listener) {
				listener.onUserGroupModified(group);
			}
		}
	}

}
