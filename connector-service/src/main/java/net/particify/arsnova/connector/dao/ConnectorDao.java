package net.particify.arsnova.connector.dao;

import java.util.List;

import net.particify.arsnova.connector.model.Course;
import net.particify.arsnova.connector.model.Membership;

public interface ConnectorDao {
	/** Returns users enlisted in course identified by ID
	 *
	 * @param courseid The course ID which identifies a course. This might be a number or a alphanumeric value
	 * @return A list of user IDs
	 */
	List<String> getCourseUsers(String courseid);

	/** Returns a list of courses the user is enlisted in
	 *
	 * @param username The users ID, equal to the user ID in authentication system
	 * @return A list of courses
	 */
	List<Course> getMembersCourses(String username);

	/** Returns the state of enlistment for one user in one course
	 *
	 * @param username The users ID, equal to the user ID in authentication system
	 * @param courseid The course ID which identifies a course. This might be a number or a alphanumeric value
	 * @return Information about the membership of one user in this course
	 */
	Membership getMembership(String username, String courseid);
}
