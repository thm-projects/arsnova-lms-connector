package de.thm.arsnova.connector.moodle.dao;

import java.util.List;

import de.thm.arsnova.connector.moodle.model.MdlUser;

public interface CourseDao {
	List<MdlUser> getCourseUsers(String courseid);
}