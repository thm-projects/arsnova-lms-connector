package de.thm.arsnova.connector.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import de.thm.arsnova.connector.model.Course;
import de.thm.arsnova.connector.model.Membership;
import de.thm.arsnova.connector.model.UserRole;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class StudipConnectorDaoImpl implements ConnectorDao {
	private static final String TYPE = "studip";
	private static final String STUDIP_COURSE_DOZENT = "dozent";

	private static final Logger logger = LoggerFactory.getLogger(StudipConnectorDaoImpl.class);

	@Autowired
	private DataSource dataSource;

	@Override
	public List<String> getCourseUsers(final String courseid) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.query(
				"SELECT seminar_user.user_id FROM seminar_user "
						+ "JOIN auth_user_md5 ON (seminar_user.user_id = auth_user_md5.user_id) "
						+ "WHERE seminar_user.Seminar_ID = ?;",
						new String[] {courseid},
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

		final NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		final Map<String, Object> jdbcParams = new HashMap<>();
		jdbcParams.put("username", username);
		jdbcParams.put("courseIds", courseIds);
		logger.debug("Querying Stud.IP statuses of user {} for course IDs {}.", username, courseIds);
		try {
			final Map<String, Membership> memberships = jdbcTemplate.query(
					"SELECT seminar_user.Seminar_ID, seminar_user.status FROM seminar_user "
							+ "JOIN auth_user_md5 ON (seminar_user.user_id = auth_user_md5.user_id) "
							+ "WHERE seminar_user.Seminar_ID IN (:courseIds) AND auth_user_md5.username = :username;",
					jdbcParams,
					new ResultSetExtractor<Map<String, Membership>>() {
						@Override
						public Map<String, Membership> extractData(final ResultSet rs) throws SQLException {
							final Map<String, Membership> memberships = new HashMap<>();
							while (rs.next()) {
								final Membership membership = new Membership();
								final String status = rs.getString("status");
								membership.setMember(true);
								membership.setUserrole(getMembershipRole(status));
								memberships.put(rs.getString("Seminar_ID"), membership);
								logger.trace("Resolved Stud.IP status {} to {}.", status, membership.getUserrole());
							}

							return memberships;
						}
					}
			);

			return memberships;
		} catch (final DataAccessException e) {
			logger.error("Could not retrieve memberships from Stud.IP database.", e);
			return Collections.emptyMap();
		}
	}

	@Override
	public List<Course> getMembersCourses(final String username) {
		final JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		logger.debug("Querying Stud.IP courses for user {}.", username);
		try {
			final List<Course> courses =  jdbcTemplate.query(
					"SELECT seminare.Seminar_ID, seminare.Name, seminare.Untertitel, seminare.start_time, seminare.duration_time, seminar_user.status FROM seminare "
							+ "JOIN seminar_user ON (seminar_user.seminar_id = seminare.seminar_id) "
							+ "JOIN auth_user_md5 ON (seminar_user.user_id = auth_user_md5.user_id) "
							+ "WHERE auth_user_md5.username = ?;",
					new String[] {username},
					new RowMapper<Course>() {
						@Override
						public Course mapRow(final ResultSet rs, final int row) throws SQLException {
							final Course course = new Course();
							course.setId(rs.getString("Seminar_ID"));
							course.setFullname(rs.getString("Name"));
							course.setShortname(rs.getString("Name"));
							course.setStartdate(Instant.ofEpochSecond(rs.getLong("start_time")));
							course.setType(TYPE);

							final long duration = rs.getLong("duration_time");
							if (duration == 0) {
								/* The course is active for one term. A term's duration is approximated to 180 days
								 * here. This might be incorrect if the Stud.IP's configuration for terms was changed
								 * manually. */
								course.setEnddate(course.getStartdate().plus(6 * 30, ChronoUnit.DAYS));
							} else if (duration > 0) {
								course.setEnddate(course.getStartdate().plusSeconds(duration));
							}

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
			logger.error("Could not retrieve courses from Stud.IP database.", e);
			return Collections.emptyList();
		}
	}

	private UserRole getMembershipRole(final String roleId) {
		if (STUDIP_COURSE_DOZENT.equals(roleId)) {
			return UserRole.TEACHER;
		}

		// User is course member, may be with right to manage the course
		return UserRole.MEMBER;
	}
}
