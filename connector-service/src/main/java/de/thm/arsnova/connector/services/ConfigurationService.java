package de.thm.arsnova.connector.services;

import org.springframework.security.access.prepost.PreAuthorize;

import de.thm.arsnova.connector.persistence.domain.Configuration;

public interface ConfigurationService {

	@PreAuthorize(value = "hasRole('ADMIN') or hasPermission(#username, 'configuration', 'read')")
	Configuration getConfigurationElement(String key);

	@PreAuthorize(value = "hasRole('ADMIN') or hasPermission(#username, 'configuration', 'write')")
	void setConfigurationElement(Configuration config);
}
