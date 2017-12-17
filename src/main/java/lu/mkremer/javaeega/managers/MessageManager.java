package lu.mkremer.javaeega.managers;

import java.util.List;

import javax.ejb.Local;

import lu.mkremer.javaeega.consumables.Consumable;
import lu.mkremer.javaeega.users.User;

@Local
public interface MessageManager {

	void notifyConsumableStock(Consumable consumable);
	void untrackConsumable(Consumable consumable);
	List<String> getWarningMessages();
	int notificationCount(User user);
	
}
