package lu.mkremer.javaeega.managers;

import java.util.List;

import javax.ejb.Local;
import javax.faces.application.FacesMessage;

import lu.mkremer.javaeega.consumables.Consumable;
import lu.mkremer.javaeega.consumables.ConsumableType;
import lu.mkremer.javaeega.users.User;

@Local
public interface MessageManager {

	void notifyConsumableStock(Consumable consumable);
	void untrackConsumable(Consumable consumable);
	void untrackConsumableType(ConsumableType type);
	List<Message> getMessagesForUser(User user);
	int notificationCount(User user);
	
	public static class Message{
		private final String html;
		private final FacesMessage.Severity severity;
		
		public Message(String html, FacesMessage.Severity severity) {
			this.html = html;
			this.severity = severity;
		}
		
		public String getHtml() {
			return html;
		}
		
		public FacesMessage.Severity getSeverity(){
			return severity;
		}
	}
	
}
