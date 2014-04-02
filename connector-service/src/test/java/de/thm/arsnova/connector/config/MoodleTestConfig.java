package de.thm.arsnova.connector.config;

import java.sql.SQLException;

import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import de.thm.arsnova.connector.dao.ConnectorDao;
import de.thm.arsnova.connector.dao.MoodleConnectorDaoImpl;
import de.thm.arsnova.connector.services.ConnectorService;
import de.thm.arsnova.connector.services.ConnectorServiceImpl;

public class MoodleTestConfig {

	@Bean(name = "dataSource")
	public DriverManagerDataSource dataSource() throws SQLException {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.hsqldb.jdbcDriver");
		dataSource.setUrl("jdbc:hsqldb:mem:testdb");
		dataSource.setUsername("test");
		dataSource.setPassword("");
		return dataSource;
	}

	@Bean
	public ConnectorDao connectorDao() {
		return new MoodleConnectorDaoImpl();
	}

	@Bean
	public ConnectorService connectorServiceImplementation() {
		return new ConnectorServiceImpl();
	}
}
