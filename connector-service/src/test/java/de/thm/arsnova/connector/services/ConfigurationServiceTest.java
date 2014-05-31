package de.thm.arsnova.connector.services;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;

import javax.sql.DataSource;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.XmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
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

	@Before
	public void initDatabase() {
		try {
			final Connection con = dataSource.getConnection();
			final IDatabaseConnection connection = new DatabaseConnection(con);
			DatabaseOperation.CLEAN_INSERT.execute(connection, getDataSet());
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	@After
	public void cleanupDatabase() {
	}

	private IDataSet getDataSet() throws Exception {
		final FileInputStream fis = new FileInputStream(new File(
				"src/test/resources/dbunit/internaldb.xml"));
		return new XmlDataSet(fis);
	}

	@Test
	public void testShouldReturnConfiguration() {
		final Configuration actual = configurationService.getConfigurationElement("key2");
		assertEquals("value2", actual.getValue());
	}

	@Test
	public void testShouldAddAndReadConfiguration() {
		final Configuration config = new Configuration();
		config.setKey("Testkey");
		config.setValue("Testvalue");

		configurationService.setConfigurationElement(config);

		final Configuration actual = configurationService.getConfigurationElement("Testkey");
		assertEquals(config, actual);
	}
}