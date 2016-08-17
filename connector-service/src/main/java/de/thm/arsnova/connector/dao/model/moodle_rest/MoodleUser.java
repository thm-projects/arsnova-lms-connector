package de.thm.arsnova.connector.dao.model.moodle_rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MoodleUser {
	
	private int id;
	private String username;
	private Role[] roles;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public Role[] getRoles() {
		return roles;
	}

	public void setRoles(Role[] roles) {
		this.roles = roles;
	}
	
	public int getFirstMoodleRoleId(){
		return getRoles()[0].getRoleid();
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	private static class Role{
		
		private int roleid;
		private String name;
		
		public int getRoleid() {
			return roleid;
		}
		public void setRoleid(int roleid) {
			this.roleid = roleid;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
	}
}
