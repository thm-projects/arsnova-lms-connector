package de.thm.arsnova.connector.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import de.thm.arsnova.connector.model.Course;
import de.thm.arsnova.connector.model.Membership;
import de.thm.arsnova.connector.model.UserRole;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;

public class MoodleConnectorDaoImpl implements ConnectorDao {
	private static final String TYPE = "moodle";

	private static final Logger logger = LoggerFactory.getLogger(MoodleConnectorDaoImpl.class);

	@Value("${lms.moodle.teacher-role-ids:3,4}") private int[] moodleTeacherRoleIds;
	@Value("${lms.moodle.student-role-ids:5}") private int[] moodleStudentRoleIds;

	@Autowired
	private DataSource dataSource;

	@Override
	public List<String> getCourseUsers(final String courseid) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.query(
				"SELECT username FROM mdl_user "
						+ "JOIN mdl_user_enrolments ON (mdl_user.id = mdl_user_enrolments.userid) "
						+ "JOIN mdl_enrol ON (mdl_user_enrolments.enrolid = mdl_enrol.id) "
						+ "WHERE mdl_enrol.courseid = ?;",
						new Object[] {Long.valueOf(courseid)},
						new RowMapper<String>() {
							@Override
							public String mapRow(ResultSet resultSet, int row) throws SQLException {
								return resultSet.getString("username");
							}
						}
				);
	}

	@Override
	public Membership getMembership(final String username, final String courseid) {
		return getMemberships(username, Collections.singletonList(courseid)).getOrDefault(courseid, new Membership());
	}

	public Map<String, Membership> getMemberships(final String username, final List<String> courseIds) {
		if (courseIds.isEmpty()) {
			return Collections.emptyMap();
		}

		try {
			final long userId = getUserId(username);
			final SqlParameterSource[] courseIdParamList = SqlParameterSourceUtils.createBatch(courseIds);
			final NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
			final Map<String, Object> jdbcParams = new HashMap<>();
			jdbcParams.put("userId", userId);
			jdbcParams.put("courseIds", courseIds.stream().map(id -> Long.valueOf(id)).collect(Collectors.toList()));
			final Map<String, Membership> memberships = jdbcTemplate.query(
					"SELECT mdl_course.id, mdl_user_enrolments.userid, mdl_role_assignments.roleid FROM mdl_enrol "
							+ "JOIN mdl_user_enrolments ON (mdl_enrol.id = mdl_user_enrolments.enrolid) "
							+ "JOIN mdl_role_assignments ON (mdl_role_assignments.userid = mdl_user_enrolments.userid) "
							+ "JOIN mdl_context ON (mdl_context.instanceid = mdl_enrol.courseid "
							+ "AND mdl_context.id = mdl_role_assignments.contextid) "
							+ "JOIN mdl_course ON (mdl_course.id = mdl_context.instanceid "
							+ "AND mdl_course.visible = 1) "
							+ "WHERE mdl_context.contextlevel = 50 "
							+ "AND mdl_enrol.courseid IN (:courseIds) "
							+ "AND mdl_user_enrolments.userid = :userId;",
					jdbcParams,
					new ResultSetExtractor<Map<String, Membership>>() {
						@Override
						public Map<String, Membership> extractData(final ResultSet rs) throws SQLException {
							final Map<String, Membership> memberships = new HashMap<>();
							while (rs.next()) {
								final Membership membership = new Membership();
								membership.setMember(true);
								membership.setUserrole(getMembershipRole(rs.getInt("roleid")));
								memberships.put(String.valueOf(rs.getLong("id")), membership);
							}

							return memberships;
						}
					}
			);

			return memberships;
		} catch (final DataAccessException e) {
			logger.error("Could not retrieve memberships from Moodle database.", e);
			return Collections.emptyMap();
		}
	}

	@Override
	public List<Course> getMembersCourses(final String username) {
		try {
			final long userId = getUserId(username);
			final JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			final List<Course> courses = jdbcTemplate.query(
					"SELECT mdl_course.id, mdl_course.fullname, mdl_course.shortname FROM mdl_course "
							+ "JOIN mdl_enrol ON (mdl_enrol.courseid = mdl_course.id) "
							+ "JOIN mdl_user_enrolments ON (mdl_enrol.id = mdl_user_enrolments.enrolid) "
							+ "WHERE mdl_course.visible = 1 AND mdl_user_enrolments.userid = ?;",
					new Long[] {userId},
					new RowMapper<Course>() {
						@Override
						public Course mapRow(final ResultSet rs, final int row) throws SQLException {
							final Course course = new Course();
							course.setId(String.valueOf(rs.getLong("id")));
							course.setFullname(rs.getString("fullname"));
							course.setShortname(rs.getString("shortname"));
							course.setType(TYPE);

							return course;
						}
					}
			);
			final List<String> courseIds = courses.stream().map(Course::getId).collect(Collectors.toList());
			final Map<String, Membership> memberships = getMemberships(username, courseIds);
			for (final Course course : courses) {
				course.setMembership(memberships.get(course.getId()));
			}

			return courses;
		} catch (final DataAccessException e) {
			logger.error("Could not retrieve courses from Moodle database.", e);
			return Collections.emptyList();
		}
	}

	private long getUserId(final String username) throws EmptyResultDataAccessException {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.queryForObject(
				"SELECT mdl_user.id FROM mdl_user "
						+ "WHERE mdl_user.username = ?;",
				new String[] {username},
				new RowMapper<Long>() {
					@Override
					public Long mapRow(ResultSet resultSet, int row) throws SQLException {
						return resultSet.getLong("id");
					}
				}
		);
	}

	private UserRole getMembershipRole(final int moodleRoleId) {
		if (Arrays.stream(moodleTeacherRoleIds).anyMatch(id -> id == moodleRoleId)) {
			return UserRole.TEACHER;
		} else if (Arrays.stream(moodleStudentRoleIds).anyMatch(id -> id == moodleRoleId)) {
			return UserRole.MEMBER;
		}

		// User is course guest
		return UserRole.OTHER;
	}
}
