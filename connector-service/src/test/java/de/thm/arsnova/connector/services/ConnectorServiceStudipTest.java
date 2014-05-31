package de.thm.arsnova.connector.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.thm.arsnova.connector.config.RepositoryTestConfig;
import de.thm.arsnova.connector.config.StudipTestConfig;
import de.thm.arsnova.connector.model.Courses;
import de.thm.arsnova.connector.model.Membership;
import de.thm.arsnova.connector.model.UserRole;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration( classes = {StudipTestConfig.class, RepositoryTestConfig.class} )
public class ConnectorServiceStudipTest {

	@Autowired
	private ConnectorService connectorService;

	@Autowired
	private DataSource dataSource;

	@Before
	public void initDatabase() {
		final JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		createTables(jdbcTemplate);
		try {
			final Connection con = dataSource.getConnection();
			final IDatabaseConnection connection = new DatabaseConnection(con);
			DatabaseOperation.CLEAN_INSERT.execute(connection, getDataSet());
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private void createTables(final JdbcTemplate jdbcTemplate) {
		jdbcTemplate.execute(
				"CREATE TABLE seminare ("
						+ "Seminar_ID varchar(254) NOT NULL,"
						+ "Name varchar(254),"
						+ "Untertitel varchar(254),"
						+ "PRIMARY KEY (Seminar_ID))"
				);

		jdbcTemplate.execute(
				"CREATE TABLE auth_user_md5 ("
						+ "user_id varchar(254) NOT NULL,"
						+ "username varchar(254),"
						+ "PRIMARY KEY (user_id))"
				);

		jdbcTemplate.execute(
				"CREATE TABLE seminar_user ("
						+ "user_id varchar(254) NOT NULL,"
						+ "Seminar_id varchar(254) NOT NULL,"
						+ "status varchar(254),"
						+ "PRIMARY KEY (user_id, Seminar_id))"
				);
	}

	private IDataSet getDataSet() throws Exception {
		final FileInputStream fis = new FileInputStream(new File("src/test/resources/dbunit/studip-datasource.xml"));
		return new XmlDataSet(fis);
	}

	@After
	public void cleanupDatabase() {
		final JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		jdbcTemplate.execute("DROP TABLE seminare");
		jdbcTemplate.execute("DROP TABLE auth_user_md5");
		jdbcTemplate.execute("DROP TABLE seminar_user");
	}

	@Test
	public void testShouldNotReturnCourseForNotEnroledUser() {
		final Courses courses = connectorService.getCourses("ptsr01");
		final int actual = courses.getCourse().size();
		final int expected = 0;

		assertEquals(expected, actual);
	}

	@Test
	public void testShouldReturnCourseForEnroledUser() {
		final Courses courses = connectorService.getCourses("ptsr00");
		final int actual = courses.getCourse().size();
		final int expected = 1;

		assertEquals(expected, actual);
	}

	@Test
	public void testShouldReturnMembershipForEnroledUser() {
		final Membership membership = connectorService.getMembership("ptsr00", "2");

		assertTrue(membership.isMember());
		assertEquals(UserRole.MEMBER, membership.getUserrole());
	}

	@Test
	public void testShouldNotReturnCoursesForNonexistantUsers() {
		final Courses courses = connectorService.getCourses("iamnothere");
		final int actual = courses.getCourse().size();
		final int expected = 0;

		assertEquals(expected, actual);
	}

	@Test
	public void testShouldIndicateAnEnroledUser() {
		final Membership membership = connectorService.getMembership("admin", "1");
		assertTrue(membership.isMember());
		assertNotNull(membership.getUserrole());
	}

	@Test
	public void testShouldNotIndicateUnenroledUsers() {
		final Membership membership = connectorService.getMembership("ptsr01", "1");
		assertFalse(membership.isMember());
		assertNull(membership.getUserrole());
	}

	@Test
	public void testShouldReturnFalseOnUnknownCourse() {
		final Membership membership = connectorService.getMembership("ptsr00", "12345678");
		assertFalse(membership.isMember());
	}

	@Test
	public void testShouldReturnFalseOnUnknownUser() {
		final Membership membership = connectorService.getMembership("iamnothere", "1");
		assertFalse(membership.isMember());
	}

	@Test
	public void testShouldReturnCorrectCourseType() {
		final Courses courses = connectorService.getCourses("ptsr00");
		final String actual = courses.getCourse().get(0).getType();
		final String expected = "studip";

		assertEquals(expected, actual);
	}

	@Test
	public void testShouldReturnCorrectCourseData() {
		final Courses courses = connectorService.getCourses("ptsr00");
		String actual = courses.getCourse().get(0).getShortname();
		String expected = "Testkurs 2";
		assertEquals(expected, actual);

		actual = courses.getCourse().get(0).getFullname();
		expected = "Testkurs 2";
		assertEquals(expected, actual);
	}

	@Test
	public void testShouldReturnCorrectCourseMembership() {
		final Courses courses = connectorService.getCourses("admin");
		final Membership actual = courses.getCourse().get(0).getMembership();

		assertTrue(actual.isMember());
		assertEquals(UserRole.TEACHER, actual.getUserrole());
	}
}