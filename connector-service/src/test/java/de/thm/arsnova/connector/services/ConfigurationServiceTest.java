package de.thm.arsnova.connector.services;

import static org.junit.Assert.assertEquals;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.thm.arsnova.connector.config.RepositoryTestConfig;
import de.thm.arsnova.connector.persistence.domain.Configuration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration( classes = {RepositoryTestConfig.class} )
public class ConfigurationServiceTest {

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private DataSource dataSource;

	@Test
	public void testShouldNotReturnCourseForNotEnroledUser() {
		Configuration config = new Configuration();
		config.setKey("Testkey");
		config.setValue("Testvalue");

		configurationService.setConfigurationElement(config);

		Configuration actual = configurationService.getConfigurationElement("Testkey");
		assertEquals(config, actual);
	}
}