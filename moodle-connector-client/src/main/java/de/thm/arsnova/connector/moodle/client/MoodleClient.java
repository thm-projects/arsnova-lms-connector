package de.thm.arsnova.connector.moodle.client;

import de.thm.arsnova.connector.moodle.model.Courses;
import de.thm.arsnova.connector.moodle.model.Membership;

/**
 * <p>A simple client for moodle-connector-service
 *
 * <p>This class has to be initialized as spring bean e.g.:
 *
 * <pre>
 * &lt;bean id="moodleClient" scope="singleton" class="de.thm.arsnova.connector.moodle.client.MoodleClientImpl">
 * 	&lt;property name="serviceLocation" value="http://example.com/" />
 * 	&lt;property name="username" value="test" />
 * 	&lt;property name="password" value="test" />
 * &lt;/bean>
 * </pre>
 *
 * <p>This following example shows the steps to make use of the client library in your project:
 *
 * <pre>
 * &#064;Autowired(required=false)
 * private MoodleClient moodleClient;
 *
 * if (moodleClient != null) {
 * ... client is initialized ...
 * }
 * </pre>
 *
 * @author Paul-Christian Volkmer
 * @since 0.8.1
 *
 */
public interface MoodleClient {
	/** This service method returns the state of membership
	 *
	 * @param username The users name as used in Moodle
	 * @param courseid The course ID as used in Moodle
	 * @return The state of membership
	 */
	Membership isMember(String username, String courseid);

	/** This service method resturns a list of courses the user is member
	 *
	 * @param username The users name as used in Moodle
	 * @return The list of courses
	 */
	Courses getCourses(String username);
}
