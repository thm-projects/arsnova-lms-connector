package de.thm.arsnova.connector.moodle.dao;

import java.util.List;

public interface CourseDao {
	List<String> getCourseUsers(String courseid);
}