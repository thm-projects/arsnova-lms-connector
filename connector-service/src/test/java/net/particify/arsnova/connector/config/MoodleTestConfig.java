package net.particify.arsnova.connector.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

import net.particify.arsnova.connector.dao.ConnectorDao;
import net.particify.arsnova.connector.dao.MoodleConnectorDaoImpl;
import net.particify.arsnova.connector.services.ConnectorService;
import net.particify.arsnova.connector.services.ConnectorServiceImpl;

@PropertySource(
		value = "classpath:config/defaults-moodle.yml",
		factory = YamlPropertySourceFactory.class)
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
