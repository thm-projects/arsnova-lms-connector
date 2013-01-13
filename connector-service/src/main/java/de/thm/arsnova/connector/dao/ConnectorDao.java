package de.thm.arsnova.connector.dao;

import java.util.List;

import de.thm.arsnova.connector.model.Course;

public interface ConnectorDao {
	List<String> getCourseUsers(String courseid);
	List<Course> getTeachersCourses(String username);
}
