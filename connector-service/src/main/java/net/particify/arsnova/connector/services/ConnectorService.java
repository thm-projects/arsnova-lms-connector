package net.particify.arsnova.connector.services;

import org.springframework.security.access.prepost.PreAuthorize;

import net.particify.arsnova.connector.model.Courses;
import net.particify.arsnova.connector.model.Membership;

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
	@PreAuthorize(value = "hasRole('ADMIN') or hasPermission(#username, 'membership', 'read')")
	Membership getMembership(String username, String courseid);

	/** This service method resturns a list of courses the user is member
	 *
	 * @param username The users name as used in target platform
	 * @return The list of courses
	 */
	@PreAuthorize(value = "hasRole('ADMIN') or hasPermission(#username, 'courses', 'read')")
	Courses getCourses(String username);
}
