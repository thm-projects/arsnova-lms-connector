package de.thm.arsnova.connector.config;

import org.springframework.context.annotation.Bean;

import de.thm.arsnova.connector.dao.ConnectorDao;
import de.thm.arsnova.connector.dao.DummyConnectorDaoImpl;
import de.thm.arsnova.connector.services.ConnectorService;
import de.thm.arsnova.connector.services.ConnectorServiceImpl;

public class DummyTestConfig {

	@Bean
	public ConnectorDao connectorDao() {
		return new DummyConnectorDaoImpl();
	}

	@Bean
	public ConnectorService connectorServiceImplementation() {
		return new ConnectorServiceImpl();
	}
}
