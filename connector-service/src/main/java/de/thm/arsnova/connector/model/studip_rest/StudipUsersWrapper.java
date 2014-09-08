package de.thm.arsnova.connector.model.studip_rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StudipUsersWrapper {
	private StudipUser[] users;

	public StudipUser[] getUsers() {
		return users;
	}

	public void setUsers(StudipUser[] users) {
		this.users = users;
	}
}
