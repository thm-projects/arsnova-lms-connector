package de.thm.arsnova.connector.config;

import org.springframework.context.annotation.Bean;

import de.thm.arsnova.connector.dao.ConnectorDao;
import de.thm.arsnova.connector.dao.StudipConnectorDaoImpl;
import de.thm.arsnova.connector.services.ConnectorService;
import de.thm.arsnova.connector.services.ConnectorServiceImpl;

public class StudipTestConfig {

	@Bean
	public ConnectorDao connectorDao() {
		return new StudipConnectorDaoImpl();
	}

	@Bean
	public ConnectorService connectorServiceImplementation() {
		return new ConnectorServiceImpl();
	}
}
