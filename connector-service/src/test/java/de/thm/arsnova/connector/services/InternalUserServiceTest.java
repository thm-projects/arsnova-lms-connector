package de.thm.arsnova.connector.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
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
import org.springframework.security.core.token.Sha512DigestUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.thm.arsnova.connector.config.RepositoryTestConfig;
import de.thm.arsnova.connector.persistence.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration( classes = {RepositoryTestConfig.class} )
public class InternalUserServiceTest {

	@Autowired
	private InternalUserService internalUserService;

	@Autowired
	private DataSource dataSource;

	@Before
	public void initDatabase() {
		try {
			Connection con = dataSource.getConnection();
			IDatabaseConnection connection = new DatabaseConnection(con);
			DatabaseOperation.CLEAN_INSERT.execute(connection, getDataSet());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@After
	public void cleanupDatabase() {
	}

	private IDataSet getDataSet() throws Exception {
		FileInputStream fis = new FileInputStream(new File(
				"src/test/resources/dbunit/internaldb.xml"));
		return new XmlDataSet(fis);
	}

	@Test
	public void testShouldIndicateAdminUser() {
		User actual = internalUserService.getUser("itsr00");
		assertTrue(actual.isAdmin());
	}

	@Test
	public void testShouldIndicateNonAdminUser() {
		User actual = internalUserService.getUser("itsr01");
		assertFalse(actual.isAdmin());
	}

	@Test
	public void testShouldSaveAndReturnUser() {
		User expected = new User();
		expected.setUserId("ptsr00");
		expected.setPassword("secret");
		expected.setAdmin(true);
		internalUserService.saveUser(expected);

		User actual = internalUserService.getUser("ptsr00");

		assertEquals(expected, actual);
	}

	@Test
	public void testShouldHashPasswordOnFirstSave() {
		User expected = new User();
		expected.setUserId("ptsr01");
		expected.setPassword("secret");
		expected.setAdmin(true);
		internalUserService.saveUser(expected);

		User actual = internalUserService.getUser("ptsr01");

		assertNotEquals("secret", actual.getPassword());

		String expectedPassword = "{SHA512}" + Sha512DigestUtils.shaHex("secret");
		internalUserService.unmakeAdmin("ptsr01");

		assertEquals(expectedPassword, internalUserService.getUser("ptsr01").getPassword());
	}
}
