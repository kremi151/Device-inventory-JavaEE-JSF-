package lu.mkremer.javaeega.beans;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import lu.mkremer.javaeega.managers.UserManager;
import lu.mkremer.javaeega.users.User;

@ManagedBean(name="selectuser")
@ViewScoped
public class SelectUserController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3707953314156391313L;

	@EJB
	private UserManager um;
	
	private String filter = null;
	
	public String getFilter() {
		return filter;
	}
	
	public void setFilter(String filter) {
		this.filter = filter;
	}
	
	public List<User> getDisplayingUsers(){
		if(filter == null || filter.length() == 0) {
			return um.listAllUsers();
		}else {
			return um.listMatchingUsers(filter);
		}
	}
	
	public void onChange() {
		System.out.println("### Current filter: " + filter);
	}
	//TODO: Fix this AJAX bug which is impossible to be fixed no matter how hard I try...
	
}
