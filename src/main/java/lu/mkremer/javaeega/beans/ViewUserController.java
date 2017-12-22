package lu.mkremer.javaeega.beans;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lu.mkremer.javaeega.managers.UserManager;
import lu.mkremer.javaeega.users.User;
import lu.mkremer.javaeega.users.UserGroup;
import lu.mkremer.javaeega.util.MessageHelper;

@ManagedBean(name="vuser")
@ViewScoped
public class ViewUserController implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5800770152969247617L;

	@ManagedProperty("#{usession}")
	private UserSession session;
	
	@EJB private UserManager um;
	
	private User user;
	
	@NotNull(message="No first name supplied")
	@Size(min=2, max=32, message="First name must be between {min} and {max} characters long") 
	private String firstName;
	
	@NotNull(message="No last name supplied")
	@Size(min=2, max=32, message="Last name must be between {min} and {max} characters long") 
	private String lastName;
	
	@NotNull
	private long groupId;
	
	@PostConstruct
	public void init() {
		if(session.isLoggedIn()) {
			String uid = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
			if(uid != null) {
				user = um.findUser(uid);
				firstName = user.getFirstName();
				lastName = user.getLastName();
				groupId = user.getGroup().getId();
			}
		}
	}
	
	public void setSession(UserSession session) {
		this.session = session;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public User getUser() {
		return user;
	}
	
	public boolean canView() {
		return user != null && session.canViewUser(user);
	}
	
	public List<UserGroup> getUserGroups(){
		return um.getUserGroups();
	}
	
	public void saveModifications() {
		if(user != null && session.canModifyUsers()) {
			UserGroup group = user.getGroup();
			if(user.getGroup().getId() != groupId) {
				group = um.getGroupById(groupId);
				if(group == null) {
					MessageHelper.throwWarningMessage("Requested user group has not been found");
				}
			}
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setGroup(group);
			um.update(user);
			MessageHelper.throwInfoMessage("User information have been updated");
		}else {
			MessageHelper.throwDangerMessage("You are not allowed to do this");
		}
	}

}
