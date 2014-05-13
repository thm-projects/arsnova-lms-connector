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

import de.thm.arsnova.connector.config.MoodleTestConfig;
import de.thm.arsnova.connector.config.RepositoryTestConfig;
import de.thm.arsnova.connector.model.Courses;
import de.thm.arsnova.connector.model.Membership;
import de.thm.arsnova.connector.model.UserRole;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration( classes = {MoodleTestConfig.class, RepositoryTestConfig.class} )
public class ConnectorServiceMoodleTest {

	@Autowired
	private ConnectorService connectorService;

	@Autowired
	private DataSource dataSource;

	@Before
	public void initDatabase() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		createTables(jdbcTemplate);
		try {
			Connection con = dataSource.getConnection();
			IDatabaseConnection connection = new DatabaseConnection(con);
			DatabaseOperation.CLEAN_INSERT.execute(connection, getDataSet());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createTables(JdbcTemplate jdbcTemplate) {
		jdbcTemplate.execute(
				"CREATE TABLE mdl_course ("
						+ "id bigint NOT NULL,"
						+ "fullname varchar(254),"
						+ "shortname varchar(254),"
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
		FileInputStream fis = new FileInputStream(new File("src/test/resources/dbunit/moodle-datasource.xml"));
		return new XmlDataSet(fis);
	}

	@After
	public void cleanupDatabase() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		jdbcTemplate.execute("DROP TABLE mdl_course");
		jdbcTemplate.execute("DROP TABLE mdl_user");
		jdbcTemplate.execute("DROP TABLE mdl_enrol");
		jdbcTemplate.execute("DROP TABLE mdl_user_enrolments");
		jdbcTemplate.execute("DROP TABLE mdl_role_assignments");
		jdbcTemplate.execute("DROP TABLE mdl_context");
	}

	@Test
	public void testShouldNotReturnCourseForNotEnroledUser() {
		Courses courses = connectorService.getCourses("admin");
		int actual = courses.getCourse().size();
		int expected = 0;

		assertEquals(expected, actual);
	}

	@Test
	public void testShouldReturnCoursesForEnroledUser() {
		Courses courses = connectorService.getCourses("ptsr00");
		int actual = courses.getCourse().size();
		int expected = 1;

		assertEquals(expected, actual);
	}

	@Test
	public void testShouldReturnMembershipForEnroledUser() {
		Membership membership = connectorService.getMembership("ptsr00", "1");

		assertTrue(membership.isMember());
		assertEquals(UserRole.TEACHER, membership.getUserrole());
	}

	@Test
	public void testShouldNotReturnCoursesForNonexistantUsers() {
		Courses courses = connectorService.getCourses("iamnothere");
		int actual = courses.getCourse().size();
		int expected = 0;

		assertEquals(expected, actual);
	}

	@Test
	public void testShouldIndicateAnEnroledUser() {
		Membership membership = connectorService.getMembership("ptsr00", "1");
		assertTrue(membership.isMember());
		assertNotNull(membership.getUserrole());
	}

	@Test
	public void testShouldNotIndicateUnenroledUsers() {
		Membership membership = connectorService.getMembership("ptsr01", "1");
		assertFalse(membership.isMember());
		assertNull(membership.getUserrole());
	}

	@Test
	public void testShouldReturnFalseOnUnknownCourse() {
		Membership membership = connectorService.getMembership("ptsr00", "12345678");
		assertFalse(membership.isMember());
	}

	@Test
	public void testShouldReturnFalseOnUnknownUser() {
		Membership membership = connectorService.getMembership("iamnothere", "1");
		assertFalse(membership.isMember());
	}

	@Test
	public void testShouldReturnCorrectCourseType() {
		Courses courses = connectorService.getCourses("ptsr00");
		String actual = courses.getCourse().get(0).getType();
		String expected = "moodle";

		assertEquals(expected, actual);
	}

	@Test
	public void testShouldReturnCorrectCourseData() {
		Courses courses = connectorService.getCourses("ptsr00");
		String actual = courses.getCourse().get(0).getShortname();
		String expected = "TK1";
		assertEquals(expected, actual);

		actual = courses.getCourse().get(0).getFullname();
		expected = "Testkurs 1";
		assertEquals(expected, actual);
	}

	@Test
	public void testShouldReturnCorrectCourseMembershipForTeacher() {
		Courses courses = connectorService.getCourses("ptsr00");
		Membership actual = courses.getCourse().get(0).getMembership();

		assertTrue(actual.isMember());
		assertEquals(UserRole.TEACHER, actual.getUserrole());
	}

	@Test
	public void testShouldReturnCorrectCourseMembershipForMember() {
		Courses courses = connectorService.getCourses("ptsr02");
		Membership actual = courses.getCourse().get(0).getMembership();

		assertTrue(actual.isMember());
		assertEquals(UserRole.MEMBER, actual.getUserrole());
	}

	@Test
	public void testShouldNotIndicateEnroledUserButInvisibleCourse() {
		Membership membership = connectorService.getMembership("ptsr02", "4");
		assertFalse(membership.isMember());
		assertNull(membership.getUserrole());
	}
}