package de.thm.arsnova.connector.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import de.thm.arsnova.connector.model.Course;
import de.thm.arsnova.connector.model.Membership;
import de.thm.arsnova.connector.model.UserRole;

public class StudipConnectorDaoImpl implements ConnectorDao {

	private static final String TYPE = "studip";
	private static final String STUDIP_COURSE_DOZENT = "dozent";

	@Autowired
	private DataSource dataSource;

	public List<String> getCourseUsers(final String courseid) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.query(
				"SELECT seminar_user.user_id FROM seminar_user "
				+ "JOIN auth_user_md5 ON (seminar_user.user_id = auth_user_md5.user_id) "
				+ "WHERE seminar_user.Seminar_ID; = ?",
				new String[] {courseid},
				new RowMapper<String>() {
					public String mapRow(ResultSet resultSet, int row) throws SQLException {
						return resultSet.getString("username");
					}
				}
		);
	}

	public Membership getMembership(final String username, final String courseid) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		List<Membership> results = jdbcTemplate.query(
				"SELECT seminar_user.status FROM seminar_user "
				+ "JOIN auth_user_md5 ON (seminar_user.user_id = auth_user_md5.user_id) "
				+ "WHERE seminar_user.Seminar_ID = ? AND auth_user_md5.username = ?;",
				new String[] {courseid, username},
				new RowMapper<Membership>() {
					public Membership mapRow(ResultSet resultSet, int row) throws SQLException {
						Membership membership = new Membership();
						if (resultSet.wasNull()) {
							membership.setMember(false);
							return membership;
						}
						membership.setMember(true);
						membership.setUserrole(getMembershipRole(resultSet.getString("status")));
						return membership;
					}
				}
		);
		if (results.size() != 1) {
			return new Membership();
		}
		return results.get(0);
	}

	public List<Course> getMembersCourses(final String username) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.query(
				"SELECT seminare.Seminar_ID, seminare.Name, seminare.Untertitel, seminar_user.status FROM seminare "
				+ "JOIN seminar_user ON (seminar_user.seminar_id = seminare.seminar_id) "
				+ "JOIN auth_user_md5 ON (seminar_user.user_id = auth_user_md5.user_id) "
				+ "WHERE auth_user_md5.username = ?;",
				new String[] {username},
				new RowMapper<Course>() {
					public Course mapRow(ResultSet resultSet, int row) throws SQLException {
						Course course = new Course();
						course.setId(resultSet.getString("Seminar_ID"));
						course.setFullname(resultSet.getString("Name"));
						course.setShortname(resultSet.getString("Name"));
						course.setType(TYPE);
						course.setMembership(getMembership(username, resultSet.getString("Seminar_ID")));
						return course;
					}
				}
		);
	}

	private UserRole getMembershipRole(final String roleId) {
		if (STUDIP_COURSE_DOZENT.equals(roleId)) {
			return UserRole.CREATOR;
		}

		// User is course member, may be with right to manage the course
		return UserRole.MEMBER;
	}
}
