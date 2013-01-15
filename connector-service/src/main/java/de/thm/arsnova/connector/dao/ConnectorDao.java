package de.thm.arsnova.connector.dao;

import java.util.List;

import de.thm.arsnova.connector.model.Course;
import de.thm.arsnova.connector.model.Membership;

public interface ConnectorDao {
	static final String ROLE_CREATOR = "creator";
	static final String ROLE_MEMBER = "member";
	static final String ROLE_OTHER = "other";

	List<String> getCourseUsers(String courseid);
	List<Course> getMembersCourses(String username);
	Membership getMembership(String username, String courseid);
}
