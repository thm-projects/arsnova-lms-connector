package de.thm.arsnova.connector.config;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import de.thm.arsnova.connector.dao.ConnectorDao;
import de.thm.arsnova.connector.dao.IliasConnectorDaoImpl;
import de.thm.arsnova.connector.dao.UniRepDao;

@ComponentScan(basePackages = {
		"de.thm.arsnova.connector.dao",
		"de.thm.arsnova.connector.services"
})
@PropertySource("file:///etc/arsnova/connector.properties")
public class AppConfig {

	@Value("jdbc.driverClassName") private String jdbcDriverClassName;
	@Value("jdbc.url") private String jdbcUrl;
	@Value("jdbc.username") private String jdbcUsername;
	@Value("jdbc.password") private String jdbcPassword;

	@Value("dao.implementation") private String daoImplementation;

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	public DriverManagerDataSource dataSource() throws SQLException {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(jdbcDriverClassName);
		dataSource.setUrl(jdbcUrl);
		dataSource.setUsername(jdbcUsername);
		dataSource.setPassword(jdbcPassword);
		return dataSource;
	}

	@Bean
	public ConnectorDao connectorDao() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		return (ConnectorDao) Class.forName(daoImplementation).newInstance();
	}

	@Bean
	public UniRepDao uniRepDao() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		return new IliasConnectorDaoImpl();
	}
}
