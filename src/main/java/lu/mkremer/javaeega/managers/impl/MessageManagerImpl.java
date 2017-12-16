package lu.mkremer.javaeega.managers.impl;

import java.util.Collections;
import java.util.List;

import javax.ejb.Singleton;

import lu.mkremer.javaeega.managers.MessageManager;
import lu.mkremer.javaeega.users.User;

@Singleton
public class MessageManagerImpl implements MessageManager{

	@Override
	public List<String> getMessages(User user) {
		return Collections.emptyList();//TODO: Messages
	}

}
