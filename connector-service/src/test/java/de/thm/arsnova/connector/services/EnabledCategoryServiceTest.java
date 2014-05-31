package de.thm.arsnova.connector.services;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration( classes = {RepositoryTestConfig.class, RepositoryTestConfig.class} )
public class EnabledCategoryServiceTest {

	@Autowired
	private EnabledCategoryService enabledCategoryService;

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
	public void testShouldIndicateEnabledCategories() {
		assertTrue(enabledCategoryService.isEnabledCategory(2));
		assertTrue(enabledCategoryService.isEnabledCategory(3));
		assertTrue(enabledCategoryService.isEnabledCategory(5));
	}

	@Test
	public void testShouldIndicateDisabledCategories() {
		assertFalse(enabledCategoryService.isEnabledCategory(1));
		assertFalse(enabledCategoryService.isEnabledCategory(4));
		assertFalse(enabledCategoryService.isEnabledCategory(6));
	}

	@Test
	public void testShouldEnableOneCategory() {
		enabledCategoryService.enableCategory(123);
		assertTrue(enabledCategoryService.isEnabledCategory(123));
	}

	@Test
	public void testShouldDisableOneCategory() {
		enabledCategoryService.disableCategory(5);
		assertFalse(enabledCategoryService.isEnabledCategory(5));
	}
}