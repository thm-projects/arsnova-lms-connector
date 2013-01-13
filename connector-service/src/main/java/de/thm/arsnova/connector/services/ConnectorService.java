package de.thm.arsnova.connector.services;

import de.thm.arsnova.connector.model.Courses;
import de.thm.arsnova.connector.model.Membership;

/** This service class provides course member information for courses
 *
 * @author Paul-Christian Volkmer
 * @since 0.0.1
 */
public interface ConnectorService {
	/** This service method returns the state of membership
	 *
	 * @param username The users name as used in target platform
	 * @param courseid The course ID as used in target platform
	 * @return The state of membership
	 */
	Membership ismember(String username, String courseid);

	/** This service method resturns a list of courses the user is member
	 *
	 * @param username The users name as used in target platform
	 * @return The list of courses
	 */
	Courses getCourses(String username);
}
