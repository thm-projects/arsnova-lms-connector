package de.thm.arsnova.connector.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.thm.arsnova.connector.config.DummyTestConfig;
import de.thm.arsnova.connector.config.RepositoryTestConfig;
import de.thm.arsnova.connector.model.Courses;
import de.thm.arsnova.connector.model.Membership;
import de.thm.arsnova.connector.model.UserRole;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration( classes = {DummyTestConfig.class, RepositoryTestConfig.class} )
public class ConnectorServiceDummyTest {

	@Autowired
	private ConnectorService connectorService;

	@Test
	public void testShouldReturnCourseForEnroledUser() {
		final Courses courses = connectorService.getCourses("ptsr00");
		final int actual = courses.getCourse().size();
		final int expected = 1;
		assertEquals(expected, actual);
	}

	@Test
	public void testShouldReturnMembership() {
		final Membership membership = connectorService.getMembership("ptsr01", "1");
		assertTrue(membership.isMember());
		assertEquals(UserRole.MEMBER, membership.getUserrole());
	}
}