package de.thm.arsnova.connector.moodle.services;

import de.thm.arsnova.connector.moodle.model.Courses;
import de.thm.arsnova.connector.moodle.model.Membership;

public interface CourseMemberService {
	Membership ismember(String username, String courseid);
	Courses getCourses(String username);
}
