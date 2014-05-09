package de.thm.arsnova.connector.persistence.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.openjpa.persistence.jdbc.Unique;

@Entity
@Table(name = "user")
public class User {

	@Id
	@Unique
	private String userId;
	private String password;
	private boolean isAdmin;

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isAdmin() {
		return isAdmin;
	}
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		if (obj instanceof User) {
			User other = (User) obj;
			if (
					(this.userId != null && this.userId.equals(other.getUserId()))
					&& (this.isAdmin = other.isAdmin())
					) return true;
		}
		return false;
	}
}
