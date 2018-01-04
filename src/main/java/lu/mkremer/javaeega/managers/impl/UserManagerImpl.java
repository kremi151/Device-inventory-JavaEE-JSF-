package lu.mkremer.javaeega.managers.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import lu.mkremer.javaeega.managers.EventManager;
import lu.mkremer.javaeega.managers.UserManager;
import lu.mkremer.javaeega.users.User;
import lu.mkremer.javaeega.users.UserDescription;
import lu.mkremer.javaeega.users.UserGroup;

//@Stateless
@Singleton
public class UserManagerImpl implements UserManager{

	@PersistenceContext
	private EntityManager em;
	
	@EJB private EventManager eventManager;

	@Override
	public User createUser(String userId, String firstName, String lastName, String hashedPassword, UserGroup group) {
		User user = new User(userId, firstName, lastName, hashedPassword, group);
		em.persist(user);
		return user;
	}

	@Override
	public void removeUser(User user) {
		em.remove(user);
	}

	@Override
	public UserGroup getDefaultGroup() {
		List<UserGroup> groups = em.createQuery("select g from UserGroup g", UserGroup.class).getResultList();
		if(!groups.isEmpty()) {
			return groups.get(0);
		}else {
			UserGroup group = new UserGroup("Normal user");
			em.persist(group);
			return group;
		}
	}

	@Override
	public UserGroup createGroup(String name) {
		UserGroup group = new UserGroup(name);
		em.persist(group);
		return group;
	}

	@Override
	public boolean doesUsernameExist(String username) {
		return !em.createQuery("select u from User u where user_id = :id").setParameter("id", username).getResultList().isEmpty();
	}

	@Override
	public User findUser(String username) {
		List<User> results = em.createQuery("select u from User u where user_id = :username", User.class)
				.setParameter("username", username)
				.getResultList();
		if(results.isEmpty()) {
			return null;
		}else {
			return results.get(0);
		}
	}

	@Override
	public List<User> listAllUsers() {
		return em.createQuery("select u from User u", User.class).getResultList();
	}

	@Override
	public List<User> listMatchingUsers(String filter) {
		return em.createQuery("select u from User u where u.user_id like :filter or u.firstName like :filter or u.lastName like :filter", User.class)
				.setParameter("filter", "%"+filter+"%").getResultList();
	}

	@Override
	public void update(UserGroup group) {
		em.merge(group);
		new Thread(() -> eventManager.dispatchUserGroupModified(group)).start();
	}

	@Override
	public List<UserGroup> getUserGroups() {
		return em.createQuery("select g from UserGroup g", UserGroup.class).getResultList();
	}

	@Override
	public UserGroup getGroupById(long id) {
		return em.find(UserGroup.class, id);
	}

	@Override
	public List<UserDescription> listUserDescriptions() {
		return em.createQuery("select new " + UserDescription.class.getCanonicalName() + "(u.user_id, u.group.name, u.group.id) from User u", UserDescription.class).getResultList();
	}

	@Override
	public void update(User user) {
		em.merge(user);
		new Thread(() -> eventManager.dispatchUserModified(user)).start();
	}
	
}
