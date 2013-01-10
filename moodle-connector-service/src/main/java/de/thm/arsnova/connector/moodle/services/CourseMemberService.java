package de.thm.arsnova.connector.moodle.services;

import de.thm.arsnova.connector.moodle.model.Courses;
import de.thm.arsnova.connector.moodle.model.Membership;

/** This service class provides course member information for moodle courses
 *
 * @author Paul-Christian Volkmer
 * @since 0.0.1
 */
public interface CourseMemberService {
	/** This service method returns the state of membership
	 *
	 * @param username The users name as used in Moodle
	 * @param courseid The course ID as used in Moodle
	 * @return The state of membership
	 */
	Membership ismember(String username, String courseid);

	/** This service method resturns a list of courses the user is member
	 *
	 * @param username The users name as used in Moodle
	 * @return The list of courses
	 */
	Courses getCourses(String username);
}
