package de.thm.arsnova.connector.config;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
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

	@Autowired private Environment env;

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean(name = "dataSource")
	public DriverManagerDataSource dataSource() throws SQLException {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
		dataSource.setUrl(env.getProperty("jdbc.url"));
		dataSource.setUsername(env.getProperty("jdbc.username"));
		dataSource.setPassword(env.getProperty("jdbc.password"));
		return dataSource;
	}

	@Bean(name = "configDataSource")
	public DriverManagerDataSource configDataSource() throws SQLException {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.hsqldb.jdbc.JDBCDriver");
		dataSource.setUrl("file:///etc/arsnova/connector.db");
		dataSource.setUsername("whatever");
		dataSource.setPassword("topsecret");
		return dataSource;
	}

	@Bean
	public ConnectorDao connectorDao() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		return (ConnectorDao) Class.forName(env.getProperty("dao.implementation")).newInstance();
	}

	@Bean
	public UniRepDao uniRepDao() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		if ("enable".equals(env.getProperty("service.startIliasConnector"))) {
			return new IliasConnectorDaoImpl();
		}
		return null;
	}
}
