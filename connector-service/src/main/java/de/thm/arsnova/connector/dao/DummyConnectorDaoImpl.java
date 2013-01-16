package de.thm.arsnova.connector.dao;

import java.util.ArrayList;
import java.util.List;

import de.thm.arsnova.connector.model.Course;
import de.thm.arsnova.connector.model.Membership;
import de.thm.arsnova.connector.model.UserRole;

public class DummyConnectorDaoImpl implements ConnectorDao {

	private static final String TYPE = "dummy";

	public List<String> getCourseUsers(String courseid) {
		List<String> result = new ArrayList<String>();
		result.add("test");
		return result;
	}

	public List<Course> getMembersCourses(String username) {
		List<Course> result = new ArrayList<Course>();
		Course course = new Course();
		course.setFullname("Dummy Course");
		course.setId("1");
		course.setType(TYPE);
		result.add(course);
		return result;
	}

	@Override
	public Membership getMembership(String username, String courseid) {
		Membership membership = new Membership();
		membership.setMember(true);
		membership.setUserrole(UserRole.MEMBER);
		return membership;
	}
}
