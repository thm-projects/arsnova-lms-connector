package net.particify.arsnova.connector.dao;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import net.particify.arsnova.connector.model.Course;
import net.particify.arsnova.connector.model.Membership;
import net.particify.arsnova.connector.model.UserRole;

public class DummyConnectorDaoImpl implements ConnectorDao {

	private static final String TYPE = "dummy";

	@Override
	public List<String> getCourseUsers(final String courseid) {
		final List<String> result = new ArrayList<String>();
		result.add("test");
		return result;
	}

	@Override
	public List<Course> getMembersCourses(final String username) {
		final List<Course> result = new ArrayList<Course>();
		if (TYPE.equals(username)) {
			return result;
		}
		final Course course = new Course();
		course.setFullname("Dummy Course");
		course.setId("1");
		course.setStartdate(Instant.EPOCH);
		course.setEnddate(Instant.EPOCH.plus(180, ChronoUnit.DAYS));
		course.setType(TYPE);
		result.add(course);
		return result;
	}

	@Override
	public Membership getMembership(final String username, final String courseid) {
		final Membership membership = new Membership();
		membership.setMember(!TYPE.equals(username));
		if (!TYPE.equals(username)) {
			membership.setUserrole(UserRole.MEMBER);
		}
		return membership;
	}
}
