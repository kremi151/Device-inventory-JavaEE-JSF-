package lu.mkremer.javaeega.beans;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import lu.mkremer.javaeega.managers.UserManager;
import lu.mkremer.javaeega.users.UserDescription;
import lu.mkremer.javaeega.users.UserGroup;

@ManagedBean(name="usermgmt")
@ViewScoped
public class UserManagementController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8774084878963163969L;
	
	@EJB private UserManager um;
	
	@ManagedProperty("#{usession}")
	private UserSession session;
	
	public void setSession(UserSession session) {
		this.session = session;
	}

	public List<UserGroup> getUserGroups(){
		if(session.canViewUserGroups()) {
			return um.getUserGroups();
		}else {
			return Collections.emptyList();
		}
	}
	
	public List<UserDescription> getUsers(){
		return um.listUserDescriptions();
	}

}
