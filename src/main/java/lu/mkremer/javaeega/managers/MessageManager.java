package lu.mkremer.javaeega.managers;

import java.util.List;

import javax.ejb.Local;

import lu.mkremer.javaeega.users.User;

@Local
public interface MessageManager {

	List<String> getMessages(User user);
	
}
