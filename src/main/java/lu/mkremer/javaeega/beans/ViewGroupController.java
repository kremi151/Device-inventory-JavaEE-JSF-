package lu.mkremer.javaeega.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
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
import lu.mkremer.javaeega.users.UserGroup;
import lu.mkremer.javaeega.util.MessageHelper;

@ManagedBean(name="vgroup")
@ViewScoped
public class ViewGroupController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 990655755002494175L;
	
	@EJB private UserManager um;
	
	private UserGroup group;
	
	@ManagedProperty("#{usession}")
	private UserSession session;
	
	private String selectedPermissions[];
	
	@NotNull(message="No permission node supplied")
	@Size(min=1, message="No permission node supplied")
	private String permissionNode;
	
	@PostConstruct
	public void init() {
		if(session.canViewUserGroups()) {
			String rawId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
			if(rawId != null) {
				try {
					group = um.getGroupById(Long.parseLong(rawId));
				}
				catch(NumberFormatException e) {}
			}
		}
	}
	
	public void setSession(UserSession session) {
		this.session = session;
	}
	
	public UserGroup getGroup() {
		return group;
	}

	public List<String> getPermissions(){
		return group != null ? new ArrayList<>(group.getPermissions()) : Collections.emptyList();
	}
	
	public String[] getSelectedPermissions() {
		return selectedPermissions;
	}

	public void setSelectedPermissions(String[] selectedPermissions) {
		this.selectedPermissions = selectedPermissions;
	}

	public String getPermissionNode() {
		return permissionNode;
	}

	public void setPermissionNode(String permissionNode) {
		this.permissionNode = permissionNode;
	}

	public void deletePermissions() {//TODO: Make user sessions being part of this group reloading from database
		if(group != null && session.canModifyUserGroups()) {
			if(selectedPermissions != null)for(String perm : selectedPermissions) {
				group.removePermission(perm);
			}
			um.update(group);
			selectedPermissions = null;
		}else {
			MessageHelper.throwDangerMessage("You are not allowed to do this");
		}
	}
	
	public void addPermission() {
		if(group != null && session.canModifyUserGroups()) {
			group.addPermission(permissionNode);
			um.update(group);
		}else {
			MessageHelper.throwDangerMessage("You are not allowed to do this");
		}
	}

}
