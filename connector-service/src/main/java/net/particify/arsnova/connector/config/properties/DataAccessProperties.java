package net.particify.arsnova.connector.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(DataAccessProperties.PREFIX)
public class DataAccessProperties {
	public static final String PREFIX = "data-access";

	private String implementation;
	private Jdbc jdbc;

	public String getImplementation() {
		return implementation;
	}

	public void setImplementation(final String implementation) {
		this.implementation = implementation;
	}

	public Jdbc getJdbc() {
		return jdbc;
	}

	public void setJdbc(final Jdbc jdbc) {
		this.jdbc = jdbc;
	}

	public static class Jdbc {
		private String driverClassName;
		private String url;
		private String username;
		private String password;

		public String getDriverClassName() {
			return driverClassName;
		}

		public void setDriverClassName(final String driverClassName) {
			this.driverClassName = driverClassName;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(final String url) {
			this.url = url;
		}

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
}
