package lu.mkremer.javaeega.users;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lu.mkremer.javaeega.devices.Device;
import lu.mkremer.javaeega.intervention.Intervention;
import lu.mkremer.javaeega.intervention.Report;

@Entity
public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2084198050106908213L;
	
	@Id
	private String user_id;
	
	@ManyToOne(optional=false)
	private UserGroup group;
	
	@Column(nullable=false)
	private String firstName;
	
	@Column(nullable=false)
	private String lastName;
	
	@Column(nullable=false)
	private String password;
	
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="owner")
	private List<Device> devices;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="user")
	private List<Report> reports;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="responsible")
	private List<Intervention> interventions;
	
	public User() {}
	
	public User(String userId, String firstName, String lastName, String passwordHash, UserGroup group) {
		this.user_id = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.group = group;
		this.password = passwordHash;
	}
	
	public String getUserId() {
		return user_id;
	}
	
	public UserGroup getGroup() {
		return group;
	}
	
	public String getPassword() {
		return password;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}
	
	public void setGroup(UserGroup group) {
		this.group = group;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public boolean hasPermission(String perm) {
		return group.hasPermission(perm);
	}
	
	@Override
	public boolean equals(Object other) {
		if(other == null || other.getClass() != User.class) {
			return false;
		}else {
			return other == this || ((User)other).user_id.equals(this.user_id);
		}
	}
	
	@Override
	public int hashCode() {
		return user_id.hashCode();
	}
}
