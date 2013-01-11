package de.thm.arsnova.connector.moodle.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

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

import de.thm.arsnova.connector.moodle.model.Courses;
import de.thm.arsnova.connector.moodle.model.Membership;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/spring-test.xml" })
public class CourseMemberServiceTest {

	@Autowired
	private CourseMemberService courseMemberService;

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
				+ "PRIMARY KEY (id))"
		);

		jdbcTemplate.execute(
				"CREATE TABLE mdl_user_enrolments ("
				+ "id bigint NOT NULL,"
				+ "enrolid bigint,"
				+ "userid bigint,"
				+ "PRIMARY KEY (id))"
		);
	}

	private IDataSet getDataSet() throws Exception {
		FileInputStream fis = new FileInputStream(new File("src/test/resources/dbunit/datasource.xml"));
		return new XmlDataSet(fis);
	}

	@After
	public void cleanupDatabase() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		jdbcTemplate.execute("DROP TABLE mdl_course");
		jdbcTemplate.execute("DROP TABLE mdl_user");
		jdbcTemplate.execute("DROP TABLE mdl_enrol");
		jdbcTemplate.execute("DROP TABLE mdl_user_enrolments");
	}

	@Test
	public void testShouldNotReturnCourseForNotEnroledUser() {
		Courses courses = courseMemberService.getCourses("ptsr00");
		int actual = courses.getCourse().size();
		int expected = 0;

		assertEquals(expected, actual);
	}
	
	@Test
	public void testShouldReturnCourseForEnroledUser() {
		Courses courses = courseMemberService.getCourses("admin");
		int actual = courses.getCourse().size();
		int expected = 2;

		assertEquals(expected, actual);
	}
	
	@Test
	public void testShouldNotReturnCoursesForNonexistantUsers() {
		Courses courses = courseMemberService.getCourses("iamnothere");
		int actual = courses.getCourse().size();
		int expected = 0;

		assertEquals(expected, actual);
	}

	@Test
	public void testShouldIndicateAnEnroledUser() {
		Membership membership = courseMemberService.ismember("admin", "1");
		assertTrue(membership.isIsmember());
	}
	
	@Test
	public void testShouldNotIndicateUnenroledUsers() {
		Membership membership = courseMemberService.ismember("pcvl72", "1");
		assertFalse(membership.isIsmember());
	}
	
	@Test
	public void testShouldReturnFalseOnUnknownCourse() {
		Membership membership = courseMemberService.ismember("pcvl72", "12345678");
		assertFalse(membership.isIsmember());
	}
	
	@Test
	public void testShouldReturnFalseOnUnknownUser() {
		Membership membership = courseMemberService.ismember("iamnothere", "1");
		assertFalse(membership.isIsmember());
	}

	@Test
	public void testShouldReturnCorrectCourseType() {
		Courses courses = courseMemberService.getCourses("admin");
		String actual = courses.getCourse().get(0).getType();
		String expected = "moodle";

		assertEquals(expected, actual);
	}
}