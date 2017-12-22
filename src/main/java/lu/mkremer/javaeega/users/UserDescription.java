package lu.mkremer.javaeega.users;

import java.io.Serializable;

public class UserDescription implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8878273714943788554L;
	
	private final String username, groupName;
	private final long groupId;
	
	public UserDescription(String username, String groupName, long groupId) {
		this.username = username;
		this.groupName = groupName;
		this.groupId = groupId;
	}

	public String getUsername() {
		return username;
	}

	public String getGroupName() {
		return groupName;
	}

	public long getGroupId() {
		return groupId;
	}
}
