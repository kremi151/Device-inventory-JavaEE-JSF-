package lu.mkremer.javaeega.beans;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.mindrot.jbcrypt.BCrypt;

import lu.mkremer.javaeega.devices.Device;
import lu.mkremer.javaeega.managers.EventManager;
import lu.mkremer.javaeega.managers.MessageManager;
import lu.mkremer.javaeega.managers.UserManager;
import lu.mkremer.javaeega.users.User;
import lu.mkremer.javaeega.util.MessageHelper;

@SessionScoped
@ManagedBean(name="usession")
public class UserSession implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6591517985681627665L;
	
	@EJB private UserManager um;
	@EJB private MessageManager mm;
	@EJB private EventManager eventManager;
	
	private User user;
	
	private String username;
	private String password;
	
	private long userModificationListenerId, userGroupModificationListenerId;
	
	@PostConstruct
	public void init() {
		userModificationListenerId = eventManager.addOnUserModifiedListener(user -> {
			if(this.user != null && this.user.getUserId().equals(user.getUserId())) {
				this.user = user;
			}
		});
		userGroupModificationListenerId = eventManager.addOnUserGroupModifiedListener(group -> {
			if(this.user != null && this.user.getGroup().getId() == user.getGroup().getId()) {
				this.user = um.findUser(this.user.getUserId());
			}
		});
	}
	
	@PreDestroy
	public void preDestroy() {
		eventManager.removeOnUserModifiedListener(userModificationListenerId);
		eventManager.removeOnUserGroupModifiedListener(userGroupModificationListenerId);
	}

	public String login() {
		User user = um.findUser(username);

		if(user != null) {
			if(BCrypt.checkpw(password, user.getPassword())) {
				username = null;
				password = null;
				this.user = user;
				return "index";
			}else {
				MessageHelper.throwDangerMessage("Invalid username or password");
			}
		}else {
			MessageHelper.throwDangerMessage("Invalid username or password");
		}
		password = null;
		return "login";
	}
	
	public void logout() throws IOException {
		System.out.println("Logging out " + getDisplayName());
		user = null;
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	    ec.invalidateSession();
	    ec.redirect(ec.getRequestContextPath() + "/index.xhtml");
	}
	
	public User getUser() {
		return user;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public int getNotificationCount() {
		return (user != null) ? mm.notificationCount(user) : 0;
	}
	
	public String getDisplayName() {
		return user != null ? user.getFirstName() + " " + user.getLastName() : "MissingNo.";
	}
	
	public boolean isLoggedIn() {
		return user != null;
	}
	
	public boolean canAddDevices() {
		return user != null && user.hasPermission("devices.add");
	}
	
	public boolean canReadDevice(Device device) {
		return device != null && user != null && (device.getOwner().equals(user) || user.hasPermission("devices.view"));
	}

	public boolean canModifyDevice(Device device) {
		return device != null && user != null && user.hasPermission("devices.modify");
	}
	
	public boolean canReportOnDevice(Device device) {
		return canReadDevice(device);
	}
	
	public boolean canRemoveDevice(Device device) {
		return device != null && user != null && user.hasPermission("devices.remove");
	}
	
	public boolean canListDevices() {
		return user != null && user.hasPermission("devices.view");
	}
	
	public boolean canRemoveDeviceTypes() {
		return user != null && user.hasPermission("devicetypes.remove");
	}
	
	public boolean canViewDeviceTypes() {
		return user != null && user.hasPermission("devicetypes.view");
	}
	
	public boolean canListConsumables() {
		return user != null && user.hasPermission("consumables.view");
	}
	
	public boolean canAddConsumableTypes() {
		return user != null && user.hasPermission("consumabletypes.add");
	}
	
	public boolean canViewConsumableTypes() {
		return user != null && user.hasPermission("consumabletypes.view");
	}
	
	public boolean canModifyConsumableTypes() {
		return user != null && user.hasPermission("consumabletypes.modify");
	}
	
	public boolean canRemoveConsumableTypes() {
		return user != null && user.hasPermission("consumabletypes.remove");
	}
	
	public boolean canModifyDeviceType() {
		return user != null && user.hasPermission("devicetypes.modify");
	}
	
	public boolean canAddDeviceTypes() {
		return user != null && user.hasPermission("devicetypes.add");
	}
	
	public boolean canSubmitInterventions() {
		return user != null && user.hasPermission("interventions.add");
	}
	
	public boolean canModifyConsumables() {
		return user != null && user.hasPermission("consumables.modify");
	}
	
	public boolean canAddConsumables() {
		return user != null && user.hasPermission("consumables.add");
	}
	
	public boolean canRemoveConsumables() {
		return user != null && user.hasPermission("consumables.remove");
	}
	
	public boolean canViewUserGroups() {
		return user != null && user.hasPermission("usergroups.view");
	}
	
	public boolean canModifyUserGroups() {
		return user != null && user.hasPermission("usergroups.modify");
	}
	
	public boolean canViewUser(User user) {
		return this.user != null && (this.user.equals(user) || this.user.hasPermission("users.view"));
	}
	
	public boolean canViewUser(String username) {
		return user != null && (user.getUserId().equals(username) || user.hasPermission("users.view"));
	}
	
	public boolean canModifyUsers() {
		return user != null && user.hasPermission("users.modify");
	}
	
}
