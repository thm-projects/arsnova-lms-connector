package de.thm.arsnova.connector.config;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import de.thm.arsnova.connector.dao.ConnectorDao;

@ComponentScan(basePackages = {
		"de.thm.arsnova.connector.dao",
		"de.thm.arsnova.connector.services"
})
@Configuration
@PropertySources({
		@PropertySource(value = "classpath:connector.properties.example"),
		@PropertySource(value = "file:///etc/arsnova/connector.properties", ignoreResourceNotFound = true)
})
public class AppConfig {

	@Autowired
	private Environment env;

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

	@Bean
	public ConnectorDao connectorDao() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		return (ConnectorDao) Class.forName(env.getProperty("dao.implementation")).newInstance();
	}
}
