package net.particify.arsnova.connector.config;

import org.springframework.context.annotation.Bean;

import net.particify.arsnova.connector.dao.ConnectorDao;
import net.particify.arsnova.connector.dao.StudipConnectorDaoImpl;
import net.particify.arsnova.connector.services.ConnectorService;
import net.particify.arsnova.connector.services.ConnectorServiceImpl;

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
