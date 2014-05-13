package de.thm.arsnova.connector.config;

import org.springframework.context.annotation.Bean;

import de.thm.arsnova.connector.dao.ConnectorDao;
import de.thm.arsnova.connector.dao.MoodleConnectorDaoImpl;
import de.thm.arsnova.connector.services.ConnectorService;
import de.thm.arsnova.connector.services.ConnectorServiceImpl;

public class MoodleTestConfig {

	@Bean
	public ConnectorDao connectorDao() {
		return new MoodleConnectorDaoImpl();
	}

	@Bean
	public ConnectorService connectorServiceImplementation() {
		return new ConnectorServiceImpl();
	}
}
