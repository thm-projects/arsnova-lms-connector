package de.thm.arsnova.connector.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import javax.sql.DataSource;

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
