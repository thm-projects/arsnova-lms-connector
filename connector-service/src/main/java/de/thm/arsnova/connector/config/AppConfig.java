package de.thm.arsnova.connector.config;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	Environment env;
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	
    @Bean
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
    	String classname = env.getProperty("dao.implementation");
    	return (ConnectorDao)Class.forName(classname).newInstance();
    }
    
    @Bean
    public UniRepDao uniRepDao() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
    	return new IliasConnectorDaoImpl();
    }
}
