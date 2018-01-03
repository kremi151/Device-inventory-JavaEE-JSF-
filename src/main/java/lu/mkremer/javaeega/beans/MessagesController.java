package lu.mkremer.javaeega.beans;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ComponentSystemEvent;

import lu.mkremer.javaeega.managers.MessageManager;
import lu.mkremer.javaeega.util.MessageHelper;

@ManagedBean(name="messages")
@ViewScoped
public class MessagesController {

	@ManagedProperty("#{usession}")
	private UserSession session;
	
	@EJB private MessageManager mm;
	
	public void setSession(UserSession session) {
		this.session = session;
	}
	
	public void preRenderPage(ComponentSystemEvent event) {
		if(session.isLoggedIn()) {
			List<MessageManager.Message> msgs = mm.getMessagesForUser(session.getUser());
			if(msgs.size() > 0) {
				for(MessageManager.Message msg : msgs) {
					MessageHelper.throwMessage(msg.getHtml(), msg.getSeverity());
				}
			}else {
				MessageHelper.throwInfoMessage("No messages found");
			}
		}
	}
}
