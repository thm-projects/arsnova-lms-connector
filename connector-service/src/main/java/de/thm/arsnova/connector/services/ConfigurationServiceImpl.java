package de.thm.arsnova.connector.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.thm.arsnova.connector.persistence.domain.Configuration;
import de.thm.arsnova.connector.persistence.repository.ConfigurationRepository;

@Service
public class ConfigurationServiceImpl implements ConfigurationService {

	@Autowired
	private ConfigurationRepository repo;

	@Override
	public Configuration getConfigurationElement(String key) {
		return repo.findOne(key);
	}

	@Override
	public void setConfigurationElement(Configuration config) {
		repo.save(config);
	}
}
