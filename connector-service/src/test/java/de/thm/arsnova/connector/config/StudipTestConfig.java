package de.thm.arsnova.connector.config;

import java.sql.SQLException;

import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import de.thm.arsnova.connector.dao.ConnectorDao;
import de.thm.arsnova.connector.dao.StudipConnectorDaoImpl;
import de.thm.arsnova.connector.services.ConnectorService;
import de.thm.arsnova.connector.services.ConnectorServiceImpl;

public class StudipTestConfig {

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
		return new StudipConnectorDaoImpl();
	}

	@Bean
	public ConnectorService connectorServiceImplementation() {
		return new ConnectorServiceImpl();
	}
}
