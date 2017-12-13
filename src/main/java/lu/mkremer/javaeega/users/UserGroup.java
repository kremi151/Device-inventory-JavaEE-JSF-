package lu.mkremer.javaeega.users;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class UserGroup implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7748386828367747683L;
	
	@Id
	@GeneratedValue
	private long group_id;
	
	@Column(nullable=false)
	private String name;
	
	@ElementCollection(fetch = FetchType.EAGER)
	private Set<String> permissions = new HashSet<String>();
	
	public UserGroup() {}
	
	public UserGroup(String name) {
		this.name = name;
	}
	
	public long getId() {
		return group_id;
	}

	public Set<String> getPermissions(){
		return Collections.unmodifiableSet(permissions);
	}
	
	public void addPermission(String perm) {
		permissions.add(perm);
	}
	
	public boolean hasPermission(String perm) {
		return permissions.contains(perm);
	}
	
	public String getName() {
		return name;
	}
}
