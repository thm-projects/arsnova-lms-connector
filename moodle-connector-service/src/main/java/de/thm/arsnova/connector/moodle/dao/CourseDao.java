package de.thm.arsnova.connector.moodle.dao;

import java.util.List;

import de.thm.arsnova.connector.moodle.model.Course;

public interface CourseDao {
	List<String> getCourseUsers(String courseid);
	List<Course> getTeachersCourses(String username);
}
