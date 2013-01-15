package de.thm.arsnova.connector.dao;

import java.util.List;

import de.thm.arsnova.connector.model.Course;
import de.thm.arsnova.connector.model.Membership;

public interface ConnectorDao {
	List<String> getCourseUsers(String courseid);
	List<Course> getMembersCourses(String username);
	Membership getMembership(String username, String courseid);
}
