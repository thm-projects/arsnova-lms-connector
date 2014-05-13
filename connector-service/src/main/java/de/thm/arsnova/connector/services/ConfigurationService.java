package de.thm.arsnova.connector.services;

import de.thm.arsnova.connector.persistence.domain.Configuration;

public interface ConfigurationService {
	Configuration getConfigurationElement(String key);
	void setConfigurationElement(Configuration config);
}
