package lu.mkremer.javaeega.beans;

import java.io.IOException;
import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.mindrot.jbcrypt.BCrypt;

import lu.mkremer.javaeega.devices.Device;
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
	
	@EJB
	private UserManager um;
	
	private User user;
	
	private String username;
	private String password;

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
	
	User getUser() {
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
	
	public boolean canShowManagementMenu() {
		return user != null && (user.hasPermission("devices.view") || user.hasPermission("devicetypes.view"));
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
	
	public static UserSession getCurrentSession() {
		FacesContext context = FacesContext.getCurrentInstance();
		return context.getApplication().evaluateExpressionGet(context, "#{usession}", UserSession.class);
	}
	
}
