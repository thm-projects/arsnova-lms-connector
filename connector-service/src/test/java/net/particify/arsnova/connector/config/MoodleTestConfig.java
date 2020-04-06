package net.particify.arsnova.connector.config;

import org.springframework.context.annotation.Bean;

import net.particify.arsnova.connector.dao.ConnectorDao;
import net.particify.arsnova.connector.dao.MoodleConnectorDaoImpl;
import net.particify.arsnova.connector.services.ConnectorService;
import net.particify.arsnova.connector.services.ConnectorServiceImpl;

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
