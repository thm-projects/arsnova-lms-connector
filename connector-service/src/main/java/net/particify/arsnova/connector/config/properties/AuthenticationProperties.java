package net.particify.arsnova.connector.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(AuthenticationProperties.PREFIX)
public class AuthenticationProperties {
	public static final String PREFIX = "authentication";

	private String username;
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}
}
