package net.particify.arsnova.connector.model.studip_rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StudipUser {
	private String id;
	private String username;

	@JsonProperty("user_id")
	public String getId() {
		return id;
	}

	@JsonProperty("user_id")
	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
