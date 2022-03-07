package net.particify.arsnova.connector.services;

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

import net.particify.arsnova.connector.config.MoodleTestConfig;
import net.particify.arsnova.connector.config.RepositoryTestConfig;
import net.particify.arsnova.connector.model.Courses;
import net.particify.arsnova.connector.model.Membership;
import net.particify.arsnova.connector.model.UserRole;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration( classes = {MoodleTestConfig.class, RepositoryTestConfig.class} )
public class ConnectorServiceMoodleTest {

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
				"CREATE TABLE mdl_course ("
						+ "id bigint NOT NULL,"
						+ "fullname varchar(254),"
						+ "shortname varchar(254),"
						+ "startdate bigint,"
						+ "enddate bigint,"
						+ "visible bigint,"
						+ "PRIMARY KEY (id))"
				);

		jdbcTemplate.execute(
				"CREATE TABLE mdl_user ("
						+ "id bigint NOT NULL,"
						+ "username varchar(254),"
						+ "PRIMARY KEY (id))"
				);

		jdbcTemplate.execute(
				"CREATE TABLE mdl_enrol ("
						+ "id bigint NOT NULL,"
						+ "enrol bigint,"
						+ "courseid bigint,"
						+ "roleid bigint,"
						+ "PRIMARY KEY (id))"
				);

		jdbcTemplate.execute(
				"CREATE TABLE mdl_user_enrolments ("
						+ "id bigint NOT NULL,"
						+ "enrolid bigint,"
						+ "userid bigint,"
						+ "PRIMARY KEY (id))"
				);

		jdbcTemplate.execute(
				"CREATE TABLE mdl_role_assignments ("
						+ "id bigint NOT NULL,"
						+ "userid bigint,"
						+ "roleid bigint,"
						+ "contextid bigint,"
						+ "PRIMARY KEY (id))"
				);

		jdbcTemplate.execute(
				"CREATE TABLE mdl_context ("
						+ "id bigint NOT NULL,"
						+ "instanceid bigint,"
						+ "contextlevel bigint,"
						+ "PRIMARY KEY (id))"
				);
	}

	private IDataSet getDataSet() throws Exception {
		final FileInputStream fis = new FileInputStream(new File("src/test/resources/dbunit/moodle-datasource.xml"));
		return new XmlDataSet(fis);
	}

	@After
	public void cleanupDatabase() {
		final JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		jdbcTemplate.execute("DROP TABLE mdl_course");
		jdbcTemplate.execute("DROP TABLE mdl_user");
		jdbcTemplate.execute("DROP TABLE mdl_enrol");
		jdbcTemplate.execute("DROP TABLE mdl_user_enrolments");
		jdbcTemplate.execute("DROP TABLE mdl_role_assignments");
		jdbcTemplate.execute("DROP TABLE mdl_context");
	}

	@Test
	public void testShouldNotReturnCourseForNotEnroledUser() {
		final Courses courses = connectorService.getCourses("admin");
		final int actual = courses.getCourse().size();
		final int expected = 0;

		assertEquals(expected, actual);
	}

	@Test
	public void testShouldReturnCoursesForEnroledUser() {
		final Courses courses = connectorService.getCourses("ptsr00");
		final int actual = courses.getCourse().size();
		final int expected = 1;

		assertEquals(expected, actual);
	}

	@Test
	public void testShouldReturnMembershipForEnroledUser() {
		final Membership membership = connectorService.getMembership("ptsr00", "1");

		assertTrue(membership.isMember());
		assertEquals(UserRole.TEACHER, membership.getUserrole());
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
		final Membership membership = connectorService.getMembership("ptsr00", "1");
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
		final String expected = "moodle";

		assertEquals(expected, actual);
	}

	@Test
	public void testShouldReturnCorrectCourseData() {
		final Courses courses = connectorService.getCourses("ptsr00");
		String actual = courses.getCourse().get(0).getShortname();
		String expected = "TK1";
		assertEquals(expected, actual);

		actual = courses.getCourse().get(0).getFullname();
		expected = "Testkurs 1";
		assertEquals(expected, actual);
	}

	@Test
	public void testShouldReturnCorrectCourseMembershipForTeacher() {
		final Courses courses = connectorService.getCourses("ptsr00");
		final Membership actual = courses.getCourse().get(0).getMembership();

		assertTrue(actual.isMember());
		assertEquals(UserRole.TEACHER, actual.getUserrole());
	}

	@Test
	public void testShouldReturnCorrectCourseMembershipForMember() {
		final Courses courses = connectorService.getCourses("ptsr02");
		final Membership actual = courses.getCourse().get(0).getMembership();

		assertTrue(actual.isMember());
		assertEquals(UserRole.MEMBER, actual.getUserrole());
	}

	@Test
	public void testShouldNotIndicateEnroledUserButInvisibleCourse() {
		final Membership membership = connectorService.getMembership("ptsr02", "4");
		assertFalse(membership.isMember());
		assertNull(membership.getUserrole());
	}
}