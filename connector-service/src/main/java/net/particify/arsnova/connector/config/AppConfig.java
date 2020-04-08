package net.particify.arsnova.connector.config;

import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import net.particify.arsnova.connector.config.properties.AuthenticationProperties;
import net.particify.arsnova.connector.config.properties.DataAccessProperties;
import net.particify.arsnova.connector.dao.ConnectorDao;

@ComponentScan(basePackages = {
		"net.particify.arsnova.connector.dao",
		"net.particify.arsnova.connector.services"
})
@Configuration
@PropertySource(
		value = {
				"classpath:config/defaults.yml",
				"classpath:config/defaults-moodle.yml"
		},
		factory = YamlPropertySourceFactory.class)
@PropertySource(
		value = {
				"file:${connector.config-dir:.}/application.yml",
				"file:${connector.config-dir:.}/lms-connector.yml",
				"file:${connector.config-dir:.}/secrets.yml"},
		ignoreResourceNotFound = true,
		factory = YamlPropertySourceFactory.class)
@EnableConfigurationProperties({
		AuthenticationProperties.class,
		DataAccessProperties.class})
public class AppConfig {
	private static final String DAO_PACKAGE = "net.particify.arsnova.connector.dao";

	@Autowired
	private DataAccessProperties dataAccessProperties;

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean(name = "dataSource")
	public DriverManagerDataSource dataSource() throws SQLException {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(dataAccessProperties.getJdbc().getDriverClassName());
		dataSource.setUrl(dataAccessProperties.getJdbc().getUrl());
		dataSource.setUsername(dataAccessProperties.getJdbc().getUsername());
		dataSource.setPassword(dataAccessProperties.getJdbc().getPassword());
		return dataSource;
	}

	@Bean
	public ConnectorDao connectorDao() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		final String implClass = dataAccessProperties.getImplementation().contains(".")
				? dataAccessProperties.getImplementation()
				: DAO_PACKAGE + "." + dataAccessProperties.getImplementation();
		return (ConnectorDao) Class.forName(implClass).newInstance();
	}
}
