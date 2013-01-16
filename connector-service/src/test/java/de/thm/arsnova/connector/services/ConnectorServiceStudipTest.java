package de.thm.arsnova.connector.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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

import de.thm.arsnova.connector.model.Courses;
import de.thm.arsnova.connector.model.Membership;
import de.thm.arsnova.connector.model.UserRole;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/spring-test-studip.xml" })
public class ConnectorServiceStudipTest {

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
		FileInputStream fis = new FileInputStream(new File("src/test/resources/dbunit/studip-datasource.xml"));
		return new XmlDataSet(fis);
	}

	@After
	public void cleanupDatabase() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		jdbcTemplate.execute("DROP TABLE seminare");
		jdbcTemplate.execute("DROP TABLE auth_user_md5");
		jdbcTemplate.execute("DROP TABLE seminar_user");
	}

	@Test
	public void testShouldNotReturnCourseForNotEnroledUser() {
		Courses courses = connectorService.getCourses("ptsr01");
		int actual = courses.getCourse().size();
		int expected = 0;

		assertEquals(expected, actual);
	}
	
	@Test
	public void testShouldReturnCourseForEnroledUser() {
		Courses courses = connectorService.getCourses("ptsr00");
		int actual = courses.getCourse().size();
		int expected = 1;

		assertEquals(expected, actual);
	}

	@Test
	public void testShouldReturnMembershipForEnroledUser() {
		Membership membership = connectorService.getMembership("ptsr00", "2");

		assertTrue(membership.isMember());
		assertEquals(UserRole.MEMBER, membership.getUserrole());
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
		Membership membership = connectorService.getMembership("admin", "1");
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
		String expected = "studip";

		assertEquals(expected, actual);
	}

	@Test
	public void testShouldReturnCorrectCourseData() {
		Courses courses = connectorService.getCourses("ptsr00");
		String actual = courses.getCourse().get(0).getShortname();
		String expected = "Testkurs 2";
		assertEquals(expected, actual);

		actual = courses.getCourse().get(0).getFullname();
		expected = "Testkurs 2";
		assertEquals(expected, actual);
	}

	@Test
	public void testShouldReturnCorrectCourseMembership() {
		Courses courses = connectorService.getCourses("admin");
		Membership actual = courses.getCourse().get(0).getMembership();

		assertTrue(actual.isMember());
		assertEquals(UserRole.CREATOR, actual.getUserrole());
	}
}