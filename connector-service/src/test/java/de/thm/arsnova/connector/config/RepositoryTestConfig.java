package de.thm.arsnova.connector.config;

import java.sql.SQLException;
import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.OpenJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import de.thm.arsnova.connector.services.ConfigurationService;
import de.thm.arsnova.connector.services.ConfigurationServiceImpl;
import de.thm.arsnova.connector.services.EnabledCategoryService;
import de.thm.arsnova.connector.services.EnabledCategoryServiceImpl;

@Configuration
@EnableJpaRepositories("de.thm.arsnova.connector.persistence.repository")
public class RepositoryTestConfig {

	@Bean
	public ConfigurationService configurationService() {
		return new ConfigurationServiceImpl();
	}

	@Bean
	public EnabledCategoryService enabledCategoryService() {
		return new EnabledCategoryServiceImpl();
	}

	@Bean(name = "configDataSource")
	public DriverManagerDataSource configDataSource() throws SQLException {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.hsqldb.jdbcDriver");
		dataSource.setUrl("jdbc:hsqldb:mem:testdb");
		dataSource.setUsername("test");
		dataSource.setPassword("");
		return dataSource;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws SQLException {
		LocalContainerEntityManagerFactoryBean lef = new LocalContainerEntityManagerFactoryBean();
		lef.setDataSource(configDataSource());
		lef.setJpaVendorAdapter(jpaVendorAdapter());
		lef.setPackagesToScan("de.thm.arsnova.connector.persistence.domain");
		Properties jpaProperties = new Properties();
		jpaProperties.put("openjpa.RuntimeUnenhancedClasses", "supported");
		lef.setJpaProperties(jpaProperties);
		lef.afterPropertiesSet();
		return lef;
	}

	@Bean
	public OpenJpaVendorAdapter jpaVendorAdapter() {
		OpenJpaVendorAdapter jpaVendorAdapter = new OpenJpaVendorAdapter();
		jpaVendorAdapter.setShowSql(true);
		jpaVendorAdapter.setGenerateDdl(true);
		return jpaVendorAdapter;
	}

	@Bean
	public PlatformTransactionManager transactionManager() throws SQLException {
		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(entityManagerFactory().getObject());
		return txManager;
	}
}
