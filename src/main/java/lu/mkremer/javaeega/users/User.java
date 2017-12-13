package lu.mkremer.javaeega.users;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

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
