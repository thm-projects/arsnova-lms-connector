package net.particify.arsnova.connector.config;

import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

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
public class AppConfig {
	private static final String DAO_PACKAGE = "net.particify.arsnova.connector.dao";

	@Autowired
	private Environment env;

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean(name = "dataSource")
	public DriverManagerDataSource dataSource() throws SQLException {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(env.getProperty("data-access.jdbc.driver-class-name"));
		dataSource.setUrl(env.getProperty("data-access.jdbc.url"));
		dataSource.setUsername(env.getProperty("data-access.jdbc.username"));
		dataSource.setPassword(env.getProperty("data-access.jdbc.password"));
		return dataSource;
	}

	@Bean
	public ConnectorDao connectorDao() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		final String implClass = env.getProperty("data-access.implementation").contains(".")
				? env.getProperty("data-access.implementation")
				: DAO_PACKAGE + "." + env.getProperty("data-access.implementation");
		return (ConnectorDao) Class.forName(implClass).newInstance();
	}
}
