package de.thm.arsnova.connector.client;

import java.util.List;

import de.thm.arsnova.connector.model.Courses;
import de.thm.arsnova.connector.model.IliasCategoryNode;
import de.thm.arsnova.connector.model.IliasQuestion;
import de.thm.arsnova.connector.model.Membership;

/**
 * <p>A simple client for connector-service
 *
 * <p>This class has to be initialized as spring bean e.g.:
 *
 * <pre>
 * &lt;bean id="connectorClient" scope="singleton" class="de.thm.arsnova.connector.client.ConnectorClientImpl">
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
 * private ConnectorClient connectorClient;
 *
 * if (connectorClient != null) {
 * ... client is initialized ...
 * }
 * </pre>
 *
 * @author Paul-Christian Volkmer
 * @since 0.8.1
 *
 */
public interface ConnectorClient {

	public enum IliasQuestionSource {
		RANDOM_TEST,
		QUESTION_POOL
	}

	/** This service method returns the state of membership
	 *
	 * @param username The users name as used in target platform
	 * @param courseid The course ID as used in target platform
	 * @return The state of membership
	 * @deprecated
	 */
	@Deprecated
	Membership isMember(String username, String courseid);

	/** This service method returns the state of membership
	 *
	 * @param username The users name as used in target platform
	 * @param courseid The course ID as used in target platform
	 * @return The state of membership
	 */
	Membership getMembership(String username, String courseid);

	/** This service method returns a list of courses the user is member
	 *
	 * @param username The users name as used in target platform
	 * @return The list of courses
	 */
	Courses getCourses(String username);

	/** Returns a dump of the Ilias repository tree.
	 *
	 * @param refId The root nodes ID as reference ID
	 * @return A list of category nodes
	 */
	IliasCategoryNode getTreeObjects(int refId);

	/** Returns a list of questions identified by the parent question pool reference ID
	 *
	 * The question source defaults to RANDOM_SOURCE
	 *
	 * @param refId The reference id of the question pool containing this question
	 * @return A list of questions containing the question, possible answers and feedback.
	 */
	List<IliasQuestion> getQuestions(int refId);

	/** Returns a list of questions identified by the parent question pool reference ID
	 *
	 * @param refId The reference id of the question pool containing this question
	 * @param source The source of the questions
	 * @return A list of questions containing the question, possible answers and feedback.
	 */
	List<IliasQuestion> getQuestions(int refId, IliasQuestionSource source);
}
